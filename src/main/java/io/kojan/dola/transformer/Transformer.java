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

/**
 * A factory interface for producing {@link Transformation} instances based on a string argument.
 *
 * <p>Implementations interpret the argument and return a corresponding transformation.
 */
public interface Transformer {

    /**
     * Produces a {@link Transformation} based on the given argument.
     *
     * @param arg a string input used to configure or select the transformation; must not be {@code
     *     null}
     * @return a {@link Transformation} instance
     */
    Transformation produceTransformation(String arg);
}
