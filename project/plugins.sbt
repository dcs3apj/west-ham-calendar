logLevel := Level.Warn

// Resolvers
resolvers ++= Seq(
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Madoushi sbt-plugins" at "https://dl.bintray.com/madoushi/sbt-plugins/")

//resolvers += "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

//addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.7.0-SNAPSHOT")

// Sbt plugins
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.1")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.6")

addSbtPlugin("com.vmunier" % "sbt-play-scalajs" % "0.3.0")

// sbt web plugins

addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.0")

addSbtPlugin("org.madoushi.sbt" % "sbt-sass" % "0.9.3")