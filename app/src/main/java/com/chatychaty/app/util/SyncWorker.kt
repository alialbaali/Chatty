package com.chatychaty.app.util

import android.app.NotificationManager
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.chatychaty.domain.repository.ChatRepository
import com.chatychaty.domain.repository.MessageRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class SyncWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters), KoinComponent {

    private val messageRepository by inject<MessageRepository>()
    private val chatRepository by inject<ChatRepository>()
    private val notificationManager by lazy { context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    override suspend fun doWork(): Result {
        messageRepository.connectHub()
        messageRepository.syncMessages()
        messageRepository.getNewMessages()
            .map { messages ->
                messages
                    .groupBy { message -> message.chatId }
                    .mapKeys { chatRepository.getChatById(it.key).first() }
            }
            .distinctUntilChanged()
            .collect {
                it.forEach { (chat, messages) ->
                    notificationManager.createNotification(context, chat, messages, false)
                }
            }
        return Result.success()
    }

}