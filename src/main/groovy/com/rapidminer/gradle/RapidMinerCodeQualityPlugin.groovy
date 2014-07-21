package com.rapidminer.gradle

import org.codehaus.groovy.transform.tailrec.UsedVariableTracker
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskState

import com.rapidminer.gradle.checkstyle.InitCheckstyleConfigFiles;


/**
 *
 * @author Nils Woehler
 *
 */
class RapidMinerCodeQualityPlugin implements Plugin<Project> {

	private static final String TASK_GROUP = 'RapidMiner Code Quality'
	private static final String CHECKSTYLE = 'checkstyle'
	private static final String ALL_JAVA = '**/*.java'
	private static final String INIT_CHECKSTYLE_CONFIG_TASK = 'checkstyleInitDefaultConfig'

	@Override
	void apply(Project project) {

		// create 'codeQuality' project extension
		CodeQualityConfiguration ext = project.extensions.create("codeQuality", CodeQualityConfiguration)

		def configurationDir = project.rootProject.file(ext.configDir)
		def headerFile = new File(configurationDir.absolutePath, ext.headerFileName)

		// add checkstyle plugin tasks
		configureCheckstyle(project, ext, configurationDir)

//		// Create prepend header tasks
//		def prependTasks = []
//		project.sourceSets.each  { set ->
//			PrependHeaderTask t = project.tasks.create(name: 'prependHeaderJava' + set.name.capitalize(), type: PrependHeaderTask)
//			t.sourceSet = set
//			t.headerFile = headerFile
//			prependTasks << t
//		}
//		tasks.create(name: 'prependHeaderJavaAll', dependsOn: prependTasks)

	}

	private void configureCheckstyle(Project project, CodeQualityConfiguration ext, File configurationDir) {
		project.configure(project) {
			apply plugin: CHECKSTYLE

			// ensure checkstyle config files are in place
			tasks.create(name: INIT_CHECKSTYLE_CONFIG_TASK, type: InitCheckstyleConfigFiles)
			checkstyleInitDefaultConfig.group = TASK_GROUP
			checkstyleInitDefaultConfig.description = "Copies the default checkstyle.xml files to " +
														"the configured configuration directory."

			def checkstyleConfigDir = new File(configurationDir.absolutePath, CHECKSTYLE)

			// Configure checkstyle tasks
			if(ext.checkstyleUseDefaultConfig) {
				project.tasks.each { t ->
					if(t.name != INIT_CHECKSTYLE_CONFIG_TASK && t.name.startsWith(CHECKSTYLE)) {
						t.dependsOn checkstyleInitDefaultConfig
					}
				}
				checkstyleInitDefaultConfig {
					configDir = checkstyleConfigDir
					checkstyleFileName = ext.checkstyleConfigFileName
				}
			}
			
			def checkstyleConfigFile = new File(checkstyleConfigDir.absolutePath, ext.checkstyleConfigFileName)

			checkstyleMain {
				if(ext.checkstyleIgnoreErrors) {
					ignoreFailures = true
				}
				reports {
					include (ALL_JAVA)
					xml { destination "${project.buildDir}/reports/checkstyle/main.xml" }
				}
				configFile checkstyleConfigFile
			}

			checkstyleTest {
				if(ext.checkstyleIgnoreErrors) {
					ignoreFailures = true
				}
				reports {
					include (ALL_JAVA)
					xml { destination "${project.buildDir}/reports/checkstyle/test.xml" }
				}
				configFile checkstyleConfigFile
			}

			gradle.taskGraph.afterTask { Task task, TaskState state ->
				if(state.failure && task.name.startsWith(CHECKSTYLE)) {
					def matcher = task.name =~ /^checkstyle(.*)$/
					if (matcher.matches()) {
						checkstyleReport(project, project.buildDir, configurationDir, matcher.group(1).toLowerCase())
					}
				}
			}

		}


	}

	private void checkstyleReport(project, buildDir, configDir, checkType) {
		def transformerFile = new File(configDir.absolutePath, "checkstyle.xsl")
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
