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

import org.apache.maven.api.model.Model;

/**
 * Represents an operation that transforms a Maven {@link Model}.
 *
 * <p>Implementations define how to modify or process a Maven model as part of a transformation
 * pipeline.
 */
public interface Transformation {

    /**
     * Applies a transformation to the given {@link Model}.
     *
     * @param model the input model to transform; must not be {@code null}
     * @return the transformed {@link Model}, which may be the same instance or a modified copy
     */
    Model transform(Model model);
}
