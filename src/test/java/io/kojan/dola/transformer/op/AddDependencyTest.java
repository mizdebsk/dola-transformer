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
import org.apache.maven.api.model.Dependency;
import org.apache.maven.api.model.Model;
import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.jupiter.api.Test;

public class AddDependencyTest {

    @Test
    public void testDependencyIsAddedWhenNotPresent() {
        String arg = "com.example:my-lib:1.2.3:compile";
        AddDependency transformer = new AddDependency();
        Transformation transformation = transformer.produceTransformation(arg);

        // Mock existing dependency
        Dependency existingDep1 = mock(Dependency.class);
        expect(existingDep1.getGroupId()).andReturn("com.example").anyTimes();
        expect(existingDep1.getArtifactId()).andReturn("existing").anyTimes();
        Dependency existingDep2 = mock(Dependency.class);
        expect(existingDep2.getGroupId()).andReturn("org.other").anyTimes();
        expect(existingDep2.getArtifactId()).andReturn("another-dep").anyTimes();

        // Mock model
        Model originalModel = mock(Model.class);
        expect(originalModel.getDependencies()).andReturn(List.of(existingDep1, existingDep2));

        // Capture the list passed to withDependencies
        Capture<List<Dependency>> capturedDeps = EasyMock.newCapture();
        Model newModel = mock(Model.class);
        expect(originalModel.withDependencies(capture(capturedDeps))).andReturn(newModel);

        replay(existingDep1, existingDep2, originalModel, newModel);

        Model result = transformation.transform(originalModel);
        assertSame(newModel, result);

        // Assert the added dependency is at the end
        assertTrue(capturedDeps.hasCaptured());
        List<Dependency> updatedDeps = capturedDeps.getValue();

        assertEquals(3, updatedDeps.size(), "Expected one existing and one added dependency");
        assertSame(existingDep1, updatedDeps.get(0));
        assertSame(existingDep2, updatedDeps.get(1));

        Dependency added = updatedDeps.get(2);
        assertEquals("com.example", added.getGroupId());
        assertEquals("my-lib", added.getArtifactId());
        assertEquals("1.2.3", added.getVersion());
        assertEquals("compile", added.getScope());

        verify(existingDep1, existingDep2, originalModel, newModel);
    }

    @Test
    public void testDependencyAlreadyExists_ModelUnchanged() {
        String arg = "com.example:my-lib:1.2.3:compile";
        AddDependency transformer = new AddDependency();
        Transformation transformation = transformer.produceTransformation(arg);

        Dependency existingDep = mock(Dependency.class);
        expect(existingDep.getGroupId()).andReturn("com.example").anyTimes();
        expect(existingDep.getArtifactId()).andReturn("my-lib").anyTimes();

        Model model = mock(Model.class);
        expect(model.getDependencies()).andReturn(List.of(existingDep));

        replay(existingDep, model);

        Model result = transformation.transform(model);
        assertSame(model, result); // should not add again

        verify(existingDep, model);
    }

    @Test
    public void testNoScopeProvided_StillAddsDependency() {
        String arg = "com.example:my-lib:1.2.3"; // No scope
        AddDependency transformer = new AddDependency();
        Transformation transformation = transformer.produceTransformation(arg);

        Model originalModel = mock(Model.class);
        expect(originalModel.getDependencies()).andReturn(List.of());

        Model newModel = mock(Model.class);
        expect(originalModel.withDependencies(anyObject())).andReturn(newModel);

        replay(originalModel, newModel);

        Model result = transformation.transform(originalModel);
        assertSame(newModel, result);

        verify(originalModel, newModel);
    }

    @Test
    public void testNoVersionProvided_StillAddsDependency() {
        String arg = "com.example:my-lib"; // No version
        AddDependency transformer = new AddDependency();
        Transformation transformation = transformer.produceTransformation(arg);

        Model originalModel = mock(Model.class);
        expect(originalModel.getDependencies()).andReturn(List.of());

        Model newModel = mock(Model.class);
        expect(originalModel.withDependencies(anyObject())).andReturn(newModel);

        replay(originalModel, newModel);

        Model result = transformation.transform(originalModel);
        assertSame(newModel, result);

        verify(originalModel, newModel);
    }

    @Test
    public void testInvalidArgumentFormatThrows() {
        AddDependency transformer = new AddDependency();

        assertThrows(
                IllegalArgumentException.class,
                () -> transformer.produceTransformation("com.example:too:long:extra:part"));
        assertThrows(
                IllegalArgumentException.class,
                () -> transformer.produceTransformation("com.example")); // too short
    }
}
