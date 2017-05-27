package com.github.roberveral.mesosgocd.spec

import org.scalatest.{FlatSpec, Matchers}

/**
  * Test for [[Task]]
  *
  * @author Rober Veral
  */
class TaskSpec extends FlatSpec with Matchers {

  "A Task" should "initialize properly all its attributes" in {
    val task = Task("test-task", "test-image", Command("test-command"), Resources(1.0, 512.0))

    task.name shouldBe "test-task"
    task.image shouldBe "test-image"
    task.command.command shouldBe "test-command"
    task.command.environment shouldBe Map()
    task.resources.cpus shouldBe 1.0
    task.resources.mem shouldBe 512.0
    task.resources.ports shouldBe Seq()
  }

}
