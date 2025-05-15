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

import io.kojan.dola.transformer.Log;
import io.kojan.dola.transformer.Transformation;
import io.kojan.dola.transformer.Transformer;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.api.model.Dependency;
import org.apache.maven.api.model.Model;

@Named("addDependency")
@Singleton
public class AddDependency implements Transformer {

    private static Dependency parseCoords(String coords) {
        String[] parts = coords.split(":");
        if (parts.length < 2 || parts.length > 4) {
            throw new IllegalArgumentException(
                    "Expected format: groupId:artifactId[:version[:scope]]");
        }

        String groupId = parts[0];
        String artifactId = parts[1];

        Dependency.Builder builder =
                Dependency.newBuilder().groupId(groupId).artifactId(artifactId);
        if (parts.length > 2) {
            builder.version(parts[2]);
        }
        if (parts.length > 3) {
            builder.scope(parts[3]);
        }
        return builder.build();
    }

    @Override
    public Transformation produceTransformation(String arg) {
        Dependency newDependency = parseCoords(arg);

        return model -> {
            List<Dependency> existing = model.getDependencies();
            for (Dependency d : existing) {
                if (d.getGroupId().equals(newDependency.getGroupId())
                        && d.getArtifactId().equals(newDependency.getArtifactId())) {
                    // Already present - skip adding
                    return model;
                }
            }

            List<Dependency> updated = new ArrayList<>(existing);
            updated.add(newDependency);
            Model newModel = model.withDependencies(updated);

            Log.info("Added dependency " + newDependency + " to " + model);
            Log.diff(model, newModel);
            return newModel;
        };
    }
}
