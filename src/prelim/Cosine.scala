package prelim

import scala.io.Source

object Cosine extends App {
  import org.apache.commons.text.similarity.CosineDistance

  val distance = new CosineDistance

  val astyle = "Astyle --style=linux < linux-kernel/acct.c > acct.c.linux"
  val cmd = Array("/bin/sh", "-c", astyle)
  Runtime.getRuntime.exec(cmd)

  val base = Source.fromFile("/users/roncoleman/tmp/style/linux-kernel/arraymap.c").mkString
  val treated = Source.fromFile("/users/roncoleman/tmp/style/acct.c.linux").mkString

  val d = distance.apply(treated, base)
  println(d)

}
