pipeline {
    stages {
        stage('Build') {
            steps {
                // Testing the commit hook
                sbt command: "clean compile assembly"
            }
        }

        stage('Test') {
            environment {
                HADOOP_HOME = "C:\\HadoopResources\\Hadoop\\"
            }
            steps {
                //Create hadoop winutils folder structure
                powershell "md -Force ${env.HADOOP_HOME}bin"
                //Download winutils.exe file to Hadoop bin
                powershell "Invoke-WebRequest http://public-repo-1.hortonworks.com/hdp-win-alpha/winutils.exe -o ${env.HADOOP_HOME}bin\\winutils.exe"
                sbt command: "test"
                sbt command: "it:test"
            }
        }
        //Deploy however you like
    }
}