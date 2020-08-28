package com.graemecliffe.jenkins_pipeline_example.common.util

import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.io.Source

object EnvironmentCreator {

  //Load DFs into local Spark environment
  def loadDfs(implicit spark: SparkSession): Unit = {
    println("Loading Dfs")
    dfs.foreach{
      case(name,df) =>
        val dbName = name.substring(0, name.indexOf("."))
        val tableName = name.substring(name.indexOf(".") + 1)
        spark.sql(f"create database if not exists $dbName")
        if (!spark.catalog.tableExists(dbName, tableName)){
          println(s"Adding table with $dbName and $tableName")
          df.write.saveAsTable(name)
        }
    }
  }

  //List of table names from resources/dfNames.txt mapped to their Spark DF
  def dfs(implicit spark: SparkSession): Iterator[(String, DataFrame)] = {
    val filePath = getClass.getResourceAsStream("/dfNames.txt")
    val names = Source.fromInputStream(filePath).getLines()
    names map (name => name -> getDf(name))
  }

  //Read export CSV file in resources/source_data to Spark DF
  //Query syntax is in resources/source_queries SQL file if data needs to be updated
  def getDf(name: String)(implicit spark: SparkSession): DataFrame ={
    val path = getClass.getResource(s"/source_data/$name.csv").getPath
    spark.read
      .option("header", "true")
      .option("timestampFormat", "yyyy-MM-dd HH:mm:ss")
      .option("inferSchema", "true")
      .csv(path)
  }
}
