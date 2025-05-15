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
package io.kojan.dola.transformer.op;

import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.*;

import io.kojan.dola.transformer.Transformation;
import java.util.List;
import org.apache.maven.api.model.Model;
import org.junit.jupiter.api.Test;

public class RemoveSubprojectTest {

    @Test
    public void testSubprojectMatchesAndIsRemoved() {
        String matcherArg = "modules-*";
        RemoveSubproject transformer = new RemoveSubproject();
        Transformation transformation = transformer.produceTransformation(matcherArg);

        List<String> originalSubprojects = List.of("core", "modules-impl", "modules-api");

        // Mock model
        Model originalModel = mock(Model.class);
        expect(originalModel.getSubprojects()).andReturn(originalSubprojects);

        Model newModel = mock(Model.class);
        expect(originalModel.withSubprojects(List.of("core"))).andReturn(newModel);

        replay(originalModel, newModel);

        Model result = transformation.transform(originalModel);
        assertSame(newModel, result);

        verify(originalModel, newModel);
    }

    @Test
    public void testNoSubprojectMatches_ModelUnchanged() {
        String matcherArg = "does-not-match-*";
        RemoveSubproject transformer = new RemoveSubproject();
        Transformation transformation = transformer.produceTransformation(matcherArg);

        List<String> subprojects = List.of("core", "web", "api");

        Model model = mock(Model.class);
        expect(model.getSubprojects()).andReturn(subprojects);

        replay(model);

        Model result = transformation.transform(model);
        assertSame(model, result); // unchanged

        verify(model);
    }

    @Test
    public void testEmptySubprojectList_ModelUnchanged() {
        String matcherArg = "anything";
        RemoveSubproject transformer = new RemoveSubproject();
        Transformation transformation = transformer.produceTransformation(matcherArg);

        Model model = mock(Model.class);
        expect(model.getSubprojects()).andReturn(List.of());

        replay(model);

        Model result = transformation.transform(model);
        assertSame(model, result); // unchanged because list is empty

        verify(model);
    }
}
