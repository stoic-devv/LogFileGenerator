package Config

import Config.LoggerStartupListener.DEFAULT_INSTANCE_ID

import sys.process.*
import ch.qos.logback.classic.{Level, Logger, LoggerContext}
import ch.qos.logback.classic.spi.LoggerContextListener
import ch.qos.logback.core.spi.ContextAwareBase
import ch.qos.logback.core.spi.LifeCycle


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
    val instanceId = getInstanceId()
    val context = getContext
    context.putProperty(LoggerStartupListener.INSTANCE_ID_KEY, instanceId)
    started = true
  }

  def getInstanceId(): String = {

    try {
      val token ="curl -X PUT http://169.254.169.254/latest/api/token -H X-aws-ec2-metadata-token-ttl-seconds: 21600".!!
      val instanceId = s"curl -H X-aws-ec2-metadata-token: $token -v http://169.254.169.254/latest/meta-data/instance-id".!!
      return instanceId
    } catch {
      case e: Exception => DEFAULT_INSTANCE_ID
    }

//    System.getenv(LoggerStartupListener.INSTANCE_ID_KEY) match {
//      case x if (x != null && x.length > 0) => x
//      case _ => LoggerStartupListener.DEFAULT_INSTANCE_ID
//    }
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