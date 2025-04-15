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

import io.kojan.dola.transformer.GidAidMatcher;
import io.kojan.dola.transformer.Log;
import io.kojan.dola.transformer.Transformation;
import io.kojan.dola.transformer.Transformer;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.api.model.Build;
import org.apache.maven.api.model.Model;
import org.apache.maven.api.model.Plugin;

@Named("removePlugin")
@Singleton
public class RemovePlugin implements Transformer {
    public Transformation produceTransformation(String arg) {
        GidAidMatcher matcher = new GidAidMatcher(arg);
        return model -> {
            Build build = model.getBuild();
            if (build != null) {
                List<Plugin> plugins = build.getPlugins();
                List<Plugin> newPlugins = new ArrayList<>(plugins.size());
                boolean modified = false;
                for (Plugin plugin : plugins) {
                    if (matcher.matches(plugin.getGroupId(), plugin.getArtifactId())) {
                        modified = true;
                        Log.info("Removed plugin " + plugin + " from " + model);
                    } else {
                        newPlugins.add(plugin);
                    }
                }
                if (modified) {
                    Model newModel = model.withBuild(build.withPlugins(newPlugins));
                    Log.diff(model, newModel);
                    return newModel;
                }
            }
            return model;
        };
    }
}
