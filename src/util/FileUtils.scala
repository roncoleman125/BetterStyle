package util

import java.io._
object FileUtils {
  def setCurrentDirectory(directory_name: String): Boolean = {
    val directory = new File(directory_name).getAbsoluteFile
    if (directory.exists || directory.mkdirs)
      System.setProperty("user.dir", directory.getAbsolutePath) != null
    else
      false
  }
}
