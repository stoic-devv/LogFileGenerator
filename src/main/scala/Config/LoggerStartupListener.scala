package Config

import Config.LoggerStartupListener.DEFAULT_INSTANCE_ID

import sys.process.*
import ch.qos.logback.classic.{Level, Logger, LoggerContext}
import ch.qos.logback.classic.spi.LoggerContextListener
import ch.qos.logback.core.spi.ContextAwareBase
import ch.qos.logback.core.spi.LifeCycle
import software.amazon.awssdk.regions.internal.util.EC2MetadataUtils

import scala.util.{Failure, Success, Try}


object LoggerStartupListener {
  private val INSTANCE_ID_KEY = "EC2_INSTANCE_ID"
  private val DEFAULT_INSTANCE_ID = "i-00000000"
  private val AWS_METADATA_URL = ""
  private val INSTANCE_ID_PATH = ""
}

class LoggerStartupListener extends ContextAwareBase with LoggerContextListener with LifeCycle {
  private var started = false

  override def start(): Unit = {
    if (started) return
    val instanceId = Try(EC2MetadataUtils.getInstanceId()) match {
      case Failure(e) => LoggerStartupListener.DEFAULT_INSTANCE_ID
      case Success(id) => id
    }
    val context = getContext
    context.putProperty(LoggerStartupListener.INSTANCE_ID_KEY, instanceId)
    started = true
  }

  override def stop(): Unit = {
    ???
  }

  override def isStarted: Boolean = started

  override def isResetResistant = true

  override def onStart(context: LoggerContext): Unit = {}

  override def onReset(context: LoggerContext): Unit = {}

  override def onStop(context: LoggerContext): Unit = {}

  def onLevelChange(logger: Logger, level: Level): Unit = {}
}