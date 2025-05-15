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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class GlobMatcherTest {

    @Test
    public void testExactMatch() {
        GlobMatcher matcher = new GlobMatcher("hello");
        assertTrue(matcher.matches("hello"));
        assertFalse(matcher.matches("hello world"));
    }

    @Test
    public void testAsteriskWildcard() {
        GlobMatcher matcher = new GlobMatcher("he*o");
        assertTrue(matcher.matches("heo"));
        assertTrue(matcher.matches("hello"));
        assertTrue(matcher.matches("heyo"));
        assertFalse(matcher.matches("hillo"));
    }

    @Test
    public void testQuestionMarkWildcard() {
        GlobMatcher matcher = new GlobMatcher("he?lo");
        assertTrue(matcher.matches("hello"));
        assertTrue(matcher.matches("hezlo"));
        assertFalse(matcher.matches("heello"));
        assertFalse(matcher.matches("helo"));
    }

    @Test
    public void testMixedWildcards() {
        GlobMatcher matcher = new GlobMatcher("a?c*e");
        assertTrue(matcher.matches("abcde"));
        assertTrue(matcher.matches("axc  e"));
        assertFalse(matcher.matches("ace"));
    }

    @Test
    public void testEmptyGlob() {
        GlobMatcher matcher = new GlobMatcher("");
        assertTrue(matcher.matches("test"));
        assertTrue(matcher.matches(""));
    }
}
