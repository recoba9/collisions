package collisions

import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe

/**
  * Created by Rostislav on 18.10.2016.
  */
object Stream extends CollisionsConfig {
  def main(args: Array[String]): Unit = {
    if ( args.length < 1 ) {
      System.err.println("Usage: Stream <tgt-data-dir>")
      System.exit(1)
    }

    val tgtDataDir = args(0).trim

    val stream = KafkaUtils.createDirectStream[String, String](
      streamingContext,
      PreferConsistent,
      Subscribe[String, String](Configuration.topics, Configuration.kafkaParams)
    )

    val ds = stream.map(record => (record.key, record.value))

    QualityGate.checkAndFormatFromTopic(ds)
      .foreachRDD(rdd =>
        if (!rdd.isEmpty()) rdd.saveAsTextFile(tgtDataDir + "-" + System.currentTimeMillis().toString)
      )

    streamingContext.start()

    streamingContext.awaitTermination()
  }
}