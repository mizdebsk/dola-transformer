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

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import org.apache.maven.api.model.Model;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class SelectiveTransformationTest {

    @Test
    public void testTransformationAppliedWhenSelectorMatches() {
        // Mock Model
        Model model = EasyMock.createMock(Model.class);
        expect(model.getGroupId()).andReturn("com.example");
        expect(model.getArtifactId()).andReturn("my-artifact");

        // Mock Transformation
        Transformation mockTransformation = EasyMock.createMock(Transformation.class);
        Model transformedModel = EasyMock.createMock(Model.class);
        expect(mockTransformation.transform(model)).andReturn(transformedModel);

        replay(model, mockTransformation, transformedModel);

        // Selector matches
        GidAidMatcher matcher = new GidAidMatcher("com.example:my-artifact");
        SelectiveTransformation selective =
                new SelectiveTransformation(List.of(matcher), mockTransformation, "test-desc");

        Model result = selective.transform(model);
        assertSame(transformedModel, result);

        verify(model, mockTransformation);
    }

    @Test
    public void testTransformationNotAppliedWhenNoSelectorMatches() {
        Model model = EasyMock.createMock(Model.class);
        expect(model.getGroupId()).andReturn("com.example");
        expect(model.getArtifactId()).andReturn("other-artifact");

        replay(model);

        // Selector does NOT match
        GidAidMatcher matcher = new GidAidMatcher("com.example:my-artifact");
        Transformation mockTransformation = EasyMock.createMock(Transformation.class);

        SelectiveTransformation selective =
                new SelectiveTransformation(List.of(matcher), mockTransformation, "no-match");

        Model result = selective.transform(model);
        assertSame(model, result);

        verify(model);
    }

    @Test
    public void testTransformationAppliedWhenSelectorsEmpty() {
        Model model = EasyMock.createMock(Model.class);
        Transformation mockTransformation = EasyMock.createMock(Transformation.class);

        Model transformedModel = EasyMock.createMock(Model.class);
        expect(mockTransformation.transform(model)).andReturn(transformedModel);

        replay(model, mockTransformation, transformedModel);

        SelectiveTransformation selective =
                new SelectiveTransformation(
                        Collections.emptyList(), mockTransformation, "empty-selectors");

        Model result = selective.transform(model);
        assertSame(transformedModel, result);

        verify(model, mockTransformation);
    }
}
