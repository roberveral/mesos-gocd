# mesos-gocd [![Build Status](https://travis-ci.org/roberveral/mesos-gocd.svg?branch=master)](https://travis-ci.org/roberveral/mesos-gocd)

This project is a simple "Hello World" like example n how to get started developing a Mesos
framework. The idea of using [GoCD] is because it has a simple distributed architecture 
(one master, many agents), it has official Docker images and it's not much complicated
to set-up.

The first code is inaccurate and has some problems which need to be resolved. The idea,
 as a complement to the *"A travel through Mesos"* talk, is to **complete the framework 
 using issues and pull requests**, allowing to put in practice some Mesos concepts and
 learn more aspects.
 
The way to colaborate will be using *GitHub Projects*. There will be some tasks/issues to
be done, and if you want to collaborate you can comment/ask and assign the task to 
yourself in order to do that code. Once finished, a PR must be launched against the 
master branch and will be discussed and reviewed by other participants.

If you want to do some feature that is not registered yet, just open an issue, and if
it makes sense it will be added and assigned.

## Build the project

```
mvn clean package
```

## Run the project

```
docker run --rm --net=host roberveral/mesos-gocd
```

(*NOTE:* if you want to run the program from the command line or from an IDE, you must
download the [Libmesos library](https://downloads.mesosphere.com/libmesos-bundle/libmesos-bundle-1.9.0-rc1-1.2.0-rc1-1.tar.gz) 
and ensure that the native library is available in the LD_LIBRARY_PATH environment variable).

## Mesos

[Mesos] is a resource manager which allows to program against a datacenter like if it
was a single machine.

In the Mesos documentation there are info about the Mesos features and how to use them:
http://mesos.apache.org/documentation/latest/.

The most useful resource for developing a framework is the details of the protobuf used
as messages with Mesos: https://github.com/apache/mesos/blob/1.2.0/include/mesos/mesos.proto.

### Test environment

The repository contains a Docker Compose to start Mesos 1.2 with one master, one agent,
a Zookeeper node.

```
docker-compose -f src/test/resources/mesos-compose.yml up
```

You can use the Mesos connection: zk://localhost:2181


## GoCD architecture

[GoCD] is an open-source continuous delivery tool. It's composed of a **master** node,
which serves a Web-UI and orchestrates the work to do, and one-to-many **agent** nodes,
 which perform the concrete pipeline jobs (building projects using Maven, for instance).
 
The documentation about the tool can be found here: <https://docs.gocd.org/current/>.

### Docker images

[GoCD] provides official Docker images for the master and agent nodes, wich are
available in the Docker Hub. Also, a README in their GitHub repository explains how
to configure each one using environment variables:

- **Master**: <https://github.com/gocd/docker-gocd-server>
- **Agent**: <https://github.com/gocd/docker-gocd-agent-ubuntu-16.04>

[GoCD]: https://www.gocd.org/
[Mesos]: https://mesos.apache.org
