/*
 * Copyright 2013-2014 RapidMiner GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rapidminer.gradle.eclipse.findbugs

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Copies FindBugs Eclipse Plugin config files to the project's .settings directory.
 *
 * @author Nils Woehler
 *
 */
class FindbugsEclipse extends DefaultTask {

	private static final String SETTINGS_FOLDER = '.settings/'

	private static final String CORE_PREFS = 'edu.umd.cs.findbugs.core.prefs'
	private static final String PLUGIN_PREFS = 'edu.umd.cs.findbugs.plugin.eclipse.prefs'

	@TaskAction
	private void copyCheckstyleConfigFiles() {

		// write core prefs
		getClass().getResource(CORE_PREFS).withInputStream { ris -> project.file(SETTINGS_FOLDER + CORE_PREFS).write ris.text }

		// write plugin prefs
		getClass().getResource(PLUGIN_PREFS).withInputStream { ris -> project.file(SETTINGS_FOLDER + PLUGIN_PREFS).write ris.text }
	}
}
