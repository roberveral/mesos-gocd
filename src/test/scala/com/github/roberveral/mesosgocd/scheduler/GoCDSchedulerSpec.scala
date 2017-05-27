package com.github.roberveral.mesosgocd.scheduler

import com.github.roberveral.mesosgocd.spec.{Command, Resources, Task}
import mesosphere.mesos.protos.{FrameworkID, Offer, OfferID, SlaveID}
import org.scalatest.{FlatSpec, Matchers}
import mesosphere.mesos.protos.Implicits._
import com.github.roberveral.mesosgocd.spec.Implicits._
import org.apache.mesos.Protos

import scala.collection.JavaConverters._


/**
  * Created by roberveral on 27/05/17.
  */
class GoCDSchedulerSpec extends FlatSpec with Matchers {
  val offers: List[Protos.Offer] =
    List(Offer(OfferID("1"), FrameworkID("1"), SlaveID("1"), "test-hostname", Resources(1.0, 512.0), Seq(), Seq()),
      Offer(OfferID("2"), FrameworkID("1"), SlaveID("2"), "test-hostname2", Resources(4.0, 1024.0), Seq(), Seq()))

  "The scheudler" should "launch the configured task when it receives an offer with enough resources" in {
    val task = Task("test-task", "test-image", Command("ls"), Resources(1.0, 512.0))

    val scheduler = new GoCDScheduler(task)
    val driver = new TestSchedulerDriver
    scheduler.resourceOffers(driver, offers.asJava)

    driver.acceptedOfferIds.size shouldBe 1
    driver.acceptedOfferIds.head.getValue shouldBe "1"
    driver.launchedTasks.size shouldBe 1
    driver.launchedTasks.head.getSlaveId.getValue shouldBe "1"
    driver.launchedTasks.head.getName shouldBe "test-task"
    driver.declinedOfferIds.size shouldBe 1
  }

  it should "launch the configured task when it receives an offer with enough resources (not he first one)" in {
    val task = Task("test-task", "test-image", Command("ls"), Resources(3.0, 512.0))

    val scheduler = new GoCDScheduler(task)
    val driver = new TestSchedulerDriver
    scheduler.resourceOffers(driver, offers.asJava)

    driver.acceptedOfferIds.size shouldBe 1
    driver.acceptedOfferIds.head.getValue shouldBe "2"
    driver.launchedTasks.size shouldBe 1
    driver.launchedTasks.head.getSlaveId.getValue shouldBe "2"
    driver.launchedTasks.head.getName shouldBe "test-task"
    driver.declinedOfferIds.size shouldBe 1
  }

  it should "decline all offers when no one fits in the task resources" in {
    val task = Task("test-task", "test-image", Command("ls"), Resources(8.0, 10000.0))

    val scheduler = new GoCDScheduler(task)
    val driver = new TestSchedulerDriver
    scheduler.resourceOffers(driver, offers.asJava)

    driver.acceptedOfferIds.size shouldBe 0
    driver.launchedTasks.size shouldBe 0
    driver.declinedOfferIds.size shouldBe 2
  }

}
