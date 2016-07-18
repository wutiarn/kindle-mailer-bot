# Telegram bot that forwards files to @kindle email (send to kindle)
Currently there is no mechanism for linking telegram accounts to @kindle.com emails (and for email verification too). Therefore you have to create a new bot for every single @kindle email. Feel free to fork this project and modify it for your needs.

However, pull requests are appreciated (i.e. if someone write above-mentioned features with mongodb integration).

## Usage
Docker compose is a preferable way to run everything up. Just get the [example configuration](https://gitlab.com/wutiarn/kindle-mailer-bot/blob/master/docker-compose.example.yml), set environment variables, rename it to `docker-compose.yml` and run `docker compose up -d`

**Note:** If you haven't heard anything about Docker or Docker Compose, please start with reading [Docker Overview](https://docs.docker.com/engine/understanding-docker/) and [Docker Compose Overview](https://docs.docker.com/compose/overview/)

## Environment variables description

| Variable            | Description                                                                         |
| ------------------- | ----------------------------------------------------------------------------------- |
| TELEGRAM_TOKEN      | Token, got from @BotFather bot (i.e. 258003162:AAFTKcGoZdgGrHJnArtCZtqaiQ8SiI6VPxw) |
| SMTP_HOST           | i.e. smtp.yandex.ru                                                                 |
| SMTP_EMAIL          | i.e. example@yandex.ru                                                              |
| SMTP_PASSWORD       | Password to connect to smtp server                                                  |
| KINDLE_EMAIL        | your send-to-kindle email (i.e. example@kindle.com)                                 |
