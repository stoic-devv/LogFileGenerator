package hash

import HelperUtils.{CreateLogger, ObtainConfigReference}
import constants.AwsConstants.{DIR_PATH, HASH_TABLE}

import java.io.{File, FileOutputStream, PrintWriter}
import scala.io.Source

object HashTable:


  val logger = CreateLogger(classOf[HashTable.type])
  val conf = ObtainConfigReference("aws") match {
    case Some(value) => value.getConfig("aws")
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }

  def createHashTable(): Unit =
    val fileList = new File(conf.getString(DIR_PATH)).listFiles.filter(_.getName.endsWith(".log"))
    val hashTableFile = new File(conf.getString(DIR_PATH) + conf.getString(HASH_TABLE))
    hashTableFile.createNewFile()
    val printWriter = new PrintWriter(hashTableFile)
    fileList.foreach(file => {
      printWriter.println(Source.fromFile(file).getLines.toList(0).split(" ")(0) + "," +
        file.getName())
    })
    printWriter.close()


