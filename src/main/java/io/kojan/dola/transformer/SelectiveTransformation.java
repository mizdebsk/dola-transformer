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
import org.apache.maven.api.model.Model;

public class SelectiveTransformation implements Transformation {

    private final List<GidAidMatcher> selectors;
    private final Transformation transformation;
    private final String desc;

    public SelectiveTransformation(
            List<GidAidMatcher> selectors, Transformation transformation, String desc) {
        this.selectors = selectors;
        this.transformation = transformation;
        this.desc = desc;
    }

    @Override
    public Model transform(Model model) {
        for (GidAidMatcher selector : selectors) {
            if (selector.matches(model.getGroupId(), model.getArtifactId())) {
                return transformation.transform(model);
            }
        }
        if (selectors.isEmpty()) {
            return transformation.transform(model);
        }
        return model;
    }

    @Override
    public String toString() {
        return "Transformation[" + desc + "]";
    }
}
