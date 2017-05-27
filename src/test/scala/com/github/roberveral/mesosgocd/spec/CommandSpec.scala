package com.github.roberveral.mesosgocd.spec

import org.scalatest.{FlatSpec, Matchers}

/**
  * Test spec for [[Command]].
  *
  * @author Rober Veral
  */
class CommandSpec extends FlatSpec with Matchers {
  "A command" should "initialize its environment as an empty map" in {
    val command = Command("ls")

    command.command shouldBe "ls"
    command.environment shouldBe Map()
  }

  it should "add environment variables when it's initialized with a map" in {
    val command = Command("ls", Map("PATH" -> "/usr/bin"))

    command.environment.isEmpty shouldBe false
    command.environment.get("PATH") shouldBe Some("/usr/bin")
    command.environment.get("HOME") shouldBe None
  }
}
