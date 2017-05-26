package com.github.roberveral.mesosgocd.spec

import java.util.UUID

import mesosphere.mesos.protos.{RangesResource, Resource, ScalarResource}
import org.apache.mesos.Protos
import mesosphere.mesos.protos.Implicits._
import org.apache.mesos.Protos.{ContainerInfo, Image}
import org.apache.mesos.Protos.ContainerInfo.MesosInfo
import org.apache.mesos.Protos.Image.Docker

import scala.collection.JavaConverters._
import scala.language.implicitConversions

/**
  * Contains implicits conversions between the framework specification and
  * Mesos Protobufs.
  *
  * @author Rober Veral
  */
object Implicits {

  /**
    * Converts [[Resources]] into a Iterable[[Protos.Resource]]
    *
    * @param resources resources to convert
    * @return the converted protobuf
    */
  implicit def resourcesToProto(resources: Resources): java.util.List[Protos.Resource] = {
    val resourcesProto: List[Protos.Resource] = List(ScalarResource(Resource.CPUS, resources.cpus),
      ScalarResource(Resource.MEM, resources.mem),
      RangesResource(Resource.PORTS, resources.ports.map(port => mesosphere.mesos.protos.Range(port, port))))
    resourcesProto.asJava
  }

  /**
    * Converts [[Resources]] to a sequence of individual [[Resource]].
    *
    * @param resources resources to convert
    * @return the sequence of [[Resource]]
    */
  implicit def resourcesToResourceSeq(resources: Resources): Seq[Resource] =
    List(ScalarResource(Resource.CPUS, resources.cpus),
      ScalarResource(Resource.MEM, resources.mem),
      RangesResource(Resource.PORTS, resources.ports.map(port => mesosphere.mesos.protos.Range(port, port))))

  /**
    * Converts a [[Map]] into a [[Protos.Environment]] with environment variables.
    *
    * @param environment environment variables to convert
    * @return the converted protobuf
    */
  implicit def mapToEnvironment(environment: Map[String, String]): Protos.Environment =
    environment.foldLeft(Protos.Environment.newBuilder)((builder, variable) =>
      builder.addVariables(Protos.Environment.Variable.newBuilder().setName(variable._1).setValue(variable._2)))
      .build()

  /**
    * Converts a [[Command]] into a [[Protos.CommandInfo]]
    *
    * @param command command to convert
    * @return the converted protobuf
    */
  implicit def commandToProto(command: Command): Protos.CommandInfo =
    Protos.CommandInfo.newBuilder()
      .setValue(command.command)
      .setEnvironment(command.environment).build()

  /**
    * Converts a [[String]] with the image name into a [[Protos.ContainerInfo]].
    * It uses the MesosContainerizer and a Docker image.
    *
    * @param container image name to convert
    * @return the converted protobuf
    */
  implicit def containerToProto(container: String): Protos.ContainerInfo =
    Protos.ContainerInfo.newBuilder()
      .setType(ContainerInfo.Type.MESOS)
      .setMesos(MesosInfo.newBuilder()
        .setImage(Image.newBuilder()
          .setType(Protos.Image.Type.DOCKER)
          .setDocker(Docker.newBuilder().setName(container))))
      .build()

  /**
    * Converts a [[Task]] into a [[Protos.TaskInfo.Builder]]. A builder is returned
    * because until a slaveId of an offer is not added the TaskInfo message is not
    * complete.
    *
    * @param task task to convert
    * @return the converted protobuf
    */
  implicit def taskToProto(task: Task): Protos.TaskInfo.Builder =
    Protos.TaskInfo.newBuilder()
      .setName(task.name)
      .setTaskId(Protos.TaskID.newBuilder().setValue(s"${task.name}-${UUID.randomUUID()}"))
      .addAllResources(task.resources)
      .setCommand(task.command)
      .setContainer(task.image)

}
