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
package com.rapidminer.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.plugins.ide.eclipse.EclipsePlugin
import org.gradle.testing.jacoco.plugins.JacocoPlugin

import com.rapidminer.gradle.checkstyle.InitCheckstyleConfigFiles
import com.rapidminer.gradle.codenarc.InitCodenarcConfigFiles
import com.rapidminer.gradle.eclipse.checkstyle.CheckstyleEclipse
import com.rapidminer.gradle.eclipse.findbugs.FindbugsEclipse
import com.rapidminer.gradle.eclipse.pmd.PMDEclipse



/**
 * The Code Quality plugin class which contains all logic for applying code quality plugins.
 *
 * @author Nils Woehler
 *
 */
class RapidMinerCodeQualityPlugin implements Plugin<Project> {

	private static final String TASK_GROUP = 'RapidMiner Code Quality'
	private static final String ALL_JAVA = '**/*.java'
	private static final String ALL_GROOVY = '**/*.groovy'

	private static final String HEADER_CHECK = 'headerCheck'
	private static final String JDEPEND = 'jdepend'
	private static final String JACOCO = 'jacoco'

	private static final String PMD = 'pmd'
	private static final String ECLIPSE_PMD_CONFIG_TASK = 'eclipsePMD'

	private static final String CHECKSTYLE = 'checkstyle'
	private static final String ECLIPSE_CHECKSTYLE_CONFIG_TASK = 'eclipseCheckstyle'
	private static final String INIT_CHECKSTYLE_CONFIG_TASK = 'checkstyleInitDefaultConfig'
	private static final String CHECKSTYLE_GEN_REPORT = 'HTMLReport'

	private static final String CODENARC = 'codenarc'
	private static final String INIT_CODENARC_CONFIG_TASK = 'codenarcInitDefaultConfig'

	private static final String FINDBUGS = 'findbugs'
	private static final String ECLIPSE_FINDBUGS_CONFIG_TASK = 'eclipseFindBugs'

	@Override
	void apply(Project project) {

		// create 'codeQuality' project extension
		CodeQualityConfiguration qualityExt = project.extensions.create("codeQuality", CodeQualityConfiguration)

		// Adds JaCoCo check tasks
		// Cannot be done in afterEvaluate as this breaks the JaCoCo plugin tasks
		if(applyPlugin(project, JACOCO, qualityExt.jacoco) &&
		(project.plugins.withType(org.gradle.api.plugins.GroovyPlugin) || project.plugins.withType(org.gradle.api.plugins.JavaPlugin))){
			configureJaCoCo(project, qualityExt)
		}

		project.afterEvaluate {
			def configurationDir = project.rootProject.file(qualityExt.configDir)

			// add header check tasks
			if(applyPlugin(project, HEADER_CHECK, qualityExt.headerCheck)) {
				configureHeaderCheck(project, qualityExt, configurationDir)
			}

			if(project.plugins.withType(GroovyPlugin)) {
				project.logger.info("${project.name} is a Groovy project. Only checking for Groovy code quality plugins.")

				if(applyPlugin(project, CODENARC, qualityExt.codenarc)) {
					// add codenarc plugin tasks of project is a groovy project
					configureCodeNarc(project, qualityExt, configurationDir)
				}

			} else if(project.plugins.withType(JavaPlugin)) {
				project.logger.info("${project.name} is a Java project. Only checking for Java code quality plugins.")

				if(applyPlugin(project, CHECKSTYLE,  qualityExt.checkstyle)) {
					// add checkstyle plugin tasks
					configureCheckstyle(project, qualityExt, configurationDir)
				}

				// add JDepend check tasks
				if(applyPlugin(project, JDEPEND, qualityExt.jdepend)) {
					configureJDepend(project, qualityExt, configurationDir)
				}

				// add FindBugs check tasks
				if(applyPlugin(project, FINDBUGS, qualityExt.findbugs)) {
					configureFindBugs(project, qualityExt, configurationDir)
				}

				// add JaCoCo check tasks
				if(applyPlugin(project, PMD, qualityExt.pmd)) {
					configurePMD(project, qualityExt)
				}
			} else {
				project.logger.info('Project is neither a Java nor a Groovy project. Only header check plugin applied.')
			}
		}
	}

	private boolean applyPlugin(Project project, String propertyKey, Boolean extensionValue){
		if(project.hasProperty(propertyKey)) {
			def property = project.properties[propertyKey]
			if(property instanceof String) {
				return Boolean.valueOf(property)
			} else {
				return false
			}
		} else {
			return extensionValue
		}
	}

	private void configureJaCoCo(Project project, CodeQualityConfiguration codeExt) {
		project.apply plugin: JacocoPlugin
	}

