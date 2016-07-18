FROM java:8-jdk

ADD . /code
WORKDIR /code

RUN ./gradlew shadowJar
RUN cp build/libs/kindle-mailer-bot.jar .

CMD java -jar kindle-mailer-bot.jar