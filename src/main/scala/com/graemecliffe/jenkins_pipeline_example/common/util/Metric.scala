package com.graemecliffe.jenkins_pipeline_example.common.util

import org.apache.spark.sql.DataFrame

//A Trait which can be used for all of your jobs
//In this case, I am calling mine Metrics
//By making your jobs implement this Trait
//You can make developing your driver and Tests easier
//Since they all only need to call .getDf
trait Metric {
  def metricName: String

  def metricTableName: String

  def getDf: DataFrame
}