	private void configurePMD(Project project, CodeQualityConfiguration codeExt) {
		project.configure(project){
			apply plugin: 'pmd'

			File ruleSet = project.rootProject.file(codeExt.configDir + '/pmd/ruleset.xml')

			pmd {
				ignoreFailures = { codeExt.pmdIgnoreErrors }
				sourceSets = [sourceSets.main]
				ruleSetFiles = files(ruleSet)
				
				// disable predfined basic ruleset
				ruleSets = []
				
				// adds Java 8 support
				toolVersion = '5.1.3'
			}

			// Create task which allows to configure PMD Eclipse plugin
			Task initPMDEclipseTask = tasks.create(name: ECLIPSE_PMD_CONFIG_TASK, type: PMDEclipse)
			initPMDEclipseTask.group = TASK_GROUP
			initPMDEclipseTask.description = "Creates PMD Eclipse plugin config files for the current project."

			project.tasks.findAll { return it.name.startsWith('pmd') }.each { it.dependsOn initPMDEclipseTask }

			initPMDEclipseTask.configure { rulesetFile = ruleSet }

			// Ensure findbugs config files are copied if project applies Eclipse plugin
			project.plugins.withType(EclipsePlugin) {
				project.tasks.eclipse.dependsOn initPMDEclipseTask

				// Also configure PMD nature and buildCommand
				eclipse.project {
					natures 'net.sourceforge.pmd.eclipse.plugin.pmdNature'
					buildCommand 'net.sourceforge.pmd.eclipse.plugin.pmdBuilder'
				}
			}

		}
	}

	private void configureJDepend(Project project, CodeQualityConfiguration codeExt, File configurationDir) {
		project.configure(project){
			apply plugin: 'jdepend'

			jdepend {
				sourceSets = [project.sourceSets.main]

				ignoreFailures = { codeExt.jdependIgnoreErrors }
				reportsDir = file("${project.buildDir}/reports/jdepend")
			}
		}
	}

	private void configureFindBugs(Project project, CodeQualityConfiguration codeExt, File configurationDir) {
		project.configure(project){
			apply plugin: 'findbugs'

			findbugs {
				sourceSets = [project.sourceSets.main]

				ignoreFailures = { codeExt.findbugsIgnoreErrors }
				reportsDir = file("${project.buildDir}/reports/findbugs")

				if(file(codeExt.findbugsExcludeFilter).exists()){
					excludeFilter = file(codeExt.findbugsExcludeFilter)
				}
				
				// adds Java 8 support
				toolVersion = '3.0.0'

				effort = 'default'
				reportLevel = 'medium'
			}


			// check if provided or providedCompile configuration exists
			if(project.configurations.find { it.name == 'provided' }) {
				dependencies { provided 'com.google.code.findbugs:annotations:3.0.0' }
			} else if(project.configurations.find { it.name == 'providedCompile' }) {
				dependencies { providedCompile 'com.google.code.findbugs:annotations:3.0.0' }
			} else {
				project.logger.info('Configuration \'provided\' does not exist. Skip adding of FindBugs annotation library dependency.')
			}

			// Create task which allows to configure FindBugs Eclipse plugin
			Task initFindbugsEclipseTask = tasks.create(name: ECLIPSE_FINDBUGS_CONFIG_TASK, type: FindbugsEclipse)
			initFindbugsEclipseTask.group = TASK_GROUP
			initFindbugsEclipseTask.description = "Creates FindBugs Eclipse plugin config files for the current project."

			// Ensure findbugs config files are copied if project applies Eclipse plugin
			project.plugins.withType(EclipsePlugin) {
				project.tasks.eclipse.dependsOn initFindbugsEclipseTask

				// Also configure FindBugs nature and buildCommand
				eclipse.project {
					natures 'edu.umd.cs.findbugs.plugin.eclipse.findbugsNature'
					buildCommand 'edu.umd.cs.findbugs.plugin.eclipse.findbugsBuilder'
				}
			}
		}
	}

	private void configureHeaderCheck(Project project, CodeQualityConfiguration codeExt, File configurationDir) {
		project.configure(project) {
			apply plugin: 'com.github.hierynomus.license'

			license.ext.year = Calendar.getInstance().get(Calendar.YEAR)
			license {
				if(codeExt.headerCheckUseRootConfig) {
					header rootProject.file(codeExt.headerFile)
				} else {
					header project.file(codeExt.headerFile)
				}
				ignoreFailures codeExt.headerCheckIgnoreErrors
				includes([ALL_JAVA, ALL_GROOVY])
			}
		}
	}


