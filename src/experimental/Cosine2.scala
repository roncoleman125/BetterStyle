package experimental

import scala.io.Source

object Cosine2 extends App {
  import info.debatty.java.stringsimilarity.QGram
  val qgram = new QGram(2)

  println(qgram.distance("ABCD", "ABCE"))

  import info.debatty.java.stringsimilarity._

  val s1 = "if (time_is_after_jiffies(acct->needcheck)) {"
//  val s2 = "$$ $$ $$ My other string..."
//  val s2 = "a1 a2 a3 a4 if (time_is_after_jiffies(acct->needcheck)) {"
  val s2 = "a101a102a103a104ifa105(time_is_after_jiffies(acct->needcheck)) {"



  var t1 = s1.replace("x", "# ")
  var t2 = s2.replace("x","# ")

//  val NGRAMS = 4
  val NGRAMS = 3
  val cosine = new Cosine(NGRAMS)

  var profile1 = cosine.getProfile(t1)
  var profile2 = cosine.getProfile(t2)

  println(cosine.similarity(profile1, profile2))

  val base = Source.fromFile("/users/roncoleman/tmp/style/linux-kernel/acct.c").mkString
  val treated = Source.fromFile("/users/roncoleman/tmp/style/acct.c.linux").mkString

  import util.Helper._
  t1 = decorate(base)
  t2 = decorate(treated)

  profile1 = cosine.getProfile(t1)
  profile2 = cosine.getProfile(t2)

  println("acct.c = "+cosine.similarity(profile1, profile2)+" "+base.count(p => p=='\n'))

  val base2 = Source.fromFile("/users/roncoleman/tmp/style/linux-kernel/fair.c").mkString
  val treated2 = Source.fromFile("/users/roncoleman/tmp/style/fair.c.linux").mkString

  t1 = decorate(base2)
  t2 = decorate(treated2)

  profile1 = cosine.getProfile(t1)
  profile2 = cosine.getProfile(t2)

  println("fair.c = "+cosine.similarity(profile1, profile2)+" "+base2.count(p => p=='\n'))

  val base3 = Source.fromFile("/users/roncoleman/tmp/style/linux-kernel/verifier.c").mkString
  val treated3 = Source.fromFile("/users/roncoleman/tmp/style/verifier.c.linux").mkString

  t1 = decorate(base3)
  t2 = decorate(treated3)

  profile1 = cosine.getProfile(t1)
  profile2 = cosine.getProfile(t2)

  println("verifier.c = "+cosine.similarity(profile1, profile2)+" "+base3.count(p => p=='\n'))

  val base4 = Source.fromFile("/users/roncoleman/tmp/style/linux-kernel/fair.c").mkString
  val treated4 = Source.fromFile("/users/roncoleman/tmp/style/fair.c.gnu").mkString

  t1 = decorate(base4)
  t2 = decorate(treated4)

  profile1 = cosine.getProfile(t1)
  profile2 = cosine.getProfile(t2)

  println("GNU fair.c = "+cosine.similarity(profile1, profile2)+" "+base2.count(p => p=='\n'))

  val base5 = Source.fromFile("/users/roncoleman/tmp/style/linux-kernel/verifier.c").mkString
  val treated5 = Source.fromFile("/users/roncoleman/tmp/style/verifier.c.gnu").mkString

  import util._
  t1 = Helper.decorate(base5)
  t2 = Helper.decorate(treated5)

  profile1 = cosine.getProfile(t1)
  profile2 = cosine.getProfile(t2)

  println("GNU verifier.c = "+cosine.similarity(profile1, profile2)+" "+base3.count(p => p=='\n'))


  val base6 = Source.fromFile("/users/roncoleman/tmp/style/linux-kernel/acct.c").mkString
  val treated6 = Source.fromFile("/users/roncoleman/tmp/style/acct.c.gnu").mkString

  t1 = Helper.decorate(base6)
  t2 = Helper.decorate(treated6)

  profile1 = cosine.getProfile(t1)
  profile2 = cosine.getProfile(t2)

  println("GNU acct.c = "+cosine.similarity(profile1, profile2)+" "+base.count(p => p=='\n'))


}
