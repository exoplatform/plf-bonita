
1. About this document
---------------------------------------------------

This user manual is targeted at end users who wants to use platform for bonita (plf 3.5.X and BOS-Community 5.7.2) .
 
This manual will explain all the basic steps to build and start this project.


2.Environnement
---------------------------------------------------
Project VErsion:3.5.3
BOs: Community-5.7.2
PLF: 3.5.5-SNAPSHOT
Java SE 1.6 
Maven 3


3. HOW TO SET UP 
---------------------------------------------------


3.1 Clone the SOurce with GIt  from : git@github.com:exoplatform/plf-bonita.git

3.2 Do a checkout of "remotes/origin/master" 's branch

3.3 Open a Terminal window  and go to the  directory that has just done the clone.

3.4 BUILDING FROM SOURCES : run the command : mvn clean install -Dmaven.test.skip -Pexo-private,exo-staging,pkg-bonita
		
3.5 Go to the tomcat directory "/packaging/bonita/target/tomcat-bundle"

3.6 Starting Deployement server : execute "start_eXo.bat"
		

4. RUNNING
---------------------------------------------------
 
4.1 Open your web browsers, Navigate to URL: http://localhost:8080/portal

