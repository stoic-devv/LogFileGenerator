package aws

import com.amazonaws.AmazonServiceException
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import HelperUtils.{CreateLogger, ObtainConfigReference}
import com.amazonaws.services.s3.transfer.TransferManagerBuilder
import constants.AwsConstants.{BUCKET_NAME, DIR_PATH}

import java.io.{File, FileNotFoundException}

object AwsDataStore:

  val logger = CreateLogger(classOf[AwsDataStore.type])
  val conf = ObtainConfigReference("aws") match {
    case Some(value) => value.getConfig("aws")
    case None => throw new RuntimeException("Cannot obtain a reference to the config data.")
  }

  // Transfer manager that handles upload
  val xferMgr = TransferManagerBuilder.standard().build()

  /**
   * Writes log files to an S3 bucket
   * Log directory and S3 bucket name are configured in application.conf
   **/
  def writeToS3(): Unit =
    try {
      val xfer = xferMgr.uploadDirectory(conf.getString(BUCKET_NAME), "",
        new File(conf.getString(DIR_PATH)), true)
      logger.info(xfer.getDescription())
      xfer.waitForCompletion()
      xferMgr.shutdownNow()
    } catch {
      case e: AmazonServiceException => logger.error(s"File upload to S3 failed with ${e.getErrorMessage()}")
      case e: FileNotFoundException => logger.error(s"No file/folder found at ${conf.getString(DIR_PATH)}")
    }
