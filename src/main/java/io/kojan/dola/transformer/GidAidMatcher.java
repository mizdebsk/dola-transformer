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

public class GidAidMatcher {
    private final GlobMatcher gidRe;
    private final GlobMatcher aidRe;

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

    public boolean matches(String gid, String aid) {
        return gidRe.matches(aid) && aidRe.matches(aid);
    }
}
