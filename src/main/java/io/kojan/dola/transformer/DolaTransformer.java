/*-
 * Copyright (c) 2025 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.kojan.dola.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.api.model.Model;
import org.apache.maven.api.spi.ModelTransformer;
import org.apache.maven.api.spi.ModelTransformerException;

@Named
@Singleton
public class DolaTransformer implements ModelTransformer {

    private final List<Transformation> transformations = new ArrayList<>();

    @Inject
    public DolaTransformer(Map<String, Transformer> transformers) {
        List<String> keys =
                System.getProperties().keySet().stream()
                        .map(Object::toString)
                        .filter(key -> key.toString().startsWith("dola.transformer.insn."))
                        .sorted()
                        .toList();
        for (String key : keys) {
            String op = key.substring(key.lastIndexOf('.') + 1);
            String val = System.getProperty(key);
            String arg = val;
            int j = val.lastIndexOf('@');
            String sel = null;
            List<GidAidMatcher> selectors = new ArrayList<>();
            if (j >= 0) {
                arg = val.substring(0, j);
                sel = val.substring(j + 1);
                if (sel.startsWith("(") && sel.endsWith(")")) {
                    sel = sel.substring(1, sel.length() - 1);
                }
                for (int i = sel.indexOf(','); i >= 0; i = sel.indexOf(',')) {
                    selectors.add(new GidAidMatcher(sel.substring(0, i)));
                    sel = sel.substring(i + 1);
                }
                selectors.add(new GidAidMatcher(sel));
            }
            Log.debug("instruction op=" + op + ", arg=" + arg);
            Transformer transformer = transformers.get(op);
            if (transformer == null) {
                Log.debug("no such transformer: " + op);
                continue;
            }
            Transformation transformation = transformer.produceTransformation(arg);
            transformations.add(
                    new SelectiveTransformation(
                            selectors,
                            transformation,
                            "op=" + op + ", arg=" + arg + ", sel=" + sel));
        }
    }

    public Model transform(Model model) {
        for (Transformation transformation : transformations) {
            Log.debug("    applying " + transformation);
            Model newModel = transformation.transform(model);
            if (newModel != model) {
                Log.debug("        MODIFIED");
                model = newModel;
            }
        }
        return model;
    }

    public Model transformFileModel(Model model) throws ModelTransformerException {
        Log.debug("transformFileModel " + model);
        return transform(model);
    }

    public Model transformRawModel(Model model) throws ModelTransformerException {
        Log.debug("transformRawModel " + model);
        return transform(model);
    }

    public Model transformEffectiveModel(Model model) throws ModelTransformerException {
        Log.debug("transformEffectiveModel " + model);
        return transform(model);
    }
}
