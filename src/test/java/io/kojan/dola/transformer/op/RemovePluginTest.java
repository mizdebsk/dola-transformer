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
import org.apache.maven.api.model.Build;
import org.apache.maven.api.model.Model;
import org.apache.maven.api.model.Plugin;
import org.junit.jupiter.api.Test;

public class RemovePluginTest {

    @Test
    public void testPluginMatchesAndIsRemoved() {
        String matcherArg = "org.apache.maven.plugins:maven-compiler-plugin";
        RemovePlugin transformer = new RemovePlugin();
        Transformation transformation = transformer.produceTransformation(matcherArg);

        // Mock plugins
        Plugin pluginToRemove = mock(Plugin.class);
        expect(pluginToRemove.getGroupId()).andReturn("org.apache.maven.plugins").anyTimes();
        expect(pluginToRemove.getArtifactId()).andReturn("maven-compiler-plugin").anyTimes();

        Plugin pluginToKeep = mock(Plugin.class);
        expect(pluginToKeep.getGroupId()).andReturn("org.apache.maven.plugins").anyTimes();
        expect(pluginToKeep.getArtifactId()).andReturn("maven-surefire-plugin").anyTimes();

        // Mock build
        Build originalBuild = mock(Build.class);
        expect(originalBuild.getPlugins()).andReturn(List.of(pluginToRemove, pluginToKeep));

        Build newBuild = mock(Build.class);
        expect(originalBuild.withPlugins(List.of(pluginToKeep))).andReturn(newBuild);

        // Mock model
        Model originalModel = mock(Model.class);
        expect(originalModel.getBuild()).andReturn(originalBuild);
        expect(originalModel.withBuild(newBuild)).andReturn(mock(Model.class)); // final result

        replay(pluginToRemove, pluginToKeep, originalBuild, newBuild, originalModel);

        Model result = transformation.transform(originalModel);
        assertNotSame(originalModel, result);

        verify(pluginToRemove, pluginToKeep, originalBuild, newBuild, originalModel);
    }

    @Test
    public void testPluginDoesNotMatch_ModelUnchanged() {
        String matcherArg = "com.example:nonexistent-plugin";
        RemovePlugin transformer = new RemovePlugin();
        Transformation transformation = transformer.produceTransformation(matcherArg);

        Plugin plugin = mock(Plugin.class);
        expect(plugin.getGroupId()).andReturn("org.apache.maven.plugins").anyTimes();
        expect(plugin.getArtifactId()).andReturn("maven-surefire-plugin").anyTimes();

        Build build = mock(Build.class);
        expect(build.getPlugins()).andReturn(List.of(plugin));

        Model model = mock(Model.class);
        expect(model.getBuild()).andReturn(build);

        replay(plugin, build, model);

        Model result = transformation.transform(model);
        assertSame(model, result);

        verify(plugin, build, model);
    }

    @Test
    public void testNoBuildSection_ModelUnchanged() {
        String matcherArg = "any:plugin";
        RemovePlugin transformer = new RemovePlugin();
        Transformation transformation = transformer.produceTransformation(matcherArg);

        Model model = mock(Model.class);
        expect(model.getBuild()).andReturn(null);

        replay(model);

        Model result = transformation.transform(model);
        assertSame(model, result);

        verify(model);
    }
}
