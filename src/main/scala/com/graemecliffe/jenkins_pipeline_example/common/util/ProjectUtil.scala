package com.graemecliffe.jenkins_pipeline_example.common.util

import org.apache.spark.sql.{Column, DataFrame, SparkSession}
import org.apache.spark.sql.functions.{current_timestamp, col, to_date}

object ProjectUtil {
  def apply()(implicit spark: SparkSession): ProjectUtil = new ProjectUtil()
}

class ProjectUtil(now: Column = current_timestamp)(implicit spark: SparkSession) {

  //currentTimestamp is the current time as of runtime by default
  //This value can be overriden for tests
  //So that you don't constantly need to update your CSV files
  //This allows you to use one constant timestamp for tests
  //Which stops your row counts in your tests from constantly changing
  lazy val currentTimestamp: Column = now

  lazy val getSampleDataFrame: DataFrame = {
    spark.table("main.dates")
      .where(
        col("date_time") === currentTimestamp
      )
  }
}
