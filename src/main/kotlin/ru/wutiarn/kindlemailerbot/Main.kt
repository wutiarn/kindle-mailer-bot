package ru.wutiarn.kindlemailerbot

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.TelegramBotAdapter
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.GetFile
import com.pengrad.telegrambot.request.GetUpdates
import com.pengrad.telegrambot.request.SendMessage
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*
import java.util.concurrent.TimeUnit
import javax.activation.DataHandler
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart
import javax.mail.util.ByteArrayDataSource

val telegramToken: String = getEnv("TELEGRAM_TOKEN")
val kindleEmail: String = getEnv("KINDLE_EMAIL")
val smtpHost: String = getEnv("SMTP_HOST")
val smtpEmail: String = getEnv("SMTP_EMAIL")
val smtpPassword: String = getEnv("SMTP_PASSWORD")


val httpClient: OkHttpClient = OkHttpClient().newBuilder()
        .readTimeout(65, TimeUnit.SECONDS)
        .build()
val bot: TelegramBot = TelegramBotAdapter.buildCustom(telegramToken, httpClient)

val props: Properties
    get() {
        val p = Properties()
        p["mail.transport.protocol"] = "smtp"
        p["mail.smtp.host"] = smtpHost
        p["mail.smtp.port"] = 587
        p["mail.smtp.auth"] = true
        p["mail.smtp.starttls.enable"] = true
        return p
    }

val session: Session = Session.getInstance(props, object : Authenticator() {
    override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(smtpEmail, smtpPassword)
    }
})


fun main(args: Array<String>) {
    var lastUpdateId = 0
    while (true) {
        for (update in bot.execute(GetUpdates().timeout(60).offset(lastUpdateId)).updates()) {
            lastUpdateId = update.updateId() + 1
            val message = update.message()
            message?.document()?.let { processMessage(message) }
        }
    }
}

fun processMessage(msg: Message) {
    val chatId = msg.chat().id()

    if (chatId !in setOf<Long>(43457173)) {
        bot.execute(SendMessage(chatId, "You can't do this."))
        return
    }

    val file = bot.execute(GetFile(msg.document().fileId())).file()
    val fileUrl = bot.getFullFilePath(file)

    val fileName = msg.document().fileName()
    bot.execute(SendMessage(chatId, "Sending $fileName..."))

    val getFileRequest = Request.Builder()
            .url(fileUrl)
            .build()

    val response = httpClient.newCall(getFileRequest)
            .execute()

    val data = response.body().bytes()

    sendMessage(data, fileName)

    bot.execute(SendMessage(chatId, "$fileName sent."))
}

fun sendMessage(data: ByteArray, filename: String) {
    val message = MimeMessage(session)
    message.setFrom("me@wutiarn.ru")
    message.addRecipients(javax.mail.Message.RecipientType.TO, kindleEmail)

    val multipart = MimeMultipart()

    val bodyPart = MimeBodyPart()
    bodyPart.dataHandler = DataHandler(ByteArrayDataSource(data, "application/x-mobipocket-ebook"))
    bodyPart.fileName = filename

    multipart.addBodyPart(bodyPart)
    message.setContent(multipart)

    Transport.send(message)
}

fun getEnv(name: String): String = System.getenv(name)
        ?: throw IllegalArgumentException("You must set $name env before start")