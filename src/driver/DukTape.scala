package driver

import java.io._

import util.Helper.getPropertyOrElse

import scala.io.Source

object DukTape extends App {

  val SENTINEL = "$#$#$#$#"
  val WORKING_DIR = getPropertyOrElse("wdir","/users/roncoleman/tmp/style/")

  System.setProperty("user.dir", WORKING_DIR)

  val lines = Source.fromFile(WORKING_DIR + "duktape.c").getLines.toList

  var os:PrintStream = null

  (0 until lines.length).foreach { k =>
    val line = lines(k)

    if(line.startsWith(SENTINEL)) {
      close(os)

      val name = "duktape-" + k + ".c"

      val path = WORKING_DIR + "/duktape/" + name

      os = new java.io.PrintStream(new File(path))
    }
    else
      os.println(line)
  }

  close(os)

  def close(os: PrintStream): Unit = {
    if(os == null)
      return

    os.flush
    os.close
  }
}
