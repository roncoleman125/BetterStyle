package driver

import info.debatty.java.stringsimilarity.{Cosine, Levenshtein}
import util.Helper

import scala.io.Source

/**
  * This object analyzes code treated with Linux and GNU styles
  */
object CosineAnalysis extends App {

  //  val DIR = "/users/roncoleman/tmp/style/linux-lib"
  //  val DIR = "/users/roncoleman/tmp/style/coreutils"
  //  val DIR = "/users/roncoleman/tmp/style/gmp"
  //  val DIR = "/users/roncoleman/tmp/style/petsc"
  //  val DIR = "/users/roncoleman/tmp/style/fftw-nostubs"
  //  val DIR = "/users/roncoleman/tmp/style/gimp"
  val SRC_DIR = "linux-kernel"

  val BASE_DIR = "/users/roncoleman/tmp/style/"

  val STYLES = List("kr", "linux", "orig", "gnu")

  val RESULTS_FILE = "results.txt"

  System.setProperty("user.dir", BASE_DIR)

  val t0 = System.currentTimeMillis

  val os = new java.io.PrintStream(new java.io.FileOutputStream(BASE_DIR + RESULTS_FILE,false))

  val NGRAM = 3

  val cosine = new Cosine(NGRAM)

  val levenshtein = new Levenshtein

  val listOfFiles = Helper.getListOfFiles(SRC_DIR)

  println("processing "+listOfFiles.size+" files")

  // Output the report header
  os.print("base loc pro1 ")

  STYLES.foreach { style =>
    os.print(style+":loc pro2 cos d ")
  }
  os.println("")

  // Set the file input codec
  Helper.initCodec

  // Process each file in the src directory
  (0 until listOfFiles.size).foreach { k =>
    val input = listOfFiles(k)

    println((k+1)+". "+input.getName)

    os.print(input.getName + " ")

    // Read-in the baseline and get its LOC
    val base = Source.fromFile(input).mkString

    val baseLoc = base.count { c => c == '\n' }
    os.print(baseLoc + " ")

    // Decorate the baseline
    val s1 = Helper.decorate(base)

    // Get the baseline's profile or vector
    val profile1 = cosine.getProfile(s1)
    os.print(profile1.size() + " ")

    STYLES.foreach { style =>
      val output = input + "." + style

      // Build the style command
//      val styleCmd = "Astyle " + options + " --mode=c --style=" + style + " < " + input + " > " + output
      val styleCmd = "gindent -" + style + " < " + input + " > " + output

      val cmd = Array("/bin/sh", "-c", styleCmd)

      // Invoke the style command and wait for the command to finish
      Runtime.getRuntime.exec(cmd).waitFor

      // Read-in the treated file and remove it
      val treated = Source.fromFile(output).mkString
      Helper.removeFile(output)

      // Get the LOC of the treated file
      val treatedLoc = treated.count(c => c == '\n') + " "
      os.print(treatedLoc)

      // Decorate the treated file
      val s2 = Helper.decorate(treated)

      // Get the treated file's profile or vector
      val profile2 = cosine.getProfile(s2)
      os.print(profile2.size() + " ")

      // Compute the similarity
      val similarity = cosine.similarity(profile1, profile2)
      os.print(similarity + " ")

      // Get the edit distance between treated and base files
      val d = levenshtein.distance(treated, base)
      os.print(d.toInt+" ")
    }

    os.println("")
    os.flush
  }
  val now = System.currentTimeMillis

  println("time: "+((now-t0)/1000))
}
