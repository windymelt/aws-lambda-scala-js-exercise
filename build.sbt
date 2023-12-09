import org.scalajs.linker.interface.ModuleSplitStyle
import org.scalajs.linker.interface.OutputPatterns
import scala.sys.process.Process

lazy val root = project
  .in(file("."))
  .enablePlugins(
    ScalaJSPlugin,
    ScalablyTypedConverterExternalNpmPlugin
  )
  .settings(
    name := "aws-lambda-scala-js-exercise",
    scalaVersion := "3.3.1",
    scalacOptions ++= Seq("-encoding", "utf-8", "-deprecation", "-feature"),

    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.CommonJSModule)
        .withOutputPatterns(OutputPatterns.fromJSFile("%s.js"))
    },
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.4.0",
      "com.github.tarao" %%% "record4s" % "0.9.1"
    ),
    externalNpm := {
      Process(Seq("yarn"), baseDirectory.value).!
      baseDirectory.value
    },
    stOutputPackage := "npm",
    Compile / stMinimize := Selection.AllExcept("@aws-crypto/sha256-js"),
  )
