name := "collisions"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.0.0"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.0.0"

libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.0.0"

libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.0.0"

libraryDependencies += "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.0-M3"

libraryDependencies += "com.databricks" %% "spark-csv" % "1.5.0"

libraryDependencies += "joda-time" % "joda-time" % "2.3"