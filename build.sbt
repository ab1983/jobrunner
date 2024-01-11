import Dependencies._

ThisBuild / scalaVersion := "2.13.5"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.workday"
ThisBuild / organizationName := "techtest"

lazy val root = (project in file("."))
  .settings(
    name := "WorkdayJobRunner",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += junit % Test,
    libraryDependencies += scalaLogging % Compile,
    libraryDependencies += logback % Compile
  )
