# README #

Evolab project to view and post BitBucket pull request comments in IntelliJ.

### Requirements ###

   * IntelliJ Community installed.
   * A shallow clone of IntelliJ community.
   * A clone of this repo. 

### Contribution guidelines ###

### Post-installation Steps ###
1. We depend on the jars in the git4idea plugin. Unfortunately, when you create your Intellij SDK, it adds some jars to the classpath but not certain plugins.
    * Project Structure > SDKs > Your Intellij SDK > Classpath tab > [+] > Expand out your Intellij CE install directory
    * Expand plugins > git4idea\lib
    * Select all the jars in there and add them, click apply

### Who do I talk to? ###

* Owen

### Task List ###
1. Is this an open source product (Steve)
    * Does Biomatters have a GitHub account? 
1. Name it (Everyone)
    * Repo can be `{name} JetBrains Plugin`, but the name should not include `JetBrains`.
1. User stories √
    * Review's perspective √
    * Author's perspective √
1. Mock ups (via screenshots)
    * Project tree √
    * Comments pane
    * Side-by-side diff viewer
    * Other diff viewers?
    * Leave a comment
1. Try out Jet Brains' PR comment system
1. Technical plan & build
    * Review and improve wiki
    * Assign jobs for evolab
    * See [API documentation](https://confluence.atlassian.com/bitbucket/pullrequests-resource-1-0-296095210.html#pullrequestsResource1.0-POSTanewcomment)

### User stories ###

* As an author  
  I want to integrate PR feedback into my git branch without switching contexts  
  So that I can save time and sanity
* As a reviewer  
  I want to provide feedback on a PR without switching contexts  
  So that I can save time and sanity

### Mock Ups ###

[Available here.](https://drive.google.com/a/biomatters.com/folderview?id=0B2S5auGjszTANGtjLXhaaUZscjQ&usp=sharing)
