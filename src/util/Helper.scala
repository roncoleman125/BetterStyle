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
      s.replace(" ", "$ ")
          .replace("\n","\n@")
          .replace("\t","\t`")
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

  /**
    * Parses a boolean string.
    * @param s String
    * @return True if "true" and false otherwise
    */
  def parseBoolean(s: String): Boolean = if(s == "true") true else false

  /**
    * Parses a string
    * @param s String
    * @return String
    */
  def parseString(s: String) = s

  /**
    * Gets an integer value from system properties, if it's not found use a default.
    * @param key Property
    * @param default Default integer
    * @return Default integer value
    */
  def getPropertyOrElse(key: String, default: Int): Int = getPropertyOrElse(key,Integer.parseInt,default)

  /**
    * Gets a generic property from the system properyies, if it's not found use a default.
    * @param key Property
    * @param parse Parser
    * @param default Default
    * @tparam T Parameterize type of value
    * @return Key-value or default value
    */
  def getPropertyOrElse[T](key: String, parse: (String) => T, default: T): T = {
    val value = System.getProperty(key)

    if(value == null)
      default
    else
      parse(value)
  }

  /**
    * Gets a system property or a default string value
    * @param key Property
    * @param default Default
    * @return Key-value or default value
    */
  def getPropertyOrElse(key: String, default: String): String = getPropertyOrElse(key,parseString,default)

  /**
    * Gets a system property or a default boolean value
    * @param key
    * @param default
    * @return
    */
  def getPropertyOrElse(key: String, default: Boolean): Boolean = getPropertyOrElse(key,parseBoolean, default)
}
