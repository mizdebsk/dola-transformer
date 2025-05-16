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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class TransformationParser {
    public static List<Transformation> parseFromProperties(
            Map<String, Transformer> transformers, Properties properties) {
        List<Transformation> transformations = new ArrayList<>();
        List<String> keys =
                properties.keySet().stream()
                        .map(Object::toString)
                        .filter(key -> key.toString().startsWith("dola.transformer.insn."))
                        .sorted()
                        .toList();
        for (String key : keys) {
            String op = key.substring(key.lastIndexOf('.') + 1);
            String val = properties.getProperty(key);
            String arg = val;
            int j = val.lastIndexOf('@');
            String sel = null;
            Map<String, GidAidMatcher> selectors = new LinkedHashMap<>();
            if (j >= 0) {
                arg = val.substring(0, j);
                sel = val.substring(j + 1);
                if (sel.startsWith("(") && sel.endsWith(")")) {
                    sel = sel.substring(1, sel.length() - 1);
                }
                for (int i = sel.indexOf(','); i >= 0; i = sel.indexOf(',')) {
                    String sel0 = sel.substring(0, i);
                    selectors.put(sel0, new GidAidMatcher(sel0));
                    sel = sel.substring(i + 1);
                }
                selectors.put(sel, new GidAidMatcher(sel));
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
                            selectors.values(),
                            transformation,
                            "op="
                                    + op
                                    + ", arg="
                                    + arg
                                    + ", sel="
                                    + String.join(",", selectors.keySet())));
        }
        return transformations;
    }
}
