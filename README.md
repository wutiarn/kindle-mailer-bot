# Telegram bot that forwards files to @kindle email (send to kindle)
Currently there is no mechanism for linking telegram accounts to @kindle.com emails (and for email verification too). Therefore you have to create a new bot for every single @kindle email. Feel free to fork this project and modify it for your needs.

However, pull requests are appreciated (i.e. if someone write above-mentioned features with mongodb integration).

## Usage
This application is dockerized. If you haven't heard anything about docker, please start with reading [Docker Overview](https://docs.docker.com/engine/understanding-docker/) and [Docker Compose Overview](https://docs.docker.com/compose/overview/)

There is an [example docker compose configuration](https://gitlab.com/wutiarn/kindle-mailer-bot/blob/master/docker-compose.example.yml). To run this container you have to specify some environment variables:

| Environment variable| Description                                                                         |
| ------------------- | ----------------------------------------------------------------------------------- |
| TELEGRAM_TOKEN      | Token, got from @BotFather bot (i.e. 258003162:AAFTKcGoZdgGrHJnArtCZtqaiQ8SiI6VPxw) |
| SMTP_HOST           | i.e. smtp.yandex.ru                                                                 |
| SMTP_EMAIL          | i.e. example@yandex.ru                                                              |
| SMTP_PASSWORD       | Password to connect to smtp server                                                  |
| KINDLE_EMAIL        | your send-to-kindle email (i.e. example@kindle.com)                                 |
