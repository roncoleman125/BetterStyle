import java.io.File

import Levenshtein.distance

import scala.io.Source
import util.Helper

object Levenshtein2 extends App {
  System.setProperty("user.dir", "/users/roncoleman/tmp/style/linux-kernel")

  val listOfFiles = Helper.getListOfFiles

  listOfFiles.foreach { name =>
    println(name)
  }


  val input = listOfFiles(0)
  val output = input +".linux"

  val astyleCmd = "Astyle --style=linux < " + input + " > " + output

  val cmd = Array("/bin/sh", "-c", astyleCmd)
  Runtime.getRuntime.exec(cmd)

  val base = Source.fromFile(input).mkString
  val loc = base.count { c => c == '\n' }

  val treated = Source.fromFile(output).mkString

  Helper.removeFile(output)

  import org.apache.commons.text.similarity.LevenshteinDetailedDistance
  val distance = new LevenshteinDetailedDistance

  val d = distance.apply(base, treated)
  println(d)
  println(loc)


}
