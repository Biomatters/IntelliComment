# README #

IntelliComment was an Evolab project (a Biomatters hackathon) project to view and post BitBucket pull request comments from IntelliJ when viewing your files. We hope to add support for Github in the future.

## Requirements ##

   * An IDE of your preference.
   * [Gradle binary only distribution](http://gradle.org/gradle-download/)

## Installation ##
1. Shallow clone of the intellij IDEA Community Edition source code: `git clone --depth 1 https://github.com/JetBrains/intellij-community.git`
(~15mins or so)
1. Download Intellij Community: `https://www.jetbrains.com/idea/download/`. You can still use ultimate to develop, but ultimate is closed source and makes debugging difficult.
1. Setup your IDE by following the relevant instructions here: `http://bjorn.tipling.com/how-to-make-an-intellij-idea-plugin-in-30-minutes`
1. Get Gradle dependencies, run `gradle compileJava`. This will fail, but after getting the dependencies (which is all we need). TODO Fix this so doesn't fail.
1. Create a plugin run config. Go to `Run > Edit Configurations` then in the tree, click the `+` button and choose type `Plugin`. Click apply, ok.
1. We depend on the jars in the git4idea plugin. Unfortunately, when you create your Intellij SDK, it adds some jars to the classpath but not certain plugins.
    * Project Structure > SDKs > Your Intellij SDK > Classpath tab > [+] > Expand out your Intellij CE install directory
    * Expand plugins > git4idea\lib
    * Select all the jars in there and add them, click apply
1. Maybe run `gradle compileJava`. This will fail but it will get the dependencies. You can run this task from the gradle sidebar > other > compileJava.
1. Navigate to project structure / modules, changed `Use module compile...` to `Inherit compiler...`.

## Development ##
* We follow the [Gitflow branching strategy|https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow]. 

## Contributing
* Log issues in [Github issue tracker|https://github.com/Biomatters/IntelliComment/issues].
* Pull requests are welcome and encouraged, we will try to review them in a timely manner.


