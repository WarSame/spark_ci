# Running automated Spark tests in Windows Jenkins pipeline

This repo contains example code showing you how to run automated Spark tests.
It also contains demonstrations of running these automated tests in your Jenkins pipeline while using a Windows agent
Further, a number of the structural patterns and helper files here can be used to get your testing setup working quickly and robustly.

###Spark on Windows
Spark on Windows requires a `winutils.exe` file in order to perform disk interactions.
This file must be located at `{HADOOP_HOME}\bin\winutils.exe`. 
In the Jenkinsfile you can see that the `HADOOP_HOME` value for the `Test` stage has been set to `C:\HadoopResources\Hadoop\`.
You can use the same value for your `HADOOP_HOME`, making sure to NOT add the `\bin\winutils.exe` to the `HADOOP_HOME`.
