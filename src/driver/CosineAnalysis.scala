package driver

import info.debatty.java.stringsimilarity.{Cosine, Levenshtein}
import util.Helper

import scala.io.Source

/**
  * This object analyzes code treated with Linux and GNU styles
  */
object CosineAnalysis extends App {
//  val DIR = "/users/roncoleman/tmp/style/linux-kernel"
  val DIR = "/users/roncoleman/tmp/style/linux-lib"
//  val DIR = "/users/roncoleman/tmp/style/coreutils"
//  val DIR = "/users/roncoleman/tmp/style/gmp"
//  val DIR = "/users/roncoleman/tmp/style/petsc"
//  val DIR = "/users/roncoleman/tmp/style/fftw-nostubs"

  val STYLES = List("kr", "linux", "orig", "gnu")

  val t0 = System.currentTimeMillis

  val NGRAM = 3
  val cosine = new Cosine(NGRAM)

  val levenshtein = new Levenshtein

  System.setProperty("user.dir", DIR)
  val listOfFiles = Helper.getListOfFiles

  // Output the report header
//  println("base loc pro1 linux:loc pro2 cos d gnu:loc pro2 cos d")
  print("base loc pro1 ")
  STYLES.foreach { style =>
    print(style+":loc pro2 cos d ")
  }
  println("")

  Helper.initCodec

  listOfFiles.foreach { input =>
    print(input.getName + " ")
    val base = Source.fromFile(input).mkString

    val baseLoc = base.count { c => c == '\n' }
    print(baseLoc + " ")

    val s1 = Helper.remodel(base)

    val profile1 = cosine.getProfile(s1)
    print(profile1.size() + " ")

    STYLES.foreach { style =>
      val output = input + "." + style

//      val options = if(style == "gnu") "--break-return-type" else ""

//      val styleCmd = "Astyle " + options + " --mode=c --style=" + style + " < " + input + " > " + output
      val gindentCmd = "gindent -" + style + " < " + input + " > " + output

//      val cmd = Array("/bin/sh", "-c", styleCmd)
      val cmd = Array("/bin/sh", "-c", gindentCmd)

      Runtime.getRuntime.exec(cmd).waitFor

      val treated = Source.fromFile(output).mkString
      Helper.removeFile(output)

      val treatedLoc = treated.count(c => c == '\n') + " "
      print(treatedLoc)

      val s2 = Helper.remodel(treated)

      val profile2 = cosine.getProfile(s2)
      print(profile2.size() + " ")

      val similarity = cosine.similarity(profile1, profile2)
      print(similarity + " ")

      val d = levenshtein.distance(treated, base)
      print(d.toInt+" ")
    }
    println("")
  }
  val now = System.currentTimeMillis

  println("time: "+((now-t0)/1000))
}
