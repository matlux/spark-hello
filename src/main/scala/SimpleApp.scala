
import java.net.{URL, URI, URLClassLoader}

import com.typesafe.config.ConfigFactory
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.hbase._
import org.apache.hadoop.hbase.client._
import org.apache.spark.{SparkConf, SparkContext}
import scala.collection.JavaConversions._

//import org.apache.spark.{SparkConf, SparkContext}

object SimpleApp {
  val conf = HBaseConfiguration.create()

  val LOG = new Log(this.getClass, conf)
  def main(args: Array[String]) {


    LOG.info("Connecting to HBase ...")
    val conn = ConnectionFactory.createConnection()
    LOG.info(s"HBase connected: $conn")

    val tableName = TableName.valueOf("mercury_dev:mathieu_test")
    val tableExists = conn.getAdmin.tableExists(tableName)
    if(!tableExists)
    {
      val tableDesc = new HTableDescriptor(tableName)
      tableDesc.addFamily(new HColumnDescriptor(Bytes.toBytes("f1")))
      tableDesc.addFamily(new HColumnDescriptor(Bytes.toBytes("f2")))
      tableDesc.addFamily(new HColumnDescriptor(Bytes.toBytes("f3")))
      conn.getAdmin.createTable(tableDesc)
    }

    val table = conn.getTable(tableName)
    LOG.info(s"HBase table: $table")
    val myKey = "r1"
    table.put(new Put(Bytes.toBytes(myKey)).addColumn(Bytes.toBytes("f1"), Bytes.toBytes(""), Bytes.toBytes("something")))
    LOG.info(s"HBase put ${myKey} to table SUCCEEDED")

    val result = table.get(new Get(Bytes.toBytes(myKey)))
    LOG.info(s"HBase get ${myKey} found? ${!result.isEmpty}")



    val currentClassPath: Seq[URL] = System.getProperty("java.class.path").split(":")
      .filterNot(_.isEmpty)
      .map((x: String) => new URI("file://" + x).toURL)
    LOG.info(s"currentClassPath: ${currentClassPath}")
    LOG.info(s"Java ClassPath: ${currentClassPath.map(_.toString).mkString("\n")}")

    val currentClassPathArray: Array[URL] = currentClassPath.toArray
    val origClassLoader = new URLClassLoader(currentClassPathArray, null)


    val thisClassLoader = this.getClass.getClassLoader
    LOG.info(s"OurClassLoader is: ${thisClassLoader.toString}")
    LOG.info(s"OrigClassLoader is: ${origClassLoader}")

    //Thread.currentThread().setContextClassLoader(origClassLoader)

    LOG.info("Creating a new SparkContext ...")
    /*val sparkContextClass = origClassLoader.loadClass("org.apache.spark.SparkContext")
    val sparkConfClass = origClassLoader.loadClass("org.apache.spark.SparkConf")
    val sparkConf: Object = sparkConfClass.newInstance().asInstanceOf[Object]
    val setAppNameMethod = sparkConfClass.getMethod("setAppName", classOf[java.lang.String])
    setAppNameMethod.invoke(sparkConf, new java.lang.String("Simple Application"))
    LOG.info(s"Created sparkConf: $sparkConf")

    val sparkContextConstructor = sparkContextClass.getConstructor(sparkConfClass)
    val sc = sparkContextConstructor.newInstance(sparkConf)*/

    val conf = new SparkConf()
    conf.setAppName("Simple Application")
    LOG.info(s"SparkConfig: ${conf.toDebugString}")
    val sc = new SparkContext(conf)

    LOG.info(s"SparkContext created: $sc")
  }

}