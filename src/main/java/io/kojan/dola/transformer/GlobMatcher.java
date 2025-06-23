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

import java.util.regex.Pattern;

/**
 * The {@code GlobMatcher} class provides functionality to match strings using simple glob patterns.
 *
 * <p>Supported glob syntax:
 *
 * <ul>
 *   <li>{@code *} matches zero or more characters
 *   <li>{@code ?} matches exactly one character
 * </ul>
 *
 * <p>Example usage:
 *
 * <pre>
 *     GlobMatcher matcher = new GlobMatcher("he*o");
 *     boolean result = matcher.matches("hello");  // returns true
 * </pre>
 */
public class GlobMatcher {

    /** The compiled regex pattern generated from the glob string. */
    private final Pattern pattern;

    /**
     * Converts a glob pattern to a corresponding regular expression {@link Pattern}.
     *
     * <p>The conversion rules are:
     *
     * <ul>
     *   <li>{@code *} is converted to {@code .*}
     *   <li>{@code ?} is converted to {@code .?}
     * </ul>
     *
     * @param glob the glob pattern to convert
     * @return the compiled {@link Pattern}, or {@code null} if the glob is empty
     */
    protected static Pattern glob2re(String glob) {
        if (glob.isEmpty()) {
            return null;
        }
        return Pattern.compile(glob.replaceAll("\\*", ".*").replaceAll("\\?", "."));
    }

    /**
     * Constructs a {@code GlobMatcher} using the specified glob pattern.
     *
     * @param glob the glob pattern to use for matching
     */
    public GlobMatcher(String glob) {
        pattern = glob2re(glob);
    }

    /**
     * Tests whether the given string matches the glob pattern.
     *
     * @param str the string to test; if {@code null}, the result is {@code false}
     * @return {@code true} if the pattern is {@code null} (i.e., empty glob) or the string matches
     *     the pattern; {@code false} otherwise, including if {@code str} is {@code null}
     */
    public boolean matches(String str) {
        return str != null && (pattern == null || pattern.matcher(str).matches());
    }
}
