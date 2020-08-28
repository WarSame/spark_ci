package com.graemecliffe.jenkins_pipeline_example.sample

import com.graemecliffe.jenkins_pipeline_example.common.util.{DataProcessor, Metric, ProjectUtil}
import org.apache.spark.sql.{DataFrame, SparkSession}

object SampleMetric{
  def apply()(implicit spark: SparkSession, processor: DataProcessor, util: ProjectUtil): SampleMetric =
    new SampleMetric()(spark, processor, util)
}

class SampleMetric()(implicit spark: SparkSession, processor: DataProcessor, util: ProjectUtil)
 extends Metric
{
  override def metricName: String = "sample"

  override def metricTableName: String = "main.dates"

  //Basic Dataframe getter
  //There is no Driver in this project, but you can return the DF to your existing Driver
  override def getDf: DataFrame = {
    processor.table(metricTableName)
  }

}
