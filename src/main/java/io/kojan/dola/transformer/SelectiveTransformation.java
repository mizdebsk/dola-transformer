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

/**
 * A {@code SelectiveTransformation} applies a given {@link Transformation} only to {@link Model}
 * instances that match one or more specified {@link GidAidMatcher} selectors.
 *
 * <p>If no selectors match the model's groupId and artifactId, the original model is returned
 * unchanged. If the list of selectors is empty, the transformation is always applied.
 *
 * <p>This class is useful for conditionally applying transformations in a Maven model processing
 * pipeline.
 */
public class SelectiveTransformation implements Transformation {

    /** List of GID:AID matchers used to decide whether to apply the transformation. */
    private final List<GidAidMatcher> selectors;

    /** The actual transformation to delegate to if selection criteria are met. */
    private final Transformation transformation;

    /** A human-readable description used in {@link #toString()}. */
    private final String desc;

    /**
     * Constructs a {@code SelectiveTransformation}.
     *
     * @param selectors a list of {@link GidAidMatcher} selectors to determine if the transformation
     *     should apply
     * @param transformation the transformation to apply if a selector matches
     * @param desc a textual description of this transformation, used in {@link #toString()}
     */
    public SelectiveTransformation(
            List<GidAidMatcher> selectors, Transformation transformation, String desc) {
        this.selectors = selectors;
        this.transformation = transformation;
        this.desc = desc;
    }

    /**
     * Applies the transformation to the given {@link Model} if any selector matches.
     *
     * <p>If {@code selectors} is empty, the transformation is always applied.
     *
     * @param model the Maven model to transform
     * @return the transformed model if applicable, or the original model if no selector matches
     */
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
