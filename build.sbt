val dottyVersion = "0.19.0-RC1"
val scala213Version = "2.13.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scalafi",
    version := "0.1.0",

    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % V.cats,
      "org.typelevel" %% "cats-effect" % V.cats,
      "com.novocode" % "junit-interface" % "0.11" % "test"),

    // To make the default compiler and REPL use Dotty
    // scalaVersion := dottyVersion,
    scalaVersion := scala213Version,

    // To cross compile with Dotty and Scala 2
    crossScalaVersions := Seq(/*dottyVersion,*/ scala213Version)
  )

lazy val V = new {
  val cats       = "2.0.0-RC1"
//   val refined    = "0.9.0"
//   val algebra    = "1.0.1"
//   val atto       = "0.6.5"
//   val scalacheck = "1.13.5"
}


// lazy val core = module("core")
//   .dependsOn(meta)
//   .settings(
//     libraryDependencies ++= Seq(
//       "org.typelevel" %%% "cats-core" % V.cats,
//       "org.typelevel" %%% "cats-free" % V.cats))





// ext.akkaStreamVersion = '10.1.9'
// ext.akkaVersion = '2.5.23'
// ext.catsVersion = '2.0.0-RC1'
// ext.catseffectVersion = '2.0.0-RC1'
// ext.dispatchVersion = '1.1.0'
// ext.log4sVersion = '1.8.2'
// ext.log4catsVersion = '1.0.0-RC1'
// ext.nifiVersion = '1.8.0'
// ext.circeVersion = '0.12.0-M4'
// ext.specs2Version = "4.7.0"
