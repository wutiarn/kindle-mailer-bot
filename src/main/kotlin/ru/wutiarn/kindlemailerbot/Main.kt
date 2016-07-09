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


val httpClient: OkHttpClient = OkHttpClient().newBuilder()
        .readTimeout(65, TimeUnit.SECONDS)
        .build()
val bot: TelegramBot = TelegramBotAdapter.buildCustom("268003162:AAFTKcGoZdgGrHJnArtCZtqoiQ8SiI2VPxw", httpClient)


val props: Properties
    get() {
        val p = Properties()
        p["mail.transport.protocol"] = "smtp"
        p["mail.smtp.host"] = "smtp.yandex.ru"
        p["mail.smtp.port"] = 587
        p["mail.smtp.auth"] = true
        p["mail.smtp.starttls.enable"] = true
        return p
    }
val session: Session = Session.getInstance(props, object: Authenticator() {
    override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication("me@wutiarn.ru", "ziybsgdbbfqporvo")
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
    val file = bot.execute(GetFile(msg.document().fileId())).file()
    val fileUrl = bot.getFullFilePath(file)
    println("URL: $fileUrl")

    bot.execute(SendMessage(chatId, "Sending ${msg.document().fileName()}..."))

    val getFileRequest = Request.Builder()
            .url(fileUrl)
            .build()

    val response = httpClient.newCall(getFileRequest)
            .execute()

    val data = response.body().bytes()

    val extension = fileUrl.split(".").last()

    sendMessage(data, extension)

    bot.execute(SendMessage(chatId, "${msg.document().fileName()} sent."))
}

fun sendMessage(data: ByteArray, extension: String) {
    val message = MimeMessage(session)
    message.setFrom("me@wutiarn.ru")
    message.addRecipients(javax.mail.Message.RecipientType.TO, "wutiarn@gmail.com")

    val multipart = MimeMultipart()

    val bodyPart = MimeBodyPart()
    bodyPart.dataHandler = DataHandler(ByteArrayDataSource(data, "application/x-mobipocket-ebook"))
    bodyPart.fileName = "upload.$extension"

    multipart.addBodyPart(bodyPart)
    message.setContent(multipart)

    Transport.send(message)
}