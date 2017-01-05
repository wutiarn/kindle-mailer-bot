FROM openjdk:8-alpine

RUN apk add --no-cache bash

ADD . /code
WORKDIR /code

RUN ./gradlew shadowJar
RUN cp build/dist/kindle-mailer-bot.jar .

CMD java -jar kindle-mailer-bot.jar