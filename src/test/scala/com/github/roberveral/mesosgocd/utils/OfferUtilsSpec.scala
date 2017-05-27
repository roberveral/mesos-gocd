package com.github.roberveral.mesosgocd.utils

import com.github.roberveral.mesosgocd.spec.{Command, Resources, Task}
import mesosphere.mesos.protos._
import org.scalatest.{FlatSpec, Matchers}

/**
  * Test for [[OfferUtils]]
  *
  * @author Rober Veral
  */
class OfferUtilsSpec extends FlatSpec with Matchers with OfferUtils {

  "checkResources" should "return true when the given scalar resource matches the offered one" in {
    checkResources(ScalarResource("cpus", 1.0), ScalarResource("cpus", 1.0)) shouldBe true
    checkResources(ScalarResource("cpus", 1.0), ScalarResource("cpus", 8.0)) shouldBe true
  }

  it should "return false when the scalar resource is of the same type, same name, but no enough amount" in {
    checkResources(ScalarResource("cpus", 2.0), ScalarResource("cpus", 1.0)) shouldBe false
  }

  it should "return false when the scalar resource names don't match" in {
    checkResources(ScalarResource("cpus", 1.0), ScalarResource("mem", 1.0)) shouldBe false
  }

  it should "return true when the given range resource matches the offered one" in {
    checkResources(RangesResource("ports", List(mesosphere.mesos.protos.Range(80, 80))),
      RangesResource("ports", List(mesosphere.mesos.protos.Range(80, 80)))) shouldBe true
    checkResources(RangesResource("ports", List(mesosphere.mesos.protos.Range(80, 80))),
      RangesResource("ports", List(mesosphere.mesos.protos.Range(0, 65539)))) shouldBe true
  }

  it should "return true when all the ranges are available in any range in the offer" in {
    checkResources(RangesResource("ports",
      List(mesosphere.mesos.protos.Range(80, 80), mesosphere.mesos.protos.Range(443, 443))),
      RangesResource("ports",
        List(mesosphere.mesos.protos.Range(80, 80), mesosphere.mesos.protos.Range(443, 443)))) shouldBe true

    checkResources(RangesResource("ports",
      List(mesosphere.mesos.protos.Range(80, 80), mesosphere.mesos.protos.Range(443, 443))),
      RangesResource("ports",
        List(mesosphere.mesos.protos.Range(40, 45), mesosphere.mesos.protos.Range(79, 500)))) shouldBe true
  }

  it should "return false when the range resource is of the same type, same name, but no enough amount" in {
    checkResources(RangesResource("ports", List(mesosphere.mesos.protos.Range(80, 80))),
      RangesResource("ports", List(mesosphere.mesos.protos.Range(30000, 31000)))) shouldBe false
  }

  it should "return false when the range resource names don't match" in {
    checkResources(RangesResource("ports", List(mesosphere.mesos.protos.Range(80, 80))),
      RangesResource("ips", List(mesosphere.mesos.protos.Range(80, 80)))) shouldBe false
  }

  it should "return true when the set resource offered contains all the entries" in {
    checkResources(SetResource("fruits", Set("apple", "banana")),
      SetResource("fruits", Set("apple", "banana"))) shouldBe true
    checkResources(SetResource("fruits", Set("apple")),
      SetResource("fruits", Set("apple", "banana"))) shouldBe true
  }

  it should "return false when the set resource not contains all the entries" in {
    checkResources(SetResource("fruits", Set("apple", "banana")),
      SetResource("fruits", Set("apple"))) shouldBe false
    checkResources(SetResource("fruits", Set("apple", "banana")),
      SetResource("fruits", Set("mango"))) shouldBe false
  }

  it should "return false when the set resource names don't match" in {
    checkResources(SetResource("fruits", Set("apple", "banana")),
      SetResource("animals", Set("lion", "cat"))) shouldBe false
  }

  it should "return false when the resource types don't match" in {
    checkResources(SetResource("fruits", Set("apple", "banana")), ScalarResource("cpus", 2.0)) shouldBe false
    checkResources(RangesResource("ports", List()), ScalarResource("cpus", 2.0)) shouldBe false
    checkResources(RangesResource("ports", List()), SetResource("fruits", Set("apple", "banana"))) shouldBe false
  }

  "checkOffer" should "return true when the Offer has enough resources for the task" in {
    import com.github.roberveral.mesosgocd.spec.Implicits._
    val task = Task("test-task", "test-image", Command("ls"), Resources(1.0, 512.0))
    val offer = Offer(OfferID("1"), FrameworkID("1"), SlaveID("1"), "test-hostname", Resources(1.0, 512.0), Seq(), Seq())
    checkOffer(task, offer) shouldBe true
  }

  it should "return false when the Offer hasn't enough resources for the task" in {
    import com.github.roberveral.mesosgocd.spec.Implicits._
    val task = Task("test-task", "test-image", Command("ls"), Resources(2.0, 1024.0))
    val offer = Offer(OfferID("1"), FrameworkID("1"), SlaveID("1"), "test-hostname", Resources(1.0, 512.0), Seq(), Seq())
    checkOffer(task, offer) shouldBe false
  }
}
