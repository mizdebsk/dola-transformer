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
 * The {@code GidAidMatcher} class matches a combination of group ID (GID) and artifact ID (AID)
 * against a glob-style pattern of the form {@code "gid:aid"}.
 *
 * <p>Both parts of the pattern can include wildcards:
 *
 * <ul>
 *   <li>{@code *} matches zero or more characters
 *   <li>{@code ?} matches exactly one character
 * </ul>
 *
 * <p>Example usage:
 *
 * <pre>
 *     GidAidMatcher matcher = new GidAidMatcher("com.example:*");
 *     boolean result = matcher.matches("com.example", "my-artifact");  // returns true
 * </pre>
 */
public class GidAidMatcher {

    /** Glob pattern matcher for the group ID. */
    private final GlobMatcher gidRe;

    /** Glob pattern matcher for the artifact ID. */
    private final GlobMatcher aidRe;

    /**
     * Constructs a {@code GidAidMatcher} from a colon-separated glob pattern string.
     *
     * @param ga the glob pattern in the format {@code "gid:aid"}
     * @throws RuntimeException if the input string does not contain a colon
     */
    public GidAidMatcher(String ga) {
        int i = ga.indexOf(':');
        if (i < 0) {
            throw new RuntimeException("gid:aid string does not contain colon: " + ga);
        }
        String gid = ga.substring(0, i);
        String aid = ga.substring(i + 1);
        this.gidRe = new GlobMatcher(gid);
        this.aidRe = new GlobMatcher(aid);
    }

    /**
     * Determines whether the given {@code gid} and {@code aid} match the glob patterns.
     *
     * @param gid the group ID to test
     * @param aid the artifact ID to test
     * @return {@code true} if both {@code gid} and {@code aid} match their respective patterns;
     *     {@code false} otherwise
     */
    public boolean matches(String gid, String aid) {
        return gidRe.matches(gid) && aidRe.matches(aid);
    }
}
