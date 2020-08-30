package com.graemecliffe.jenkins_pipeline_example.common.util

import org.apache.spark.sql.{Column, DataFrame, SparkSession}
import org.apache.spark.sql.functions.current_timestamp

object ProjectUtil {
  def apply()(implicit spark: SparkSession): ProjectUtil = new ProjectUtil()
}

class ProjectUtil(now: Column = current_timestamp)(implicit spark: SparkSession) {

  lazy val getFiscalCalendarDataFrame: DataFrame = {
    spark.table("enterprise.date_dim")
  }
}
