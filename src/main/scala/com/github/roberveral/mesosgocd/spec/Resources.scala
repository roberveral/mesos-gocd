package com.github.roberveral.mesosgocd.spec

/**
  * Defines the resource requirements of a task.
  *
  * @param cpus number of cpu cores required.
  * @param mem memory size allocated.
  * @param ports ports needed (all ports used should be reserved).
  */
case class Resources(cpus: Double,
                     mem: Double,
                     ports: Seq[Int] = Seq())
