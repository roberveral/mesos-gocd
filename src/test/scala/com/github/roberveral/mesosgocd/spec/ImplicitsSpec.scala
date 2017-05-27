package com.github.roberveral.mesosgocd.spec

import mesosphere.mesos.protos.Resource
import org.apache.mesos.Protos
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConverters._

/**
  * Test for [[Implicits]]
  *
  * @author Rober Veral
  */
class ImplicitsSpec extends FlatSpec with Matchers {
  import Implicits._

  "A Command" should "implicitly convert to a Protos.CommandInfo properly configured" in {
    val commandInfo: Protos.CommandInfo = Command("ls", Map("PATH" -> "/usr/bin", "HOME"-> "/home/test"))

    commandInfo.getValue shouldBe "ls"
    commandInfo.getEnvironment.getVariablesCount shouldBe 2
    commandInfo.getEnvironment.getVariables(0).getName shouldBe "PATH"
    commandInfo.getEnvironment.getVariables(0).getValue shouldBe "/usr/bin"
    commandInfo.getEnvironment.getVariables(1).getName shouldBe "HOME"
    commandInfo.getEnvironment.getVariables(1).getValue shouldBe "/home/test"
  }

  it should "implicitly convert to a Protos.CommandInfo with empty environment when not given" in {
    val commandInfo: Protos.CommandInfo = Command("ls")

    commandInfo.getValue shouldBe "ls"
    commandInfo.getEnvironment.getVariablesCount shouldBe 0
  }

  "Resources" should "implicitly convert to a util.List[Protos.Resource] properly configured" in {
    val resourceList: java.util.List[Protos.Resource] = Resources(1.0, 512.0, Seq(80, 443))

    resourceList.asScala.find(_.getName == Resource.CPUS).get.getScalar.getValue shouldBe 1.0
    resourceList.asScala.find(_.getName == Resource.MEM).get.getScalar.getValue shouldBe 512.0
    resourceList.asScala.find(_.getName == Resource.PORTS).get.getRanges.getRangeCount shouldBe 2
    resourceList.asScala.find(_.getName == Resource.PORTS).get.getRanges.getRange(0).getBegin shouldBe 80
    resourceList.asScala.find(_.getName == Resource.PORTS).get.getRanges.getRange(0).getEnd shouldBe 80
    resourceList.asScala.find(_.getName == Resource.PORTS).get.getRanges.getRange(1).getBegin shouldBe 443
    resourceList.asScala.find(_.getName == Resource.PORTS).get.getRanges.getRange(1).getEnd shouldBe 443
  }

  it should "implicitly convert to a util.List[Protos.Resource] with empty PORT ranges when not ports set" in {
    val resourceList: java.util.List[Protos.Resource] = Resources(1.0, 512.0)

    resourceList.asScala.find(_.getName == Resource.CPUS).get.getScalar.getValue shouldBe 1.0
    resourceList.asScala.find(_.getName == Resource.MEM).get.getScalar.getValue shouldBe 512.0
    resourceList.asScala.find(_.getName == Resource.PORTS).get.getRanges.getRangeCount shouldBe 0
  }

  "A Task" should "implicitly convert to a Protos.TaskInfo.Builder properly configured with no slaveId set" in {
    val taskInfo: Protos.TaskInfo.Builder = Task("test-task", "test-image", Command("ls"), Resources(1.0, 512.0))
    val resourceList = taskInfo.getResourcesList

    taskInfo.getName shouldBe "test-task"
    taskInfo.getTaskId.getValue.startsWith("test-task") shouldBe true
    taskInfo.getSlaveId.isInitialized shouldBe false
    taskInfo.getContainer.getType shouldBe Protos.ContainerInfo.Type.MESOS
    taskInfo.getContainer.getMesos.getImage.getType shouldBe Protos.Image.Type.DOCKER
    taskInfo.getContainer.getMesos.getImage.getDocker.getName shouldBe "test-image"
    taskInfo.getCommand.getValue shouldBe "ls"
    resourceList.asScala.find(_.getName == Resource.CPUS).get.getScalar.getValue shouldBe 1.0
    resourceList.asScala.find(_.getName == Resource.MEM).get.getScalar.getValue shouldBe 512.0
    resourceList.asScala.find(_.getName == Resource.PORTS).get.getRanges.getRangeCount shouldBe 0
  }
}
