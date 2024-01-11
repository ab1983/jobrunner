package com.workday.scala

import org.scalatest._

/**
 * These are integration tests that cover the basic requirements of a JobRunner,
 * the NaiveJobRunner does not meet all of them so some of these tests are ignored.
 * Your implementation of JobRunner should pass this tests, feel free to copy
 * this class and adapt it to your solution.
 */
class NaiveJobRunnerSpec extends FlatSpec with Matchers {

  it should "eventually execute all the jobs" in {
    // There are 5 jobs of 100ms each = 500ms of cpu time
    val originalJobs = List.fill(5)(NaiveJob())
    val testQueue = NaiveJobQueue(originalJobs)
    val runningThread = new Thread() {
      override def run(): Unit = {
        new NaiveJobRunner().run(testQueue)
      }
    }
    runningThread.start()
    Thread.sleep(1000) // 1s is enough to execute all jobs even for the naive implementation
    testQueue.length shouldBe 0
    originalJobs.forall(_.executed) shouldBe true
  }

  // Fails in the naive implementation
  ignore should "execute jobs while meeting performance requirements" in {
    // There are 20 jobs of 100ms each = 2s of cpu time
    val originalJobs = List.fill(20)(NaiveJob())
    val testQueue = NaiveJobQueue(originalJobs)
    new Thread() {
      override def run(): Unit = {
        new NaiveJobRunner().run(testQueue)
      }
    }.start()
    Thread.sleep(1000) // Only 1s wait should be enough for all jobs to be executed
    originalJobs.forall(_.executed) shouldBe true
  }

  // Fails in the naive implementation
  ignore should "execute jobs while meeting fairness requirements" in {
    val customerIds = 1 to 100
    // There are 100000 jobs of 100ms each
    val jobs = customerIds.map(customerId => List.fill(1000)(NaiveJob(customerId = customerId))).flatten
    val testQueue = NaiveJobQueue(jobs.toList)
    new Thread() {
      override def run(): Unit = {
        new NaiveJobRunner().run(testQueue)
      }
    }.start()
    Thread.sleep(10000) // This should be enough to execute about 10% of the jobs on a modern pc
    jobs.groupBy(_.customerId).foreach {
      case (_, jobs) =>
        // For every customer there should be at least 1 executed job
        jobs.count(job => job.executed) shouldBe >(0)
    }
  }

  it should "shutdown gracefully" in {
    val testQueue = NaiveJobQueue(List.fill(5)(NaiveJob()))
    val jobRunner = new NaiveJobRunner()
    val runningThread = new Thread() {
      override def run(): Unit = {
        jobRunner.run(testQueue)
      }
    }
    runningThread.start()
    jobRunner.shutdown()
    testQueue.length shouldBe >(0)
    runningThread.isAlive shouldBe false
  }

}
