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
import org.apache.maven.api.model.Dependency;
import org.apache.maven.api.model.Model;

@Named("removeDependency")
@Singleton
public class RemoveDependency implements Transformer {
    public Transformation produceTransformation(String arg) {
        GidAidMatcher matcher = new GidAidMatcher(arg);
        return model -> {
            List<Dependency> dependencies = model.getDependencies();
            List<Dependency> newDependencies = new ArrayList<>(dependencies.size());
            boolean modified = false;
            for (Dependency dependency : dependencies) {
                if (matcher.matches(dependency.getGroupId(), dependency.getArtifactId())) {
                    modified = true;
                    Log.info("Removed dependency " + dependency + " from " + model);
                } else {
                    newDependencies.add(dependency);
                }
            }
            if (modified) {
                Model newModel = model.withDependencies(newDependencies);
                Log.diff(model, newModel);
                return newModel;
            }
            return model;
        };
    }
}
