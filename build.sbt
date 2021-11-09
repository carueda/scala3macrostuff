val scala3Version = "3.1.0"

// kindda conflicts while also editing in vscode
// scalafmtOnCompile := true

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3macrostuff",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "com.outr" %% "scribe" % "3.6.0",
      "org.scalameta" %% "munit" % "1.0.0-M1" % Test,
    ),
  )

scalacOptions ++= Seq(
  "-Xcheck-macros",
  // "-Ycheck:all",
)
