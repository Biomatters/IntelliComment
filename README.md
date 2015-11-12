# README #

Evolab project to view and post BitBucket pull request comments in IntelliJ.

### Requirements ###

   * An IDE of your preference.
   * [Gradle binary only distribution](http://gradle.org/gradle-download/)

### Setup ###
1. Shallow clone of the intellij IDEA Community Edition source code: `git clone --depth 1 https://github.com/JetBrains/intellij-community.git`
(~15mins or so)
1. Download Intellij Community: `https://www.jetbrains.com/idea/download/`. You can still use ultimate to develop, but ultimate is closed source and makes debugging difficult.
1. Get Gradle dependencies, run `gradle compileJava`. This will fail, but after getting the dependencies (which is all we need). TODO Fix this so doesn't fail.
1. Create a plugin run config. Go to `Run > Edit Configurations` then in the tree, click the `+` button and choose type `Plugin`. Click apply, ok. 