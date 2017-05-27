package com.github.roberveral.mesosgocd.spec

/**
  * Defines a task that's run by the framework.
  *
  * @param name name of the task to run.
  * @param image Docker image for the task container.
  * @param command command to launch in the task container (usually the defined entrypoint).
  * @param resources the resources required by the task.
  */
case class Task(name: String,
                image: String,
                command: Command,
                resources: Resources)
