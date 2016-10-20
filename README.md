# collisions

This application demonstrates:
- data ingestion into Cassandra
- reporting on Cassandra's data
- streaming data processing from Kafka

All these tasks are implemented using Spark/Scala.

The data to process is the collisions history provided by NYPD. 

The app consists of 3 modules: etl, analytics and streaming.

##ETL
Input data is a csv format file taken from https://data.cityofnewyork.us/Public-Safety/NYPD-Motor-Vehicle-Collisions/h9gi-nx95#column-menu (Export->Download as CSV) located in **data_sample** folder.

Scripts to create Cassandra's objects located in **cassandra/cql**, *cassandra.yaml* configuration is in **cassandra/conf**.

App project is in **app** folder, built tool is SBT.

Class for ETL job is collisions.Load, Cassandra db settings (node, port, keyspace, table etc) are in *CollisionsConfig.scala*

To launch spark ETL job run:

`spark-submit --class collisions.Load <collisions.jar> <src-data-dir>`

where `<src-data-dir>` is a csv file location, job is submitted for local master

##Analytics

All the reports consider the Cassandra table as input data, the report logic is implemented using Spark SQL via HiveQL queries.

The reports output is a csv file.

Class for Analytics job is collisions.Report, Cassandra db settings (node, port, keyspace, table etc) are in *CollisionsConfig.scala*

To launch spark Report job run:

`spark-submit --class collisions.Report <collisions.jar> <report-type> <tgt-data-dir>`

where `<report-type>` is one of the: **monthly**, **season**, **day-night**. `<tgt-data-dir>` is an output csv file location, job is submitted for local master.

##Streaming

Kafka topic is used here. Kafka configurations located in **kafka/config**: *connect-standalone-collisions.properties*, *connect-file-source-collisions.properties*, *connect-file-sink-collisions.properties*.

Additional windows bat file *connect-standalone.bat* is in **kafka/bin/windows**

To start zookeeper run: 

`bin\windows\zookeeper-server-start.bat config\zookeeper.properties`

then start Kafka server: 

`bin\windows\kafka-server-start.bat config\server.properties`

start Kafka Connect to send the messages from file to topic:

`bin\windows\connect-standalone.bat config\connect-standalone-collisions.properties config\connect-file-source-collisions.properties`

here the file to read the messages from is **kafka/test.txt**, it must be located in Kafka root direcotry.

Streamed data after format checks is saved in local folder as serialized case class record.

Class for Streaming job is collisions.Stream, Kafka settings (server, port, topic name etc) are in **CollisionsConfig.scala**

To launch spark Stream job run:

`spark-submit --class collisions.Stream <collisions.jar> <tgt-data-dir>`

where `<tgt-data-dir>` is an output files location, job is submitted for local master.

##Versions and dependencies

- Apache Spark 2.0.0

- Kafka 2.11-0.10.0.1

- Cassandra 3.8.0

- Scala 2.11.8

Put these jars into Spark **jars** folder:

- `org.apache.spark:spark-streaming-kafka-0-10-assembly_2.11-2.0.0.jar`

- `com.datastax.spark:spark-cassandra-connector_2.11-2.0.0-M3.jar`

- `com.twitter:jsr166e-1.1.0.jar`
