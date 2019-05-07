package util

import java.io.File

object Helper {
  def getListOfFiles(dir: String):List[File] = {
    val wd = System.getProperty("user.dir")

    val d = if(dir.startsWith("/")) new File(dir) else new File(wd+"/"+dir)

    if (d.exists && d.isDirectory)
      d.listFiles.filter(_.isFile).toList
    else
      List[File]()
  }

  def getListOfFiles():List[File] = {
    val wd = System.getProperty("user.dir")

    val d = new File(wd)

    if (d.exists && d.isDirectory)
      d.listFiles.filter(_.isFile).toList
    else
      List[File]()
  }

  def removeFile(path: String): Boolean = new File(path).delete()
}
