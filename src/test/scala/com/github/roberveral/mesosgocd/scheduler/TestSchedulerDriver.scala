package com.github.roberveral.mesosgocd.scheduler

import java.util

import org.apache.mesos.Protos.Offer.Operation
import org.apache.mesos.Protos._
import org.apache.mesos.SchedulerDriver

import scala.collection.JavaConverters._

/**
  * An implementation of an [[SchedulerDriver]] for testing schedulers.
  *
  * @author Rober Veral
  */
class TestSchedulerDriver extends SchedulerDriver {
  var acceptedOfferIds: List[OfferID] = List()
  var declinedOfferIds: List[OfferID] = List()
  var launchedTasks: List[TaskInfo] = List()

  override def declineOffer(offerID: OfferID, filters: Filters): Status = {
    declinedOfferIds = offerID :: declinedOfferIds
    null
  }

  override def declineOffer(offerID: OfferID): Status = {
    declinedOfferIds = offerID :: declinedOfferIds
    null
  }

  override def launchTasks(collection: util.Collection[OfferID], collection1: util.Collection[TaskInfo], filters: Filters): Status = {
    acceptedOfferIds = acceptedOfferIds ++ collection.asScala
    launchedTasks = launchedTasks ++ collection1.asScala
    null
  }

  override def launchTasks(collection: util.Collection[OfferID], collection1: util.Collection[TaskInfo]): Status = {
    acceptedOfferIds = acceptedOfferIds ++ collection.asScala
    launchedTasks = launchedTasks ++ collection1.asScala
    null
  }

  override def launchTasks(offerID: OfferID, collection: util.Collection[TaskInfo], filters: Filters): Status = {
    acceptedOfferIds = offerID :: acceptedOfferIds
    launchedTasks = launchedTasks ++ collection.asScala
    null
  }

  override def launchTasks(offerID: OfferID, collection: util.Collection[TaskInfo]): Status = {
    acceptedOfferIds = offerID :: acceptedOfferIds
    launchedTasks = launchedTasks ++ collection.asScala
    null
  }

  override def stop(b: Boolean): Status = ???

  override def stop(): Status = ???

  override def killTask(taskID: TaskID): Status = ???

  override def acceptOffers(collection: util.Collection[OfferID], collection1: util.Collection[Operation], filters: Filters): Status = ???

  override def acknowledgeStatusUpdate(taskStatus: TaskStatus): Status = ???

  override def requestResources(collection: util.Collection[Request]): Status = ???

  override def sendFrameworkMessage(executorID: ExecutorID, slaveID: SlaveID, bytes: Array[Byte]): Status = ???

  override def join(): Status = ???

  override def reconcileTasks(collection: util.Collection[TaskStatus]): Status = ???

  override def reviveOffers(): Status = ???

  override def suppressOffers(): Status = ???

  override def run(): Status = ???

  override def abort(): Status = ???

  override def start(): Status = ???
}
