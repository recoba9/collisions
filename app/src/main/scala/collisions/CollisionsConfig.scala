package collisions

/**
  * Created by Rostislav on 15.10.2016.
  */

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}

trait CollisionsConfig {

  val conf = new SparkConf()
    .setMaster(Configuration.SparkMaster)
    .setAppName(Configuration.AppName)

  lazy val sparkSession = SparkSession
    .builder()
    .config(conf)
    .config(Configuration.SparkWarehouse, Configuration.WarehouseLocation)
    .config(Configuration.CassandraConnection, Configuration.CassandraNode)
    .config(Configuration.CassandraConnectionPort, Configuration.CassandraPort)
    .getOrCreate()

  lazy val streamingContext = new StreamingContext(conf, Seconds(10))

  object Configuration {
    val AppName = "CollisionsApp"
    val SparkMaster = "local"
    val SparkWarehouse = "spark.sql.warehouse.dir"
    val WarehouseLocation = "D:\\spark-2.0.0-bin-hadoop2.7\\warehouse"
    val CassandraConnection = "spark.cassandra.connection.host"
    val CassandraNode = "localhost"
    val CassandraConnectionPort = "spark.cassandra.connection.port"
    val CassandraPort = "9042"
    val InFileDelimiter = ","
    val InDateFormat = "MM/dd/yyyy"
    val OutDateFormat = "yyyy-MM-dd"
    val CassandraKeyspace = "collision_db"
    val CassandraTable = "collisions_by_zip"
    val ColumnNames = Seq("date",
                          "time",
                          "borough",
                          "zipCode",
                          "latitude",
                          "longtitude",
                          "location",
                          "onStreetName",
                          "crossStreetName",
                          "offStreetName",
                          "numberOfPersonsInjured",
                          "numberOfPersonsKilled",
                          "numberOfPedestriansInjured",
                          "numberOfPedestriansKilled",
                          "numberOfCyclistInjured",
                          "numberOfCyclistKilled",
                          "numberOfMotoristInjured",
                          "numberOfMotoristKilled",
                          "contributingFactorVehicle1",
                          "contributingFactorVehicle2",
                          "contributingFactorVehicle3",
                          "contributingFactorVehicle4",
                          "contributingFactorVehicle5",
                          "uniqueKey",
                          "vehicleTypeCode1",
                          "vehicleTypeCode2",
                          "vehicleTypeCode3",
                          "vehicleTypeCode4",
                          "vehicleTypeCode5")
    val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "example",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    val topics = Array("collisions-topic")
  }
}