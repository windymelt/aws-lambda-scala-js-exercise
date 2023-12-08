import org.scalajs.linker.interface.ModuleSplitStyle
import org.scalajs.linker.interface.OutputPatterns

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin, ScalablyTypedConverterPlugin, ScalaJSBundlerPlugin)
  .settings(
    name := "aws-lambda-scala-js-exercise",
    scalaVersion := "3.3.1",
    scalacOptions ++= Seq("-encoding", "utf-8", "-deprecation", "-feature"),

    // scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.CommonJSModule)
        .withOutputPatterns(OutputPatterns.fromJSFile("%s.js"))
        .withModuleSplitStyle(ModuleSplitStyle.SmallModulesFor(List("example")))
    },

    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.4.0",
      "com.github.tarao" %%% "record4s" % "0.9.1",
    ),

    Compile / npmDependencies ++= Seq(
      "@aws-crypto/sha256-js" -> "5.2.0",
    ),
    stMinimize := Selection.AllExcept("@aws-crypto/sha256-js"),
    stOutputPackage := "npm",
  )
