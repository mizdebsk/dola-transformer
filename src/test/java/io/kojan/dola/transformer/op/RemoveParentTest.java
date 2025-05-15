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
import org.apache.maven.api.model.Model;
import org.apache.maven.api.model.Parent;
import org.junit.jupiter.api.Test;

public class RemoveParentTest {

    @Test
    public void testParentMatchesAndIsRemoved() {
        // Setup matcher arg
        String matcherArg = "com.example:parent-artifact";
        RemoveParent transformer = new RemoveParent();
        Transformation transformation = transformer.produceTransformation(matcherArg);

        // Mock parent
        Parent parent = mock(Parent.class);
        expect(parent.getGroupId()).andReturn("com.example").anyTimes();
        expect(parent.getArtifactId()).andReturn("parent-artifact").anyTimes();
        expect(parent.getVersion()).andReturn("1.0.0").anyTimes();

        // Mock original model
        Model originalModel = mock(Model.class);
        expect(originalModel.getParent()).andReturn(parent).anyTimes();
        expect(originalModel.getGroupId()).andReturn(null).anyTimes(); // triggers groupId copy
        expect(originalModel.getVersion()).andReturn(null).anyTimes(); // triggers version copy

        // Expect withParent(null), withGroupId(), and withVersion() calls
        Model modelWithoutParent = mock(Model.class);
        expect(originalModel.withParent(null)).andReturn(modelWithoutParent);

        Model modelWithGroupId = mock(Model.class);
        expect(modelWithoutParent.withGroupId("com.example")).andReturn(modelWithGroupId);

        Model finalModel = mock(Model.class);
        expect(modelWithGroupId.withVersion("1.0.0")).andReturn(finalModel);

        replay(parent, originalModel, modelWithoutParent, modelWithGroupId, finalModel);

        Model result = transformation.transform(originalModel);
        assertSame(finalModel, result);

        verify(parent, originalModel, modelWithoutParent, modelWithGroupId, finalModel);
    }

    @Test
    public void testParentDoesNotMatch_NoChange() {
        String matcherArg = "com.example:some-other-parent";
        RemoveParent transformer = new RemoveParent();
        Transformation transformation = transformer.produceTransformation(matcherArg);

        // Mock parent
        Parent parent = mock(Parent.class);
        expect(parent.getGroupId()).andReturn("org.other").anyTimes();
        expect(parent.getArtifactId()).andReturn("unrelated").anyTimes();

        // Mock model
        Model model = mock(Model.class);
        expect(model.getParent()).andReturn(parent);

        replay(parent, model);

        Model result = transformation.transform(model);
        assertSame(model, result); // unchanged

        verify(parent, model);
    }

    @Test
    public void testNoParent_NoChange() {
        String matcherArg = "com.example:parent";
        RemoveParent transformer = new RemoveParent();
        Transformation transformation = transformer.produceTransformation(matcherArg);

        // Mock model with null parent
        Model model = mock(Model.class);
        expect(model.getParent()).andReturn(null);

        replay(model);

        Model result = transformation.transform(model);
        assertSame(model, result); // unchanged

        verify(model);
    }
}
