package com.github.roberveral.mesosgocd

import com.github.roberveral.mesosgocd.scheduler.GoCDScheduler
import com.github.roberveral.mesosgocd.spec.{Command, Resources, Task}
import mesosphere.mesos.protos.FrameworkInfo
import org.apache.mesos.MesosSchedulerDriver
import mesosphere.mesos.protos.Implicits._

/**
  * @author Rober Veral
  */
object Main extends App {
  val framework = FrameworkInfo("mesos-gocd")

  val gocdServerTask =
    Task("gocd-server",
      "gocd/gocd-server:v17.3.0",
      Command("/docker-entrypoint.sh"),
      Resources(1.0, 512.0, Seq(8153, 8154)))

  val scheduler = new GoCDScheduler(gocdServerTask)

  val driver = new MesosSchedulerDriver(scheduler, framework, "zk://localhost:2181/mesos")
  driver.run()
}
