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
package com.rapidminer.gradle;

/**
 * Configuration container for Code Quality plugin.
 *
 * @author Nils Woehler
 *
 */
public class CodeQualityConfiguration {

	/**
	 * The basic root project's configuration directory.
	 */
	String configDir = "config/"

	// ############### Java Header ##################

	/**
	 * Defines whether the header check tasks should be added.
	 */
	boolean headerCheck = true

	/**
	 * The path to the file that contains the license header.
	 * The path should be relative to the root folder of the root project.
	 * It will be used to check the file header if {@link #addHeaderChecks} is
	 * set to <code>true</code>.
	 */
	String headerFile = "config/HEADER"

	/**
	 * Defines whether to ignore header errors
	 */
	boolean headerCheckIgnoreErrors = false

	/**
	 * If set to <code>true</code>, the headerFile parameter will point
	 * to the local project instead to the root project config file.
	 * This can be used to have different licenses in subprojects of
	 * a multi project build environment.
	 */
	boolean headerCheckUseRootConfig = true

	// ############### Codenarc ##################

	/**
	 * Defines whether the CodeNarc plugin should be applied. Default: true.
	 * Can be overwritten by defining the project property
	 * 'codenarc' (e.g. 'gradle check -P codenarc=false').
	 *
	 * CodeNarc will only be  applied for project that also apply the Groovy plugin.
	 */
	boolean codenarc = true

	/**
	 * Defines whether to use the shipped Codenarc config file.
	 */
	boolean codenarcUseDefaultConfig = true

	/**
	 * Defines the name of the Codenarc configuration file.
	 * It has to be placed in $configDir/codenarc/
	 */
	String codenarcConfigFileName = "codenarc.conf"

	/**
	 * Defines whether a build should fail on Codenarc errors.
	 */
	boolean codenarcIgnoreErrors = true

	// ############### Checkstyle ##################

	/**
	 * Defines if the checkstyle plugin should be applied.
	 * Can be overwritten by defining the project property
	 * 'checkstyle' (e.g. 'gradle check -P checkstyle=false').
	 */
	boolean checkstyle = true

	/**
	 * Defines whether to use the shipped checkstyle config file.
	 */
	boolean checkstyleUseDefaultConfig = true

	/**
	 * Defines the name of the checkstyle configuration file.
	 * It has to be placed in $configDir/checkstyle/
	 */
	String checkstyleConfigFileName = "checkstyle.xml"

	/**
	 * Defines whether a build ignore checkstyle errors.
	 * Default is: <code>false</code>
	 */
	boolean checkstyleIgnoreErrors = false

	// ############### JDepend ##################

	/**
	 * Defines if the JDepend plugin should be applied. Default is: <code>false</code>.
	 * Can be overwritten by defining the project property
	 * 'jdepend' (e.g. 'gradle check -P jdepend=true').
	 */
	boolean jdepend = false

	/**
	 * Defines whether JDepend errors should be ignored. Default is: <code>true</code>
	 */
	boolean jdependIgnoreErrors = true


	// ############### FindBugs ##################

	/**
	 * Defines if the FindBugs plugin should be applied. Default is: <code>true</code>.
	 * Can be overwritten by defining the project property
	 * 'findbugs' (e.g. 'gradle check -P findbugs=false').
	 */
	boolean findbugs = true

	/**
	 * Defines whether FindBugs errors should be ignored. Default is: <code>false</code>
	 */
	boolean findbugsIgnoreErrors = false

	/**
	 * The path to the file containing FindBugs excludes. If the specified file is present
	 * it will be used. Otherwise the exclude filter will be ignored.
	 *
	 */
	String findbugsExcludeFilter = 'config/findbugs/exclude.xml'

	// ############### JaCoCo #################

	/**
	 * Defines if the JaCoCo plugin should be applied. Default is: false.
	 * Can be overwritten by defining the project property
	 * 'jacoco' (e.g. 'gradle check -Pjacoco=true').
	 */
	boolean jacoco = false


	// ############### PMD #################
	/**
	 * Defines if the PMD plugin should be applied. Default is: false.
	 * Can be overwritten by defining the project property
	 * 'pmd' (e.g. 'gradle check -P pmd=true').
	 */
	boolean pmd = false

	/**
	 * Defines whether PMD errors should be ignored. Default is: <code>false</code>
	 */
	boolean pmdIgnoreErrors = false
}
