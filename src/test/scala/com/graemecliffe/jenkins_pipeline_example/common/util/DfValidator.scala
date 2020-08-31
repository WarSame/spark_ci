package com.graemecliffe.jenkins_pipeline_example.common.util

import java.sql.Timestamp
import java.time.Instant

import org.apache.spark.sql.{Column, DataFrame}
import org.scalamock.scalatest.MockFactory
import org.apache.spark.sql.functions.{col, lit}
import com.graemecliffe.jenkins_pipeline_example.common.util.SharedSparkSession.spark

class DfValidator(timestampIn: String = "2020-04-28T12:00:00.000Z") extends MockFactory  {

  //Check that the schema we pass in as requiredFields
  //Matches the schema that the test result value df actually has
  def checkSchema(
                   df: DataFrame,
                   requiredFields: Seq[String]
                 ): Unit ={
    val sortedColumns: Array[String] = df.columns.sorted

    println(
      "Result schema: \n" +
        sortedColumns.mkString(",\n")
    )

    val listClue = "Result columns = " + df.columns.mkString(", ") +
      " \nExpected columns = " + requiredFields.mkString(", ") + "\n"
    val missingCols = "Columns in your result that were not expected: \n" +
      (df.columns diff requiredFields ).mkString(", ") + "\n"
    val extraCols = "Columns that were expected but were not in your result: \n" +
      (requiredFields diff df.columns).mkString(", ") + "\n"
    val colMessage = missingCols + extraCols

    //Check that DF has same number of fields as required
    withClue(
      "Output DF does not have required number of fields \n" + listClue
        + colMessage
    ) {
      assertResult(requiredFields.size) {
        df.columns.length
      }
    }
    //Check that DF has every required field, ensures every field is present and there are
    //No surplus fields in the DF
    withClue(
      "Output DF does not have the required field names\n" + listClue
        + colMessage
    ) {
      assertResult(df.columns.length) {
        requiredFields.count(name => df.columns.contains(name))
      }
    }
  }

  //Check that the values passed in as catCount and dogCount
  //Match the row counts we actually have in the test result value df
  def checkCounts(
                   df: DataFrame,
                   catCount: Option[Long],
                   dogCount: Option[Long]
                 ): Unit = {
    val sortedColumns: Array[String] = df.columns.sorted
    val persistedDf = df.select(sortedColumns.head, sortedColumns.tail: _*).persist()

    println(
      "Result schema: \n" +
        sortedColumns.mkString(",\n")
    )

    persistedDf.show()

    val groupedDf = persistedDf.groupBy("animal_type").count
    groupedDf.show()

    catCount match {
      case Some(x) =>
        assertResult(x) {
          groupedDf
            .where(col("animal_type") === "cat")
            .select("count")
            .head
            .getLong(0)
        }
      case None =>
    }
    dogCount match {
      case Some(x) => assertResult(x){
        groupedDf
          .where(col("animal_type") === "dog")
          .select("count")
          .head
          .getLong(0)
      }
      case None =>
    }
  }

  //Timestamp for the current time
  //By default this is the time the job in the real world
  //This can be override so that historical data can be used
  val now: Column = lit(Timestamp.from(Instant.parse(timestampIn)))

  //Mock util which uses data at a certain point in time
  //Since we don't want to constantly update sample data but time marches forward
  //If we are looking at the previous 3 weeks, the sample data would quickly be useless
  //This allows us to always look at the same 3 weeks
  def getUtil: ProjectUtil = {
    new ProjectUtil(now)
  }

  //Write DF out to output directory if validation of data is wanted
  //Also verifies writing out works
  def outputDf(df: DataFrame) : Unit = {
    df.coalesce(1)
      .write
      .option("header","true")
      .option("sep",",")
      .option("timestampFormat", "yyyy/MM/dd HH:mm:ss")
      .mode("overwrite")
      .csv("test-output/path")
  }
}
