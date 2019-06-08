package driver

import info.debatty.java.stringsimilarity.{Cosine, Damerau, Jaccard, JaroWinkler, Levenshtein, LongestCommonSubsequence, NormalizedLevenshtein}
import org.apache.commons.text.similarity.JaccardSimilarity
import util.Helper._

import scala.io.Source

/**
  * This object analyzes code treated with Linux and GNU styles
  */
object SimilarityAnalysis extends App {

  val DECORATE_ENABLED = getPropertyOrElse("decorate",true)

  //  val DIR = "/users/roncoleman/tmp/style/linux-lib"
  //  val DIR = "/users/roncoleman/tmp/style/coreutils"
  //  val DIR = "/users/roncoleman/tmp/style/gmp"
  //  val DIR = "/users/roncoleman/tmp/style/petsc"
  //  val DIR = "/users/roncoleman/tmp/style/fftw-nostubs"
  //  val DIR = "/users/roncoleman/tmp/style/gimp"
  val SRC_DIR = getPropertyOrElse("srcdir","linux-kernel")

  val WORKING_DIR = getPropertyOrElse("wdir","/users/roncoleman/tmp/style/")

  val STYLES = List("kr", "linux", "orig", "gnu")

  val RESULTS_FILE = "results.txt"

  System.setProperty("user.dir", WORKING_DIR)

  val t0 = System.currentTimeMillis

  val os = new java.io.PrintStream(new java.io.FileOutputStream(WORKING_DIR + RESULTS_FILE,false))

  val ngram = getPropertyOrElse("ngram",3)

  val shingle = new Jaccard(ngram)

  // Get the metric string distance method
  val msd = new Levenshtein
//  val msd = new Damerau
//  val msd = new JaroWinkler()
//  val msd = new LongestCommonSubsequence

  val listOfFiles = getListOfFiles(SRC_DIR,".c")

  print(listOfFiles.size+" files "+SRC_DIR+" decorate "+DECORATE_ENABLED+" ngram "+ngram+" ")
  println(shingle.getClass.getSimpleName+" "+msd.getClass.getSimpleName)

  // Output the report header
  os.print("file base:loc base:pro1 ")

  STYLES.foreach { style =>
    os.print(style+":loc "+style+":pro2 "+style+":cos "+style+":d ")
  }
  os.println("")

  // Set the file input codec
  initCodec

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
    val s1 = decorate(base, DECORATE_ENABLED)

    // Get the baseline's profile or vector
    val profile1 = shingle.getProfile(s1)
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
      removeFile(output)

      // Get the LOC of the treated file
      val treatedLoc = treated.count(c => c == '\n') + " "
      os.print(treatedLoc)

      // Decorate the treated file
      val s2 = decorate(treated, DECORATE_ENABLED)

      // Get the treated file's profile or vector
      val profile2 = shingle.getProfile(s2)
      os.print(profile2.size() + " ")

      // Compute the similarity
      val similarity = shingle.similarity(profile1, profile2)
      os.print(similarity + " ")

      // Get the edit distance between treated and base files
      val d = msd.distance(treated, base)
      os.print(d.toInt+" ")
      os.flush
    }

    os.println("")
    os.flush
  }
  val now = System.currentTimeMillis

  println("time: "+((now-t0)/1000))
}
