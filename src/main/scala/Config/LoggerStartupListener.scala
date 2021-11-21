package Config

import ch.qos.logback.classic.{Level, Logger, LoggerContext}
import ch.qos.logback.classic.spi.LoggerContextListener
import ch.qos.logback.core.spi.ContextAwareBase
import ch.qos.logback.core.spi.LifeCycle


object LoggerStartupListener {
  private val INSTANCE_ID_KEY = "EC2_INSTANCE_ID"
  private val DEFAULT_INSTANCE_ID = "i-00000000"
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
    System.getenv(LoggerStartupListener.INSTANCE_ID_KEY) match {
      case x if (x != null && x.length > 0) => x
      case _ => LoggerStartupListener.DEFAULT_INSTANCE_ID
    }
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