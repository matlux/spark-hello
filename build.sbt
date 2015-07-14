name := "Simple Project"

version := "1.0"

scalaVersion := "2.10.4"

resolvers += "Hadoop Releases" at "https://repository.cloudera.com/content/repositories/releases/"


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.3.0" % "provided",
  "org.apache.hbase" % "hbase-client" % "1.0.0" % "provided",
  "org.apache.hbase" % "hbase-common" % "1.0.0" % "provided",
  "org.apache.hadoop" % "hadoop-common" % "2.2.0" % "provided")
  /*.map(
    _ excludeAll ( new ExclusionRule("org.slf4j"), new ExclusionRule("log4j") )
  )*/

assemblyJarName in assembly := "simple-project-shaded.jar"
