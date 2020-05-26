FROM openjdk:8-alpine

# Required for starting application up.
RUN apk update && apk add bash nodejs npm python

#Backend
RUN mkdir -p /opt/app
ENV PROJECT_HOME /opt/app
ENV REVIEW_PATH $PROJECT_HOME/review.csv
ENV KEYWORD_PATH $PROJECT_HOME/keyword.txt

COPY build/libs/wechallenge-1.0.jar $PROJECT_HOME/wechallenge.jar
COPY src/main/resources/review.csv $REVIEW_PATH
COPY src/main/resources/keyword.txt $KEYWORD_PATH

#Fronend
RUN mkdir -p $PROJECT_HOME/web
COPY angularclient $PROJECT_HOME/angularclient
RUN chmod -R 777 /usr/lib/node_modules/
RUN cd $PROJECT_HOME/angularclient && npm install -g @angular/cli@1.7.4 && npm install

WORKDIR $PROJECT_HOME

CMD ["java", "-Djava.security.egd=file:/dev/./urandom","-jar", "./wechallenge.jar"]

