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

public class GidAidMatcherTest {

    @Test
    public void testExactMatch() {
        GidAidMatcher matcher = new GidAidMatcher("com.example:my-artifact");
        assertTrue(matcher.matches("com.example", "my-artifact"));
        assertFalse(matcher.matches("com.example", "other-artifact"));
        assertFalse(matcher.matches("com.other", "my-artifact"));
    }

    @Test
    public void testWildcardInAid() {
        GidAidMatcher matcher = new GidAidMatcher("com.example:my-*");
        assertTrue(matcher.matches("com.example", "my-artifact"));
        assertTrue(matcher.matches("com.example", "my-library"));
        assertFalse(matcher.matches("com.example", "other-lib"));
    }

    @Test
    public void testWildcardInGid() {
        GidAidMatcher matcher = new GidAidMatcher("com.*:artifact");
        assertTrue(matcher.matches("com.example", "artifact"));
        assertTrue(matcher.matches("com.test", "artifact"));
        assertFalse(matcher.matches("org.example", "artifact"));
    }

    @Test
    public void testBothWildcards() {
        GidAidMatcher matcher = new GidAidMatcher("com.??:my-*");
        assertTrue(matcher.matches("com.ab", "my-artifact"));
        assertFalse(matcher.matches("com.abc", "my-artifact")); // too many chars for ??
        assertFalse(matcher.matches("com.ab", "artifact")); // doesn't start with my-
    }

    @Test
    public void testEmptyPatternThrows() {
        Exception exception =
                assertThrows(
                        RuntimeException.class,
                        () -> {
                            new GidAidMatcher("com.example"); // Missing colon
                        });
        assertTrue(exception.getMessage().contains("does not contain colon"));
    }

    @Test
    public void testEmptyGlobMatchesEverything() {
        GidAidMatcher matcher = new GidAidMatcher(":");
        assertTrue(matcher.matches("anything", "anything"));
        assertTrue(matcher.matches("", ""));
    }
}
