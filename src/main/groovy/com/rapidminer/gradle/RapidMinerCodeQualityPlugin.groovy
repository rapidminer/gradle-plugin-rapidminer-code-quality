package com.rapidminer.gradle

import org.codehaus.groovy.transform.tailrec.UsedVariableTracker
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskState

import com.rapidminer.gradle.checkstyle.InitCheckstyleConfigFiles
import com.rapidminer.gradle.codenarc.InitCodenarcConfigFiles


/**
 *
 * @author Nils Woehler
 *
 */
class RapidMinerCodeQualityPlugin implements Plugin<Project> {

	private static final String TASK_GROUP = 'RapidMiner Code Quality'
	private static final String ALL_JAVA = '**/*.java'

	private static final String CHECKSTYLE = 'checkstyle'
	private static final String INIT_CHECKSTYLE_CONFIG_TASK = 'checkstyleInitDefaultConfig'
	private static final String CHECKSTYLE_GEN_REPORT = 'HTMLReport'

	private static final String CODENARC = 'codenarc'
	private static final String INIT_CODENARC_CONFIG_TASK = 'codenarcInitDefaultConfig'

	private static final String HEADER_CHECK = 'headerCheck'
	private static final String JDEPEND = 'jdepend'
	private static final String FINDBUGS = 'findbugs'

	@Override
	void apply(Project project) {

		// create 'codeQuality' project extension
		CodeQualityConfiguration qualityExt = project.extensions.create("codeQuality", CodeQualityConfiguration)


		project.afterEvaluate {
			def configurationDir = project.rootProject.file(qualityExt.configDir)

			if(applyPlugin(project, CHECKSTYLE,  qualityExt.checkstyle)) {
				// add checkstyle plugin tasks
				configureCheckstyle(project, qualityExt, configurationDir)
			}

			if(applyPlugin(project, CODENARC, qualityExt.codenarc)) {
				// add codenarc plugin tasks of project is a groovy project
				configureCodeNarc(project, qualityExt, configurationDir)
			}

			// add header check tasks
			if(applyPlugin(project, HEADER_CHECK, qualityExt.headerCheck)) {
				configureHeaderCheck(project, qualityExt, configurationDir)
			}

			// add JDepend check tasks
			if(applyPlugin(project, JDEPEND, qualityExt.jdepend)) {
				configureJDepend(project, qualityExt, configurationDir)
			}

			// add FindBugs check tasks
			if(applyPlugin(project, FINDBUGS, qualityExt.findbugs)) {
				configureFindBugs(project, qualityExt, configurationDir)
			}
		}
	}

	private boolean applyPlugin(Project project, String propertyKey, Boolean extensionValue){
		if(project.hasProperty(propertyKey)) {
			return Boolean.valueOf(project.getProperty(propertyKey))
		} else {
			return extensionValue
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

				effort = 'max'
				reportLevel = 'high'
			}
		}
	}

	private void configureHeaderCheck(Project project, CodeQualityConfiguration codeExt, File configurationDir) {
		project.configure(project) {
			apply plugin: 'license'

			license.ext.year = Calendar.getInstance().get(Calendar.YEAR)
			license {
				if(codeExt.headerCheckUseRootConfig) {
					header rootProject.file(codeExt.headerFile)
				} else {
					header project.file(codeExt.headerFile)
				}
				ignoreFailures codeExt.headerCheckIgnoreErrors
				includes([ALL_JAVA, '**/*.groovy',])
			}
		}
	}


	private void configureCodeNarc(Project project, CodeQualityConfiguration ext, File configurationDir) {
		if(!project.plugins.withType(org.gradle.api.plugins.GroovyPlugin)) {
			project.logger.info("${project.name} is no Groovy project. Skipping application of Codenarc plugin.")
			return
		}
		project.configure(project) {
			apply plugin: CODENARC

			// ensure codenarc config file is in place
			tasks.create(name: INIT_CODENARC_CONFIG_TASK, type: InitCodenarcConfigFiles)
			codenarcInitDefaultConfig.group = TASK_GROUP
			codenarcInitDefaultConfig.description = "Copies the default codenarc.conf files to " +
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

			def checkstyleConfigDir = new File(configurationDir.absolutePath, CHECKSTYLE)
			def checkstyleConfigFile = new File(checkstyleConfigDir.absolutePath, ext.checkstyleConfigFileName)

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
			tasks.create(name: INIT_CHECKSTYLE_CONFIG_TASK, type: InitCheckstyleConfigFiles)
			checkstyleInitDefaultConfig.group = TASK_GROUP
			checkstyleInitDefaultConfig.description = "Copies the default checkstyle.xml files to " +
					"the configured configuration directory."

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
