name := "rankservice"

organization := "acleague"

version := "0.1-SNAPSHOT"

scalaVersion := "2.10.4"

resolvers ++= Seq (
  "neo4j-releases" at "http://m2.neo4j.org/releases",
  "neo4j-snapshots" at "http://m2.neo4j.org/snapshots"
)

libraryDependencies ++= Seq(
  "org.neo4j" % "neo4j" % "2.1.6",
  "org.neo4j" % "neo4j-rest-graphdb" % "2.0.1",
  "org.neo4j" % "neo4j-kernel" % "2.1.6" classifier "tests",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)

libraryDependencies += "com.sun.jersey" % "jersey-core" % "1.18.3"

libraryDependencies += "com.sun.jersey" % "jersey-client" % "1.18.3"
