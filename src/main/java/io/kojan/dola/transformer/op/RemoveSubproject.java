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

import io.kojan.dola.transformer.GlobMatcher;
import io.kojan.dola.transformer.Log;
import io.kojan.dola.transformer.Transformation;
import io.kojan.dola.transformer.Transformer;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.inject.Singleton;
import org.apache.maven.api.model.Model;

@Named("removeSubproject")
@Singleton
public class RemoveSubproject implements Transformer {
    public Transformation produceTransformation(String arg) {
        GlobMatcher matcher = new GlobMatcher(arg);
        return model -> {
            List<String> subprojects = model.getSubprojects();
            List<String> newSubprojects = new ArrayList<>(subprojects.size());
            boolean modified = false;
            for (String subproject : subprojects) {
                if (matcher.matches(subproject)) {
                    modified = true;
                    Log.info("Removed subproject " + subproject + " from " + model);
                } else {
                    newSubprojects.add(subproject);
                }
            }
            if (modified) {
                Model newModel = model.withSubprojects(newSubprojects);
                Log.diff(model, newModel);
                return newModel;
            }
            return model;
        };
    }
}
