## Change Log

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

