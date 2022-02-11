package org.hejwo.ecommerce.base.application

import cats.effect.IO
import com.typesafe.scalalogging
import org.slf4j.LoggerFactory
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

trait IOLogging {

  private val className = getClass

  protected val logger: Logger[IO] = Slf4jLogger.getLoggerFromClass[IO](className)
  protected val strictLogger: scalalogging.Logger = scalalogging.Logger(LoggerFactory.getLogger(className))

}
