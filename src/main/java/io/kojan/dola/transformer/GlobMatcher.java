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

public class GlobMatcher {

    private final Pattern pattern;

    protected static Pattern glob2re(String glob) {
        if (glob.isEmpty()) {
            return null;
        }
        return Pattern.compile(glob.replaceAll("\\*", ".*").replaceAll("\\?", "."));
    }

    public GlobMatcher(String glob) {
        pattern = glob2re(glob);
    }

    public boolean matches(String str) {
        return pattern == null || pattern.matcher(str).matches();
    }
}
