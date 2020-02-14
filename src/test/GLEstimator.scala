package test

object GLEstimator {
  val BILLIONSTHS_OF_SECONDS = 1000000000.0

  def main(args: Array[String]): Unit = {
    val start = System.nanoTime

    val pi = (0 to Int.MaxValue).foldLeft(0.0) { (sum, index) =>
      val numerator = if((index & 1) == 0) 1 else -1

      sum + numerator / ( 2.0*index + 1)
    } * 4

    val finish = System.nanoTime

    val delta = (finish - start) / BILLIONSTHS_OF_SECONDS

    println("Pi = "+pi)
    println("dt = %6.2fs".format(delta))
  }
}
