## Change Log

#### 0.4.5

#### 0.4.4
* Fixes plugin portal publishing issue

#### 0.4.3
* Bumps version of 'license-gradle-plugin' to version 0.14.0
* Adds new third party license folder check task. It can be activated to be run before the tests by specifying 
```$xslt
codeQuality {
    checkThirdPartyLicensesBeforeTest = true
}
```

#### 0.4.2
* Changes 'license-gradle-plugin' back to version 0.11.1 to fix broken license header checks

#### 0.4.1
* Updates findbugs version to 3.0.1
* Updates checkstyle version to 6.16.1
* Updates PMD version to 5.4.1
* Adapts default checkstyle configuration file rules to be more liberal including
** do not check for TODO comments anymore
** only check public methods for JavaDoc, not constructors
** do not check for author tag anymore
* Removes the test sourceSet from the checkstyle checks. Now only the main sourceSet will be checked.

#### 0.4.0
* Adds compile configuration to Checkstyle task classpath
* Updates 'license-gradle-plugin' to version 0.12.1
* Disables PMD as default code style check

#### 0.3.7
* Use UTF-8 encoding when updating license headers with licenseFormat task

#### 0.3.6
* Decreases Findbugs effort to "default" instead of "max"

#### 0.3.5
* Adds shortened plugin name 'com.rapidminer.code-quality' to comply with plugins.gradle.org standards

#### 0.3.4
* Downgrade accidentally set PMD version from 5.2 back to version 5.1.3

#### 0.3.3
* Sets checkstyle tool version to 5.9 to enable Java 8 code checking
* Sets FindBugs tool version to 3.0.0 to enable Java 8 code checking
* Sets PMD tool version to 5.1.3 to enable Java 8 code checking
* PMD errors will now fail the build by default

#### 0.3.2
* Also add findbugs annotation library as providedCompile dependency if provided configuration does not exist

#### 0.3.1 
* Disables PMD Eclipse plugin on full builds
* Disables FindBugs Eclipse plugin on full builds

#### 0.3.0
* Jacoco is now disabled by default (introduced too many build failures)
* Improved rule sets used by checkstyle
* Checkstyle errors will now fail the build by default
* FindBugs is now enabled by default
* FindBugs errors will now fail the build by default

#### 0.2.3
* Updated license plugin to vesion 0.11.0
* Added Gradle 2.1 compatible plugin name 'com.rapidminer.gradle.code-quality' 
* Adds JaCoCo plugin for Java and Groovy projects
* Adds optional PMD plugin for Java projects
* Checkstyle plugin won't be applied to Groovy projects anymore
* FindBugs plugin won't be applied to Groovy projects anymore
* JDepend plugin won't be applied to Groovy projects anymore

#### 0.2.2
* Adds headerCheckUseRootConfig extension property
* Adds tasks for transforming checkstyle XML reports to HTML reports

#### 0.2.1
* By default JDepend and FindBugs will be disabled
* Introduced project properties headerCheck, checkstyle, codenarc, jdepend, and findbugs

#### 0.2.0
* Adds JDepend for Java and Groovy projects
* Adds FindBugs for Java and Groovy projects
* Improve license header checks
* Adds CodeNarc for Groovy projects

#### 0.1.1
* Adds prependJavaHeader task for each source set

#### 0.1.0 
* Extension release

