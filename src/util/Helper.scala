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

  def decorate(s: String, enabled: Boolean = true): String = {
    if(enabled)
      s.replace(" ", "# ")
          .replace("\n","\n@")
          .replace("\t","\t!")
    else
      s
  }

  /**
    * This method mainly sets up the codec to avoid the java.nio.charset.MalformedInputException
    */
  def initCodec: Unit = {
    // See https://stackoverflow.com/questions/26268132/all-inclusive-charset-to-avoid-java-nio-charset-malformedinputexception-input
    // See https://stackoverflow.com/questions/13625024/how-to-read-a-text-file-with-mixed-encodings-in-scala-or-java
    import java.nio.charset.CodingErrorAction
    import scala.io.Codec

    implicit val codec = Codec("ISO-8859-1")
    codec.onMalformedInput(CodingErrorAction.REPLACE)
    codec.onUnmappableCharacter(CodingErrorAction.REPLACE)
  }
}
