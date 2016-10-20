package collisions

import org.apache.spark.sql.SaveMode

/**
  * Created by Rostislav on 16.10.2016.
  */
object Report extends CollisionsConfig{
  def main(args: Array[String]): Unit = {
    if ( args.length < 2 ) {
      System.err.println("Usage: Report <report-type> <save-path>")
      System.exit(1)
    }

    val reportType = args(0).trim
    val tgtDataDir = args(1).trim

    def getSQL(report_type: String): String = {
      reportType match {
        case "monthly" => "select zip, year, date_format(col_date, 'MMM') as month, qty from (\n" +
          "select zip, date_format(col_date, 'yyyy') as year, trunc(col_date, 'MM') as col_date, count(*) as qty\n" +
          "from collisions_by_zip\n" +
          "group by rollup(zip, date_format(col_date, 'yyyy'), trunc(col_date, 'MM')))\n" +
          "order by zip, year, col_date\n"

        case "season" => "select zip, year, substr(season_, 2) as season, qty\n" +
          "from (\n" +
          "select zip, year, season_, count(*) as qty\n" +
          "from (\n" +
          "select zip, date_format(col_date, 'yyyy') as year, \n" +
          "case date_format(col_date, 'MM')\n" +
          "when 1 then '1Winter'\n" +
          "when 2 then '1Winter'\n" +
          "when 3 then '2Spring'\n" +
          "when 4 then '2Spring'\n" +
          "when 5 then '2Spring'\n" +
          "when 6 then '3Summer'\n" +
          "when 7 then '3Summer'\n" +
          "when 8 then '3Summer'\n" +
          "when 9 then '4Fall'\n" +
          "when 10 then '4Fall'\n" +
          "when 11 then '4Fall'\n" +
          "else '5Winter'\n" +
          "end as season_,\n" +
          "col_date\n" +
          "from collisions_by_zip\n" +
          ")\n" +
          "group by rollup(zip, year, season_)\n" +
          "order by zip, year, season_)"

        case "day-night" => "select zip, year, period, count(*) as qty\n" +
          "from (\n" +
          "select zip, date_format(col_date, 'yyyy') as year,\n" +
          "case when substr(col_time, 1, instr(col_time, ':') - 1) >= 6 and substr(col_time, 1, instr(col_time, ':') - 1) < 18 then 'day' else 'night' end as period\n" +
          "from collisions_by_zip\n" +
          ")\n" +
          "group by rollup(zip, year, period)\n" +
          "order by zip, year, period"

        case _ => ""
      }
    }

    val sql = getSQL(reportType)

    if (!sql.isEmpty) {
      sparkSession
        .read
        .format("org.apache.spark.sql.cassandra")
        .options(Map( "table" -> Configuration.CassandraTable, "keyspace" -> Configuration.CassandraKeyspace))
        .load()
        .createOrReplaceTempView("collisions_by_zip")

      val df = sparkSession.sql(sql)

      df.write.mode(SaveMode.Overwrite)
        .format("com.databricks.spark.csv")
        .option("header", "true")
        .save(tgtDataDir)
    }
    else {
      System.err.println("Unsupported report-type")
      System.exit(1)
    }
  }
}