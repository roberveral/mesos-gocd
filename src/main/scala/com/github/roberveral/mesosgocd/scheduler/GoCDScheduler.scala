package com.github.roberveral.mesosgocd.scheduler

import java.util

import com.github.roberveral.mesosgocd.spec.Task
import com.github.roberveral.mesosgocd.utils.OfferUtils
import org.apache.mesos.Protos
import org.apache.mesos.{Scheduler, SchedulerDriver}

import mesosphere.mesos.protos.Implicits._
import com.github.roberveral.mesosgocd.spec.Implicits._
import scala.collection.JavaConverters._

/**
  * Scheduler for the GoCD framework.
  *
  * @author Rober Veral
  */
class GoCDScheduler(task: Task) extends Scheduler with OfferUtils {
  override def offerRescinded(schedulerDriver: SchedulerDriver, offerID: Protos.OfferID): Unit = {}

  override def disconnected(schedulerDriver: SchedulerDriver): Unit = {}

  override def reregistered(schedulerDriver: SchedulerDriver, masterInfo: Protos.MasterInfo): Unit = {}

  override def slaveLost(schedulerDriver: SchedulerDriver, slaveID: Protos.SlaveID): Unit = {}

  override def error(schedulerDriver: SchedulerDriver, s: String): Unit = {}

  override def statusUpdate(schedulerDriver: SchedulerDriver, taskStatus: Protos.TaskStatus): Unit =
    println(s"received status update $taskStatus")

  override def frameworkMessage(schedulerDriver: SchedulerDriver,
                                executorID: Protos.ExecutorID,
                                slaveID: Protos.SlaveID,
                                bytes: Array[Byte]): Unit = {}

  override def resourceOffers(schedulerDriver: SchedulerDriver, list: util.List[Protos.Offer]): Unit = {
    // https://github.com/apache/mesos/blob/1.2.0/include/mesos/mesos.proto
    val receivedOffers = list.asScala
    // Select a valid offer for the task to run
    val optionalAcceptOffer = receivedOffers.find(checkOffer(task, _))
    // If there is a valid offer, get the remaining offers
    val remainingOffers = receivedOffers diff optionalAcceptOffer.toList
    // Launche the task by accepting the offer
    optionalAcceptOffer.foreach(offer =>
      schedulerDriver.launchTasks(List(offer.getId).asJava,
        List(taskToProto(task).setSlaveId(offer.getSlaveId).build()).asJava))
    // Decline the other offers
    remainingOffers foreach(offer => schedulerDriver.declineOffer(offer.getId))
  }

  override def registered(schedulerDriver: SchedulerDriver,
                          frameworkID: Protos.FrameworkID,
                          masterInfo: Protos.MasterInfo): Unit =
    println(s"Framework registered with ID: $frameworkID")

  override def executorLost(schedulerDriver: SchedulerDriver,
                            executorID: Protos.ExecutorID,
                            slaveID: Protos.SlaveID,
                            i: Int): Unit = {}
}
