def integrationTestFilter(name: String): Boolean = name endsWith "IntegrationTest"
def unitTestFilter(name: String): Boolean = !integrationTestFilter(name)

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(inConfig(IntegrationTest)(Defaults.testTasks) : _*)
  .settings(
    Defaults.itSettings,
    testOptions in Test := Seq(
      Tests.Filter(unitTestFilter)
    ),
    testOptions in IntegrationTest := Seq(
      Tests.Filter(integrationTestFilter)
    ),
    name := "spark_ci",
    version := "1.0",
    dependencyClasspath in IntegrationTest := (dependencyClasspath in IntegrationTest).value
      ++ (exportedProducts in Test).value
  )

scalaVersion := "2.11.8"

val sparkVersion = "2.3.0"

resolvers += "Cloudera Repo" at "https://repository.cloudera.com/artifactory/cloudera-repos/"

javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled")
javacOptions ++= Seq(
  "-source", "1.8", "-target", "1.8",
  "-Xlint")

initialize := {
  val _ = initialize.value
  if (sys.props("java.specification.version") != "1.8")
    sys.error("Java 8 is required for this project.")
}

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-hive" % sparkVersion % "provided",
  "org.apache.kudu" % "kudu-spark2_2.11" % "1.7.0-cdh5.16.1",
  "org.scalactic" %% "scalactic" % "3.0.1" % "test,it",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test,it",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.3.0" % "test,it",
  "com.novocode" % "junit-interface" % "0.9" % "test,it",
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.8.1",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.1"
)

//Add src/it as test source directory (src/test is default)
unmanagedSourceDirectories in Test := baseDirectory { base =>
  Seq(
    base / "src/it/scala",
    base / "src/test/scala"
  )
}.value

parallelExecution in IntegrationTest := true
parallelExecution in Test := true

//Remove temp directory through "sbt clean"
//Useful because otherwise the temp directory ends up with thousands of directories
//Which can slow performance
cleanFiles += baseDirectory.value / "temp"