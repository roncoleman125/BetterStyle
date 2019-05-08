import java.io.File

import Levenshtein.distance

import scala.io.Source
import util.Helper

object Levenshtein2 extends App {

  val STYLE = "linux"

  System.setProperty("user.dir", "/users/roncoleman/tmp/style/linux-kernel")

  val listOfFiles = Helper.getListOfFiles

  listOfFiles.foreach { input =>
    print(input.getName+" ")

    val output = input + "." + STYLE

    val astyleCmd = "Astyle --style="+ STYLE +" < " + input + " > " + output

    val cmd = Array("/bin/sh", "-c", astyleCmd)
    Runtime.getRuntime.exec(cmd).waitFor

    val base = Source.fromFile(input).mkString
    val loc = base.count { c => c == '\n' }

    val treated = Source.fromFile(output).mkString

    Helper.removeFile(output)

//    import org.apache.commons.text.similarity.LevenshteinDetailedDistance
//    val distance = new LevenshteinDetailedDistance
//
//    // This statement represents how get code in shape: base -> treated. Notice the parameter order.
//    val d = distance.apply(treated, base)
//
//    println("%d %d %d %d %d".format(loc, d.getDistance, d.getDeleteCount, d.getInsertCount, d.getSubstituteCount))

    import org.apache.commons.lang.StringUtils
    val d = StringUtils.getLevenshteinDistance(treated, base)
    println(loc+" "+d)
  }
}
