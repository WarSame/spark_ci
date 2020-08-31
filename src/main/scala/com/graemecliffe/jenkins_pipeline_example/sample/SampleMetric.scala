package com.graemecliffe.jenkins_pipeline_example.sample

import com.graemecliffe.jenkins_pipeline_example.common.util.{Metric, ProjectUtil}
import org.apache.spark.sql.{DataFrame, SparkSession}

object SampleMetric{
  def apply()(implicit spark: SparkSession, util: ProjectUtil): SampleMetric =
    new SampleMetric()(spark, util)
}

class SampleMetric()(implicit spark: SparkSession, util: ProjectUtil)
 extends Metric
{
  override def metricName: String = "sample"

  override def metricTableName: String = "main.dates"

  //Basic Dataframe getter
  //There is no Driver in this project, but you can return the DF to your existing Driver
  //This is called in your test to get a sample DataFrame back as a result
  override def getDf: DataFrame = {
    util.getSampleDataFrame
  }

}
