
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
// http://tomstechnicalblog.blogspot.com/2016/11/using-kotlin-language-with-spark.html?m=1
fun main(args: Array<String>) {

    val conf = SparkConf()
            .setMaster("spark://ben:7077")
            .setAppName("Kotlin Spark Test")

    val sc = JavaSparkContext(conf)

    val items = listOf("123/643/7563/2134/ALPHA", "2343/6356/BETA/2342/12", "23423/656/343")

    val input = sc.parallelize(items)

 //   val sumOfNumbers = input.flatMap { it.split("/") }
//    val sumOfNumbers = input.flatMap { it.split("/") }
//            .filter { it.matches(Regex("[0-9]+")) }
//            .map { it.toInt() }
//            .reduce {total,next -> total + next }

    println(input)
//    println(sumOfNumbers)

}