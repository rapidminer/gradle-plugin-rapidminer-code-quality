package com.rapidminer.gradle

import java.io.File;
import org.codehaus.groovy.transform.tailrec.UsedVariableTracker;
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskState
import com.rapidminer.gradle.InitCheckstyleFiles


/**
 *
 * @author Nils Woehler
 *
 */
class RapidMinerCodeQualityPlugin implements Plugin<Project> {

	private static final String TASK_GROUP = "RapidMiner Code Quality"

	@Override
	void apply(Project project) {

		// create 'codeQuality' project extension
		project.extensions.create("codeQuality", CodeQualityConfiguration)

		project.configure(project) {
			apply plugin: 'checkstyle'

			// ensure checkstyle config files are found
			tasks.create(name: 'checkstyleInitDefault', type: InitCheckstyleFiles)
			checkstyleInitDefault.group = TASK_GROUP
			checkstyleInitDefault.description = "Copies the default checkstyle.xml files to the configured configuration directory."

			afterEvaluate {
				def configurationDir = rootProject.file(codeQuality.configDir)
				def configurationFile = new File(configurationDir.absolutePath, codeQuality.configFileName)
				def headerFile = new File(configurationDir.absolutePath, codeQuality.javaHeaderFileName)

				// Ensure that config files will be copied if default config should be used
				if(codeQuality.useDefaultConfig) {
					check.dependsOn checkstyleInitDefault

					checkstyleInitDefault {
						configDir = configurationDir
						checkstyleFile = configurationFile
					}
				}

				project.checkstyle.configProperties = [ "headerFile" :  headerFile ]

				checkstyleMain {
					if(codeQuality.ignoreErrors) {
						ignoreFailures = true
					}
					reports {
						include ( '**/*.java')
						xml { destination "${project.buildDir}/reports/checkstyle/main.xml" }
					}
					configFile configurationFile
				}

				checkstyleTest {
					if(codeQuality.ignoreErrors) {
						ignoreFailures = true
					}
					reports {
						include ( '**/*.java')
						xml { destination "${project.buildDir}/reports/checkstyle/test.xml" }
					}
					configFile configurationFile
				}

				gradle.taskGraph.afterTask {Task task, TaskState state ->
					if(state.failure) {
						if (task.name in [
							'checkstyleMain',
							'checkstyleTest'
						]) {
							def matcher = task.name =~ /^checkstyle(.*)$/
							if (matcher.matches()) {
								checkstyleReport(project, project.buildDir, configurationDir, matcher.group(1).toLowerCase())
							}
						}
					}
				}
			}

		}

	}

	void checkstyleReport(project, buildDir, configDir, checkType) {
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
