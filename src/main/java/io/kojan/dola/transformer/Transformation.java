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
 * Represents a transformation operation that modifies a Maven {@link Model}.
 *
 * <p>Transformations are applied to manipulate POM models programmatically.
 *
 * <p>Implementations may update elements such as:
 *
 * <ul>
 *   <li>Dependencies
 *   <li>Plugins
 *   <li>Parent declarations
 *   <li>Modules (subprojects)
 *   <li>Project metadata (groupId, version, etc.)
 * </ul>
 *
 * <p>Transformations may return the original model instance (if no change is needed) or a modified
 * one, depending on the logic.
 */
public interface Transformation {

    /**
     * Applies a transformation to the given Maven {@link Model}.
     *
     * @param model the input model to transform; must not be {@code null}
     * @return the transformed {@link Model}; may be the original instance if no changes are made
     */
    Model transform(Model model);
}
