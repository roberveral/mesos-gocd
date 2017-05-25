package com.github.roberveral.mesosgocd.spec

/**
  * Defines a command to launch inside a task's container.
  *
  * @param command the command to run.
  * @param environment the environment variables to run the given
  *                    command with.
  */
case class Command(command: String,
                   environment: Map[String, String] = Map())
