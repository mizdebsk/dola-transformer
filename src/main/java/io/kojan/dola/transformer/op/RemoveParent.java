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
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.api.model.Model;
import org.apache.maven.api.model.Parent;

@Named("removeParent")
@Singleton
public class RemoveParent implements Transformer {
    public Transformation produceTransformation(String arg) {
        GidAidMatcher matcher = new GidAidMatcher(arg);
        return model -> {
            Parent parent = model.getParent();
            if (parent != null && matcher.matches(parent.getGroupId(), parent.getArtifactId())) {
                Model newModel = model.withParent(null);
                Log.info("Removed parent " + parent + " from " + model);
                if (model.getGroupId() == null) {
                    newModel = newModel.withGroupId(parent.getGroupId());
                    Log.info("Set groupId=" + parent.getGroupId() + " for " + model);
                }
                if (model.getVersion() == null) {
                    newModel = newModel.withVersion(parent.getVersion());
                    Log.info("Set version=" + parent.getVersion() + " for " + model);
                }
                Log.diff(model, newModel);
                return newModel;
            }
            return model;
        };
    }
}
