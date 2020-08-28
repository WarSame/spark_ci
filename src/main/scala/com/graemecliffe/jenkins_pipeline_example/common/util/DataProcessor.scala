package com.graemecliffe.jenkins_pipeline_example.common.util

import org.apache.spark.sql.{DataFrame, SparkSession}

trait DataProcessor {
  def table(tableName: String)(implicit spark: SparkSession): DataFrame

  def sql(sqlQuery: String)(implicit spark: SparkSession): DataFrame
}

class SparkDataProcessor extends DataProcessor {
  override def table(tableName: String)(implicit spark: SparkSession): DataFrame =
  {
    spark.table(tableName)
  }

  override def sql(sqlQuery: String)(implicit spark: SparkSession): DataFrame =
  {
    spark.sql(sqlQuery)
  }
}
