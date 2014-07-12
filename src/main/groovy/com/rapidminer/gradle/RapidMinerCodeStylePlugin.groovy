package com.rapidminer.gradle

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

	private static final String EXTENSION_GROUP = "RapidMiner Extension"

	@Override
	void apply(Project project) {

		// create 'extension' project extension
		//		project.extensions.create("extensionConfig", ExtensionConfiguration)

		project.configure(project) {
			apply plugin: 'checkstyle'

			// ensure checkstyle config files are found
			tasks.create(name: 'checkstyleInitConfig', type: InitCheckstyleFiles)
			checkstyleInitConfig.group = EXTENSION_GROUP

			check.dependsOn checkstyleInitConfig

			project.checkstyle.configProperties = [ "headerDir" : project.file("license/") ]

			checkstyleMain {
				reports {
					include ( '**/*.java')
					xml { destination "${project.buildDir}/reports/checkstyle/main.xml" }
				}
				configFile = file("$buildDir/checkstyle/checkstyle.xml")
			}

			checkstyleTest {
				reports {
					include ( '**/*.java')
					xml { destination "${project.buildDir}/reports/checkstyle/test.xml" }
				}
				configFile = file("$buildDir/checkstyle/checkstyle.xml")
			}

			gradle.taskGraph.afterTask {Task task, TaskState state ->
				if(state.failure) {
					if (task.name in [
						'checkstyleMain',
						'checkstyleTest'
					]) {
						def matcher = task.name =~ /^checkstyle(.*)$/
						if (matcher.matches()) {
							checkstyleReport(project, project.buildDir, matcher.group(1).toLowerCase())
						}
					}
				}
			}
		}

	}
	
	void checkstyleReport(project, buildDir, checkType) {
		def ceckstyleOut = project.file("$buildDir/reports/checkstyle/${checkType}.xml")
		if (ceckstyleOut.exists()) {
			project.ant.xslt(
				in: "$buildDir/reports/checkstyle/${checkType}.xml",
				style:"target/checkstyle/checkstyle.xsl",
				out:"$buildDir/reports/checkstyle/checkstyle_${checkType}.html"
			)
		}
	}

}
