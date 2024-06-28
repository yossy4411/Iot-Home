/*
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

package com.okayugroup.IotHome;

import com.okayugroup.IotHome.event.EventController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class Resources {
    public final String parent = "/api"; // API側の親を定義します
    @GetMapping(parent + "/{root}/{controller}")
    public Object getIt(@PathVariable String root, @PathVariable String controller) {
        var re = EventController.execute(root, controller);
        if (re != null) return re.result();
        return null;
    }

}
