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
import org.junit.jupiter.api.Test;

public class RemoveDependencyTest {

    @Test
    public void testDependencyMatchesAndIsRemoved() {
        String matcherArg = "com.example:to-remove";
        RemoveDependency transformer = new RemoveDependency();
        Transformation transformation = transformer.produceTransformation(matcherArg);

        // Mock dependencies
        Dependency matchDep = mock(Dependency.class);
        expect(matchDep.getGroupId()).andReturn("com.example").anyTimes();
        expect(matchDep.getArtifactId()).andReturn("to-remove").anyTimes();

        Dependency keepDep = mock(Dependency.class);
        expect(keepDep.getGroupId()).andReturn("com.other").anyTimes();
        expect(keepDep.getArtifactId()).andReturn("keep").anyTimes();

        // Mock model
        Model originalModel = mock(Model.class);
        expect(originalModel.getDependencies()).andReturn(List.of(matchDep, keepDep));

        Model newModel = mock(Model.class);
        expect(originalModel.withDependencies(List.of(keepDep))).andReturn(newModel);

        replay(matchDep, keepDep, originalModel, newModel);

        Model result = transformation.transform(originalModel);
        assertSame(newModel, result);

        verify(matchDep, keepDep, originalModel, newModel);
    }

    @Test
    public void testNoDependenciesMatch_ModelUnchanged() {
        String matcherArg = "com.example:nonexistent";
        RemoveDependency transformer = new RemoveDependency();
        Transformation transformation = transformer.produceTransformation(matcherArg);

        Dependency dep = mock(Dependency.class);
        expect(dep.getGroupId()).andReturn("com.other").anyTimes();
        expect(dep.getArtifactId()).andReturn("keep").anyTimes();

        Model model = mock(Model.class);
        expect(model.getDependencies()).andReturn(List.of(dep));

        replay(dep, model);

        Model result = transformation.transform(model);
        assertSame(model, result); // unchanged

        verify(dep, model);
    }

    @Test
    public void testEmptyDependencies_ModelUnchanged() {
        String matcherArg = "com.example:whatever";
        RemoveDependency transformer = new RemoveDependency();
        Transformation transformation = transformer.produceTransformation(matcherArg);

        Model model = mock(Model.class);
        expect(model.getDependencies()).andReturn(List.of());

        replay(model);

        Model result = transformation.transform(model);
        assertSame(model, result); // nothing to remove

        verify(model);
    }
}