	private void configureCodeNarc(Project project, CodeQualityConfiguration ext, File configurationDir) {
		project.configure(project) {
			apply plugin: CODENARC

			// ensure codenarc config file is in place
			Task initTask = tasks.create(name: INIT_CODENARC_CONFIG_TASK, type: InitCodenarcConfigFiles)
			initTask.group = TASK_GROUP
			initTask.description = "Copies the default codenarc.conf files to " +
					"the configured configuration directory."

			def codenarcConfigDir = new File(configurationDir.absolutePath, CODENARC)

			// Configure codenarc tasks
			if(ext.codenarcUseDefaultConfig) {
				project.tasks.each { t ->
					if(t.name != INIT_CODENARC_CONFIG_TASK && t.name.startsWith(CODENARC)) {
						t.dependsOn codenarcInitDefaultConfig
					}
				}
				codenarcInitDefaultConfig {
					configDir = codenarcConfigDir
					codenarcFileName = ext.codenarcConfigFileName
				}
			}

			codenarc {
				ignoreFailures = ext.codenarcIgnoreErrors
				configFile = new File(codenarcConfigDir.absolutePath, ext.codenarcConfigFileName)
			}
		}
	}

	private void configureCheckstyle(Project project, CodeQualityConfiguration ext, File configurationDir) {
		project.configure(project) {
			apply plugin: CHECKSTYLE

			File checkstyleConfigDir = new File(configurationDir.absolutePath, CHECKSTYLE)
			File checkstyleConfigFile = new File(checkstyleConfigDir.absolutePath, ext.checkstyleConfigFileName)

			// adds Java 8 support
			checkstyle.toolVersion = '5.9'
			
			checkstyleMain {
				ignoreFailures = ext.checkstyleIgnoreErrors
				reports {
					include (ALL_JAVA)
					xml { destination "${project.buildDir}/reports/checkstyle/main.xml" }
				}
				configFile checkstyleConfigFile
			}

			checkstyleTest {
				ignoreFailures = ext.checkstyleIgnoreErrors
				reports {
					include (ALL_JAVA)
					xml { destination "${project.buildDir}/reports/checkstyle/test.xml" }
				}
				configFile checkstyleConfigFile
			}

			// Gather all checkstyle tasks
			project.tasks.findAll { t ->
				return t.name.startsWith(CHECKSTYLE)
			}.each { t ->
				def type =  t.name.substring(CHECKSTYLE.length())
				Task genReportTask = project.tasks.create(name: CHECKSTYLE + type.capitalize() + CHECKSTYLE_GEN_REPORT){
					description 'Task that generates a HTML report from the checkstyle task result'
					doLast {
						checkstyleReport(project, project.buildDir, configurationDir, type.toLowerCase())
					}
				}
				t.finalizedBy genReportTask
			}

			// ensure checkstyle config files are in place
			Task generateCheckstyleConfigTask = tasks.create(name: INIT_CHECKSTYLE_CONFIG_TASK, type: InitCheckstyleConfigFiles)
			generateCheckstyleConfigTask.group = TASK_GROUP
			generateCheckstyleConfigTask.description = "Copies the default checkstyle.xml files to " +
					"the configured configuration directory."

			// Configure checkstyle init config tasks
			if(ext.checkstyleUseDefaultConfig) {
				project.tasks.each { t ->
					if(t.name != INIT_CHECKSTYLE_CONFIG_TASK && t.name.startsWith(CHECKSTYLE)) {
						t.dependsOn generateCheckstyleConfigTask
					}
				}
				checkstyleInitDefaultConfig {
					configDir = checkstyleConfigDir
					checkstyleFileName = ext.checkstyleConfigFileName
				}

				// if default config should be used, also configure Eclipse plugin config file task
				Task eclipseCheckstyle = tasks.create(name: ECLIPSE_CHECKSTYLE_CONFIG_TASK, type: CheckstyleEclipse)
				eclipseCheckstyle.group = TASK_GROUP
				eclipseCheckstyle.description = "Creates Checkstyle Eclipse plugin config files for the current project."

				eclipseCheckstyle.configure {
					configDir = ext.configDir
					checkstyleFileName = ext.checkstyleConfigFileName
				}

				// Ensure findbugs config files are copied if project applies Eclipse plugin
				project.plugins.withType(EclipsePlugin) {
					project.tasks.eclipse.dependsOn eclipseCheckstyle

					// ensure that checkstyle.xml is present
					eclipseCheckstyle.dependsOn generateCheckstyleConfigTask

					// Also configure checkstyle nature and buildCommand
					eclipse.project {
						natures 'net.sf.eclipsecs.core.CheckstyleNature'
						buildCommand 'net.sf.eclipsecs.core.CheckstyleBuilder'
					}
				}
			}



		}
	}

	private void checkstyleReport(project, buildDir, configDir, checkType) {
		def transformerFile = new File(configDir.absolutePath, "checkstyle/checkstyle.xsl")
		if(!transformerFile.exists()){
			project.logger.warn("Cannot generate checkstyle report HTML: XSL transformer file is missing (" + transformerFile + ")" )
			return
		}
		def ceckstyleOut = project.file("$buildDir/reports/checkstyle/${checkType}.xml")
		if (ceckstyleOut.exists()) {
			project.ant.xslt(
					in: "$buildDir/reports/checkstyle/${checkType}.xml",
					style: transformerFile,
					out:"$buildDir/reports/checkstyle/checkstyle_${checkType}.html"
					)
		}
	}

}
