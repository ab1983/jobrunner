package com.workday.scala

import scala.util.Random

class NaiveJobRunner extends JobRunner {

  @volatile
  private var shouldContinue = true
  @volatile
  private var shutdownFinished = false

  override def run(jobQueue: JobQueue): Unit = {
    while (shouldContinue) {
      val nextJob = jobQueue.pop()
      nextJob.execute()
    }
    shutdownFinished = true
  }

  override def shutdown(): Unit = {
    shouldContinue = false
    while (!shutdownFinished) {
      Thread.sleep(1)
    }
  }
}

case class NaiveJobQueue(initialQueue: List[Job]) extends JobQueue {
  @volatile
  private var currentQueue: List[Job] = initialQueue

  override def pop(): Job = this.synchronized {
    currentQueue match {
      case head :: tail =>
        currentQueue = tail
        head
      case Nil =>
        Thread.sleep(Long.MaxValue)
        throw new NoSuchElementException("end of the world")
    }
  }

  override def length: Int = currentQueue.size
}

case class NaiveJob(customerId: Long = Random.nextLong(), duration: Int = 100) extends Job {
  override val uniqueId: Long = Random.nextLong()

  var executed: Boolean = false

  override def execute(): Unit = {
    Thread.sleep(duration)
    executed = true
  }
}