FROM openjdk:8

ADD https://downloads.mesosphere.com/libmesos-bundle/libmesos-bundle-1.9.0-rc1-1.2.0-rc1-1.tar.gz ./

RUN tar -xvf /libmesos-bundle-1.9.0-rc1-1.2.0-rc1-1.tar.gz

ENV LD_LIBRARY_PATH=/libmesos-bundle/lib:$LD_LIBRARY_PATH

COPY mesos-gocd-*-jar-with-dependencies*.jar /mesos-gocd.jar

CMD ["java", "-jar", "/mesos-gocd.jar"]