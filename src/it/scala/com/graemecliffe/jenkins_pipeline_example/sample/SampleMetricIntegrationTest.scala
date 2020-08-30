package com.graemecliffe.jenkins_pipeline_example.sample

import com.graemecliffe.jenkins_pipeline_example.common.util.SharedSparkSession.spark
import com.graemecliffe.jenkins_pipeline_example.common.util.{DfValidator, ProjectUtil}
import org.scalamock.scalatest.MockFactory
import org.scalatest.FlatSpec

class SampleMetricIntegrationTest extends FlatSpec with MockFactory {

  val utils = new DfValidator

  "getDf" should "create a DF with the expected schema" in {
    implicit val util: ProjectUtil = utils.getUtil
    val df = SampleMetric().getDf
    utils.checkSchema(
      df,
      Seq(
        "date_time",
        "animal_type",
        "colour"
      )
    )

    utils.checkCounts(df, Some(2), Some(3))
  }
}
