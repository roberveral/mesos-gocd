package com.github.roberveral.mesosgocd.utils

import com.github.roberveral.mesosgocd.spec.Task
import mesosphere.mesos.protos._
import com.github.roberveral.mesosgocd.spec.Implicits._

/**
  * Trait with utils to deal with Mesos Offers and [[Task]].
  *
  * @author Rober Veral
  */
trait OfferUtils {

  /**
    * Checks if the task can be launched in the given offer. It will check if for all the
    * task's requested resources exists any resource in the offer which matches the requirement.
    *
    * @param task task to check.
    * @param offer offer to validate.
    * @return true if the offer can launch the task, false in other case.
    */
  def checkOffer(task: Task, offer: Offer): Boolean =
    task.resources.forall(resource =>
      offer.resources.exists(offeredResource => checkResources(resource, offeredResource)))

  /**
    * Compares a resource with an offered one to check if the resoure fits in the offered one
    * (there is enough resources). A check can only be true if both resources are of the same
    * type, has the same name and enough amount.
    *
    * @param resource resource to check.
    * @param offeredResource resource contained in the offer.
    * @return true if the offered resource has enough resources of
    *         the same type and name, false in other case.
    */
  def checkResources(resource: Resource, offeredResource: Resource): Boolean =
    (resource, offeredResource) match {
      case (ScalarResource(name1, value1, _), ScalarResource(name2, value2, _)) if name1 == name2 =>
        value1 <= value2

      case (RangesResource(name1, ranges1, _), RangesResource(name2, ranges2, _)) if name1 == name2 =>
        ranges1.forall(range => ranges2.exists(range2 =>
          (range2.begin to range2.end).containsSlice(range.begin to range.end)))

      case (SetResource(name1, set1, _), SetResource(name2, set2, _)) if name1 == name2 =>
        set1.forall(set2.contains)

      case _ => false
  }
}
