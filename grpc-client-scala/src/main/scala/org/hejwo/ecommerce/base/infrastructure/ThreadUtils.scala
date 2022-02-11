package org.hejwo.ecommerce.base.infrastructure

import cats.effect.IO

import java.lang.management.{ManagementFactory, ThreadInfo}

object ThreadUtils {

  def printThreads(desc: String): IO[List[ThreadInfo]] = {
    val allThreads = IO {
      ManagementFactory.getThreadMXBean
        .dumpAllThreads(false, false)
        .toList
    }

    for {
      getThreads <- allThreads
      _ <- IO(println(desc + s"\t\t(thread count: ${getThreads.size})"))
    } yield getThreads
  }

  def diff(list1: List[ThreadInfo], list2: List[ThreadInfo]): Unit =
    list2
      .filterNot(e2 => list1.exists(e1 => e1.getThreadName == e2.getThreadName))
      .sortBy(_.getThreadName)
      .zipWithIndex
      .map { case (info, inc) => s"$inc: ${info.toString}" }
      .foreach(println)
}
