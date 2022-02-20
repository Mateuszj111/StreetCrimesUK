FROM ubuntu:20.04

USER root

ARG SPARK_VERSION=3.2.1

RUN apt-get -y update && apt-get -y install openjdk-8-jdk && apt-get -y install curl && apt-get -y install wget && apt-get -y install vim && apt-get -y install unzip

RUN mkdir -p /opt && \
    cd /opt && \
    curl http://archive.apache.org/dist/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop3.2.tgz | \
        tar -zx && \
    ln -s spark-${SPARK_VERSION}-bin-hadoop3.2 spark

ENV PATH $PATH:/opt/spark/bin
ENV SPARK_HOME /opt/spark

# sbt installation
RUN apt-get install apt-transport-https curl gnupg -yqq sudo netcat
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | sudo tee /etc/apt/sources.list.d/sbt.list
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | sudo tee /etc/apt/sources.list.d/sbt_old.list
RUN curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | sudo -H gpg --no-default-keyring --keyring gnupg-ring:/etc/apt/trusted.gpg.d/scalasbt-release.gpg --import
RUN sudo chmod 644 /etc/apt/trusted.gpg.d/scalasbt-release.gpg
RUN sudo apt-get update -y && sudo apt-get install -y sbt

RUN useradd -ms /bin/bash docker


WORKDIR /opt/application
ARG WDIR=/opt/application



COPY project $WDIR/project
COPY src $WDIR/src
COPY build.sbt $WDIR/.
COPY download_jars.sh $WDIR/.
COPY entrypoint.sh $WDIR/.

#RUN wget https://policeuk-data.s3.amazonaws.com/download/465b4b07ef43a38796817fd22939ef32b23c216f.zip
#RUN unzip 465b4b07ef43a38796817fd22939ef32b23c216f.zip -d $WDIR/src/main/resources/
#RUN rm 465b4b07ef43a38796817fd22939ef32b23c216f.zip

RUN unzip $WDIR/src/main/resources/dataset.zip -d $WDIR/src/main/resources/
RUN rm $WDIR/src/main/resources/dataset.zip

RUN sh $WDIR/src/main/resources/rearrange.sh
RUN sh $WDIR/download_jars.sh

RUN sbt compile
RUN sbt package

ENTRYPOINT ["/opt/application/entrypoint.sh"]