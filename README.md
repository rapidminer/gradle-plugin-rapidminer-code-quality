## Introduction
The 'com.rapidminer.code-quality' plugin adds tasks for automatic code quality checks for Java/Groovy projects at RapidMiner. 
It is meant to be work out-of-the-box without any further configuration (at least in most of the cases).

The plugin depends on other plugins (e.g. the Eclipse plugin, the Java plugin, etc.). Therefore it should only be applied 
after after all other plugins have been applied.

## How to use (requires Gradle 2.1+)
	// Apply other plugins first before applying the code-quality plugin 
	apply plugin: 'java'
	
	plugins {
		id 'com.rapidminer.code-quality' version «plugin version»
	}
	 
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
		findbugsIgnoreErrors = false
		
		/**
		 * The path to the file containing FindBugs excludes. If the specified file is present
		 * it will be used. Otherwise the exclude filter will be ignored.
		 *
		 */
		findbugsExcludeFilter = 'config/findbugs/exclude.xml'
		
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
		 * Defines whether PMD errors should be ignored. Default is: <code>false</code>
		 */
		boolean pmdIgnoreErrors = false
	}
	
## Applied Plugins

### Java projects:
- CheckStyle (http://www.gradle.org/docs/current/userguide/checkstyle_plugin.html)
- FindBugs (http://www.gradle.org/docs/current/userguide/findbugs_plugin.html)
- JDepend (http://www.gradle.org/docs/current/userguide/jdepend_plugin.html)
- PMD (http://www.gradle.org/docs/current/userguide/pmd_plugin.html)
- JaCoCo (http://www.gradle.org/docs/current/userguide/jacoco_plugin.html)

### Groovy projects:
- CodeNarc (http://www.gradle.org/docs/current/userguide/codenarc_plugin.html, only for Groovy projects)
- JaCoCo (http://www.gradle.org/docs/current/userguide/jacoco_plugin.html)

### All projects:
- license (https://github.com/hierynomus/license-gradle-plugin)

## Added Tasks
##### eclipseFindBugs
The task will be added if FindBugs is activated and the Eclipse plugin is applied. It configures the FindBugs Eclipse plugin by copying files to the .settings/ project folder.

##### checkstyleInitDefaultConfig
Copies the default checkstyle config file to the directory specified by configDir. Will be executed before check tasks if checkstyleUseDefaultConfig is set to true.

##### codenarcInitDefaultConfig
Copies the default CodeNarc config file to the directory specified by configDir. Will be executed before check tasks if codenarcUseDefaultConfig is set to true.

## Eclipse Plugins
- FindBugs (http://findbugs.sourceforge.net/manual/eclipse.html)
- CheckStyle (http://eclipse-cs.sourceforge.net/)