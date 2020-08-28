package com.graemecliffe.jenkins_pipeline_example.common.util

import org.apache.spark.sql.{Column, DataFrame, SparkSession}
import org.apache.spark.sql.functions.current_timestamp

object ProjectUtil {
  def apply()(implicit spark: SparkSession, processor: DataProcessor): ProjectUtil = new ProjectUtil()
}

class ProjectUtil(now: Column = current_timestamp)(implicit spark: SparkSession, processor: DataProcessor) {

  lazy val getFiscalCalendarDataFrame: DataFrame = {
    processor.table("enterprise.date_dim")
  }
}
