package collisions

/**
  * Created by Rostislav on 14.10.2016.
  */

import org.apache.spark.sql.SaveMode

object Load extends CollisionsConfig {
  def main(args: Array[String]) {

    if ( args.length < 1 ) {
      System.err.println("Usage: Load <src-data-dir>")
      System.exit(1)
    }

    val srcDataDir = args(0).trim

    val inDF = sparkSession.read
      .format("com.databricks.spark.csv")
      .option("delimiter", Configuration.InFileDelimiter)
      .option("header", true)
      .load(srcDataDir).cache()

    val outDF = QualityGate.checkAndFormatFromFile(inDF)

    outDF.write
      .format("org.apache.spark.sql.cassandra")
      .options(Map( "table" -> Configuration.CassandraTable, "keyspace" -> Configuration.CassandraKeyspace))
      .mode(SaveMode.Append)
      .save()
  }
}