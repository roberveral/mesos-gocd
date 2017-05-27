package com.github.roberveral.mesosgocd.spec

import org.scalatest.{FlatSpec, Matchers}

/**
  * Test for [[Resources]]
  *
  * @author Rober Veral
  */
class ResourcesSpec extends FlatSpec with Matchers {

  "Resources" should "initialize with no ports when not given" in {
    val resources = Resources(1.0, 2.0)

    resources.cpus shouldBe 1.0
    resources.mem shouldBe 2.0
    resources.ports.isEmpty shouldBe true
  }

  it should "add the ports when it's initialized with a list of them" in {
    val resources = Resources(1.0, 2.0, Seq(80, 443))

    resources.ports shouldBe Seq(80, 443)
  }
}
