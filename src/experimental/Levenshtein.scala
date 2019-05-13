package experimental

import scala.io.Source

object Levenshtein extends App {
  println("hello")

  import org.apache.commons.text.similarity.LevenshteinDetailedDistance
  val distance = new LevenshteinDetailedDistance

//  val d = distance.apply("frog", "fog")
//  println(d)

  System.setProperty("user.dir", "/users/roncoleman/tmp/style/")

  // See http://www.codecodex.com/wiki/Execute_an_external_program_and_capture_the_output
  val astyle = "Astyle --style=linux < linux-kernel/acct.c > acct.c.linux"
  val cmd = Array("/bin/sh", "-c", astyle)
  Runtime.getRuntime.exec(cmd)

  val base = Source.fromFile("/users/roncoleman/tmp/style/linux-kernel/acct.c").mkString
  val treated = Source.fromFile("/users/roncoleman/tmp/style/acct.c.linux").mkString

  val d = distance.apply(treated, base)
  println(d)


}
