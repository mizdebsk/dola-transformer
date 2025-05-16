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

    private final List<Transformation> transformations;

    @Inject
    public DolaTransformer(Map<String, Transformer> transformers) {
        transformations =
                TransformationParser.parseFromProperties(transformers, System.getProperties());
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
