package com.workday.scala

/**
 * If creating a solution in Scala, your code goes here
 */

class JobRunnerImpl extends JobRunner {
  /**
   * Continuously consumes the job queues and executes the jobs,
   * execution continues forever until shutdown is invoked.
   */
  override def run(jobQueue: JobQueue): Unit = ???

  /**
   * Stops consuming jobs from the queue and blocks until
   * all jobs that have been dequeued have finished their execution.
   */
  override def shutdown(): Unit = ???
}
