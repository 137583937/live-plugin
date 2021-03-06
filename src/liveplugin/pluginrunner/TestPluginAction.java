/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package liveplugin.pluginrunner;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import liveplugin.IDEUtil;
import liveplugin.Icons;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static liveplugin.pluginrunner.GroovyPluginRunner.TEST_SCRIPT;

public class TestPluginAction extends AnAction implements DumbAware {
	public TestPluginAction() {
		super("Run Plugin Tests", "Run Plugin Integration Tests", Icons.TEST_PLUGIN_ICON);
	}

	@Override public void actionPerformed(@NotNull AnActionEvent event) {
		testCurrentPlugin(event);
	}

	@Override public void update(@NotNull AnActionEvent event) {
		event.getPresentation().setEnabled(!RunPluginAction.findCurrentPluginIds(event).isEmpty());
	}

    private void testCurrentPlugin(AnActionEvent event) {
        IDEUtil.saveAllFiles();
        List<String> pluginIds = RunPluginAction.findCurrentPluginIds(event);
        ErrorReporter errorReporter = new ErrorReporter();
        RunPluginAction.runPlugins(pluginIds, event, errorReporter, createPluginRunners(errorReporter));
    }

    public static List<PluginRunner> createPluginRunners(ErrorReporter errorReporter) {
        List<PluginRunner> result = new ArrayList<>();
        result.add(new GroovyPluginRunner(TEST_SCRIPT, errorReporter, RunPluginAction.environment()));
        return result;
    }
}
