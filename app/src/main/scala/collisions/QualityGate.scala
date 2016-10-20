package collisions

/**
  * Created by Rostislav on 15.10.2016.
  */

import java.sql.Date
import org.apache.spark.sql.{DataFrame, Dataset}
import org.apache.spark.streaming.dstream.DStream
import org.joda.time.format.DateTimeFormat

object QualityGate {
  def checkDate(date: String, format: String): Boolean = {
    try {
      val formatter = DateTimeFormat.forPattern(format)
      formatter.parseDateTime(date)
      true
    }
    catch {
      case _: Throwable => false
    }
  }

  def checkInt(int: String): Boolean = {
    try {
      int.toInt
      true
    }
    catch {
      case _: Throwable => false
    }
  }

  def checkAndFormatFromFile(df: DataFrame): Dataset[FormattedCollisionRecord] = {
    import Load.sparkSession.implicits._

    val inFormat = Load.Configuration.InDateFormat

    // rename DF column names to exclude spaces
    val renamedDF = df.toDF(Load.Configuration.ColumnNames: _*)

    // filter out empty zip, date and unique key columns; incorrect date and unique key formats
    val filteredDS = renamedDF.as[RawCollisionRecord].filter(x =>
      !x.zipCode.isEmpty && !x.date.isEmpty && !x.uniqueKey.isEmpty &&
        checkDate(x.date, inFormat) && checkInt(x.uniqueKey))

    //transform filteredDS to FormattedCollisionRecord DS (Cassandra data types)
    filteredDS.map(x => {
      val formatter = DateTimeFormat.forPattern(inFormat)
      FormattedCollisionRecord(x.zipCode, new Date(formatter.parseDateTime(x.date).getMillis),
                                x.uniqueKey.toInt, x.time,
                                x.borough, x.onStreetName, x.crossStreetName,
                                x.offStreetName, x.location)})
  }

  def checkAndFormatFromTopic(ds: DStream[(String, String)]): DStream[FormattedCollisionRecord] = {
    val inFormat = Stream.Configuration.InDateFormat

    // split input string delimited by delimiter
    val splittedDS = ds.map(x => x._2.split(Stream.Configuration.InFileDelimiter, -1))

    // filter out empty zip, date and unique key columns; incorrect date and unique key formats
    val filteredDS = splittedDS.filter(x =>
      x.length == 29 && !x(0).isEmpty && !x(3).isEmpty &&
        !x(24).isEmpty && checkDate(x(0), inFormat) && checkInt(x(23)))

    //transform filteredDS to FormattedCollisionRecord DS
    filteredDS.map(x => {
      val formatter = DateTimeFormat.forPattern(inFormat)
      FormattedCollisionRecord(x(3), new Date(formatter.parseDateTime(x(0)).getMillis),
        x(23).toInt, x(1),
        x(2), x(7), x(8),
        x(9), x(6))})
  }
}