package com.workday.scala

/**
 * ***********************
 * Do not modify this file
 * ***********************
 */

/**
  * Defines a piece of work to be executed on behalf of a customer.
  */
trait Job {
  // Uniquely identifies the customer associated with this job
  val customerId: Long
  // Uniquely identifies the job.
  val uniqueId: Long
  // Estimated time in milliseconds that the job will take to execute
  val duration: Int
  // Execute the job
  def execute(): Unit
}

/**
  * As jobs are launched by customers they are placed on this queue by an
  * external system, this interface provides the job runner the methods required
  * to retrieve those jobs.
  * Important: do not treat this as a fixed predefined collection of job, this
  * is meant to abstract the interaction between the JobRunner and the external
  * system that holds the customers requests.
  */
trait JobQueue {
  // Removes a job from the queue. If the queue has been drained,
  // this call will block until a new job becomes available
  def pop(): Job

  // Returns the current number of enqueued jobs, note that this
  // number might increase or decrease as new jobs are enqueued by
  // customer or are consumed by the JobRunner
  def length: Int
}

/**
  * This is interface that you will need to implement. A JobRunner encapsulates
 * the logic to schedule the execution of jobs.
  */
trait JobRunner {

  /**
    * Continuously consumes the job queue and executes the jobs,
    * execution continues forever until shutdown is invoked.
    */
  def run(jobQueue: JobQueue): Unit

  /**
   * Stops consuming jobs from the queue and blocks until
   * all jobs that have been dequeued have finished their execution.
   */
  def shutdown(): Unit
}
