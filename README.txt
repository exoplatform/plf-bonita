=========================
    eXo Platform for Bonita
     25 August 2011
=========================


* Introduction
--------------

Welcome to eXo Platform for Bonita.



* Package Content
-----------------

This release contains:
 - Platform 3.0.6
 - Bonita 5.5.1



* Build and Packaging Instructions
----------------------------------

 - Prerequisites : 
    Java 6.
	Make sure your maven settings are up-to-date.
	Bonita Licenses.

 - Check-out the source from  :
   https://svn.exoplatform.org/projects/plfbonita/
   
 - Go into the main folder, run
   mvn clean install -Pexo-private
   
 - Go into the folder /packaging/tomcat/pkg/target/tomcat
   Here is your eXo Platform for Bonita Tomcat distribution

 - Go into the folder /packaging/tomcat/pkg/target/tomcat/bonita/server/licenses/
   Here you have to drop your Bonita Licenses

 - To build also documentations and archives used for distribution
   mvn clean install -Pexo-private,distrib 

* Known Issues
--------------

 
* Other Resources
-----------------

 - Internal Wiki  :  https://wiki-int.exoplatform.org/display/PLF/
 - Jira           :  https://jira.exoplatform.org/browse/PLFBONITA
 - SVN            :  https://svn.exoplatform.org/projects/plfbonita/
 - Fisheye        :  https://fisheye.exoplatform.org/browse/plfbonita
 - Crucible 	  :	 https://fisheye.exoplatform.org/project/CR-PLFBONITA