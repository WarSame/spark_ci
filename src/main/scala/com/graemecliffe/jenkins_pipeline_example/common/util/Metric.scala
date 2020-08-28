package com.graemecliffe.jenkins_pipeline_example.common.util

import org.apache.spark.sql.DataFrame

trait Metric {
  def metricName: String

  def metricTableName: String

  def getDf: DataFrame
}
