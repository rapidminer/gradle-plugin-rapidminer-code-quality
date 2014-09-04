## Introduction
The 'rapidminer-code-quality' plugin is designed to add tasks for automatic code quality checks. 
The plugin ships with preconfigured configuration files which are used by default.

## How to use
	buildscript { 
		dependencies { 
			classpath 'com.rapidminer.gradle:code-quality:$VERSION'
		} 
	}
	 
	apply plugin: 'com.rapidminer.gradle.code-quality'
	 
	codeQuality {
	 
		/**
		 * The basic root project's configuration directory.
		 */
		configDir = "config/"

		// ############### License Header ##################
		
		/**
		 * Defines whether the header check tasks should be added.
		 * Can be overwritten by defining the project property 
		 * 'headerCheck' (e.g. 'gradle check -P headerCheck=false').
		 */
		headerCheck = true
	
		/**
		 * The path to the file that contains the license header. 
		 * The path should be relative to the root folder of the root project. 
		 * It will be used to check the file header if {@link #addHeaderChecks} is 
		 * set to <code>true</code>.
		 */
		headerFile = "config/HEADER"
		
		/**
		 * Defines whether to ignore header errors
		 */
		headerCheckIgnoreErrors = false

		/**
		 * If set to <code>true</code>, the headerFile parameter will point
		 * to the local project instead to the root project config file.
		 * This can be used to have different licenses in subprojects of 
		 * a multi project build environment.
		 */
		headerCheckUseRootConfig = true

		// ############### CodeNarc ##################

		/**
		 * Defines whether the CodeNarc plugin should be applied. Default: true.
		 * Can be overwritten by defining the project property 
		 * 'codenarc' (e.g. 'gradle check -P codenarc=false').
		 *
		 * CodeNarc will only be  applied for project that also apply the Groovy plugin.
		 */
		codenarc = true
		
		/**
		 * Defines whether to use the shipped Codenarc config file.
		 */
		codenarcUseDefaultConfig = true

		/**
		 * Defines the name of the Codenarc configuration file.
		 * It has to be placed in $configDir/codenarc/
		 */
		codenarcConfigFileName = "codenarc.conf"

		/**
		 * Defines whether a build should fail on Codenarc errors.
		 */
		codenarcIgnoreErrors = true

		// ############### Checkstyle ##################
		
		/**
		 * Defines if the checkstyle plugin should be applied.
		 * Can be overwritten by defining the project property 
		 * 'checkstyle' (e.g. 'gradle check -P checkstyle=false').
		 */
		checkstyle = true
		
		/**
		 * Defines whether to use the shipped checkstyle config file.
		 */
		checkstyleUseDefaultConfig = true

		/**
		 * Defines the name of the checkstyle configuration file.
		 * It has to be placed in $configDir/checkstyle/
		 */
		checkstyleConfigFileName = "checkstyle.xml"

		/**
		 * Defines whether a build should fail on checkstyle errors.
		 */
		checkstyleIgnoreErrors = true
		
		// ############### JDepend ##################

		/**
		 * Defines if the JDepend plugin should be applied. Default is: false.
		 * Can be overwritten by defining the project property 
		 * 'jdepend' (e.g. 'gradle check -P jdepend=true').
		 */
		jdepend = false
		
		/**
		 * Defines whether JDepend errors should be ignored. Default is: true
		 */
		jdependIgnoreErrors = true

		
		// ############### FindBugs ##################
		
		/**
		 * Defines if the FindBugs plugin should be applied. Default is: true.
		 * Can be overwritten by defining the project property 
		 * 'findbugs' (e.g. 'gradle check -P findbugs=false').
		 */
		findbugs = true
		
		/**
		 * Defines whether FindBugs errors should be ignored. Default is: true
		 */
		findbugsIgnoreErrors = true
		
		// ############### JaCoCo #################
	
		/**
		 * Defines if the JaCoCo plugin should be applied. Default is: false.
		 * Can be overwritten by defining the project property 
		 * 'jacoco' (e.g. 'gradle check -P jacoco=true').
		 */
		boolean jacoco = false
	
	
		// ############### PMD ################# 
		/**
		 * Defines if the PMD plugin should be applied. Default is: true.
		 * Can be overwritten by defining the project property
		 * 'pmd' (e.g. 'gradle check -P pmd=false').
		 */
		boolean pmd = true
		
		/**
		 * Defines whether PMD errors should be ignored. Default is: <code>true</code>
		 */
		boolean pmdIgnoreErrors = true
	}
	
## Applied Plugins

### Java projects:
- checkstyle (http://www.gradle.org/docs/current/userguide/checkstyle_plugin.html)
- findbugs (http://www.gradle.org/docs/current/userguide/findbugs_plugin.html)
- jdepend (http://www.gradle.org/docs/current/userguide/jdepend_plugin.html)
- pmd (http://www.gradle.org/docs/current/userguide/pmd_plugin.html)
- jacoco (http://www.gradle.org/docs/current/userguide/jacoco_plugin.html)

### Groovy projects:
- codenarc (http://www.gradle.org/docs/current/userguide/codenarc_plugin.html, only for Groovy projects)
- jacoco (http://www.gradle.org/docs/current/userguide/jacoco_plugin.html)

### All projects:
- license (https://github.com/hierynomus/license-gradle-plugin)

## Added Tasks
##### checkstyleInitDefaultConfig
Copies the default checkstyle config file to the directory specified by configDir. Will be executed before check tasks if checkstyleUseDefaultConfig is set to true.

##### codenarcInitDefaultConfig
Copies the default CodeNarc config file to the directory specified by configDir. Will be executed before check tasks if codenarcUseDefaultConfig is set to true.