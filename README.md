# README #

Evolab project to view and post BitBucket pull request comments in IntelliJ.

### Licensing ###
All files in this project are under the [GPL License](http://www.gnu.org/licenses/gpl-howto.html). We have yet to implement the header comments to each file.

### Requirements ###

   * An IDE of your preference.
   * [Gradle binary only distribution](http://gradle.org/gradle-download/)

### Setup ###
1. Shallow clone of the intellij IDEA Community Edition source code: `git clone --depth 1 https://github.com/JetBrains/intellij-community.git`
(~15mins or so)
1. Download Intellij Community: `https://www.jetbrains.com/idea/download/`. You can still use ultimate to develop, but ultimate is closed source and makes debugging difficult.
1. Setup your IDE by following the relevant instructions here: `http://bjorn.tipling.com/how-to-make-an-intellij-idea-plugin-in-30-minutes`
1. Get Gradle dependencies, run `gradle compileJava`. This will fail, but after getting the dependencies (which is all we need). TODO Fix this so doesn't fail.
1. Create a plugin run config. Go to `Run > Edit Configurations` then in the tree, click the `+` button and choose type `Plugin`. Click apply, ok.

Hacky things that you have to do until gradle is nicely integrated. TODO Fix this. 

### Post-installation Steps ###
1. We depend on the jars in the git4idea plugin. Unfortunately, when you create your Intellij SDK, it adds some jars to the classpath but not certain plugins.
    * Project Structure > SDKs > Your Intellij SDK > Classpath tab > [+] > Expand out your Intellij CE install directory
    * Expand plugins > git4idea\lib
    * Select all the jars in there and add them, click apply

### Package M ###

1. Maybe run `gradle compileJava`. This will fail but it will get the dependencies. You can run this task from the gradle sidebar > other > compileJava.
1. Navigate to project structure / modules, changed `Use module compile...` to `Inherit compiler...`.
