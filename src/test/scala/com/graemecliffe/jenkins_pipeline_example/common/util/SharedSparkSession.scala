package com.graemecliffe.jenkins_pipeline_example.common.util

import java.io.File

import org.apache.spark.sql.SparkSession

object SharedSparkSession {
  /**
    * SharedSparkSession is a spark session usable for unit tests that share a single spark object. By
    * using the same object only one spark session is created for all tests.
    */

  lazy implicit val spark: SparkSession = createSparkSession

  private val warehousePath: String = new File("temp/spark-warehouse").getAbsolutePath
  private val checkpointPath: String = new File("temp/checkpoints").getAbsolutePath

  private def createSparkSession: SparkSession = {
    implicit val session: SparkSession = SparkSession
      .builder()
      .appName("spark_test")
      .master("local")
      .config("spark.sql.warehouse.dir", warehousePath)
      .config("spark.ui.enabled", "false")
      .config("spark.sql.shuffle.partitions", "1")
      .getOrCreate()
    session.sparkContext.setCheckpointDir(checkpointPath)
    session.sparkContext.setLogLevel("ERROR")
    EnvironmentCreator.loadDfs(session)
    session
  }

}

