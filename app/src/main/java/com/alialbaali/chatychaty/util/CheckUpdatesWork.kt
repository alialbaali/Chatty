package com.alialbaali.chatychaty.util
//
//import android.content.Context
//import androidx.work.Worker
//import androidx.work.WorkerParameters
//import com.alialbaali.chatychaty.getApplicationScope
//import com.apps.chatychaty.database.AppDatabase
//import com.apps.chatychaty.network.DAOs
//import com.apps.chatychaty.network.Repos
//import com.alialbaali.chatychaty.getToken
//import kotlinx.coroutines.launch
//import retrofit2.HttpException
//
// class CheckUpdatesWork(
//    context: Context,
//    workerParameters: WorkerParameters
//) :
//    Worker(context, workerParameters) {
//
//    init {
//        DAOs.chatDao = AppDatabase.getInstance(context).chatDao
//        DAOs.messageDao = AppDatabase.getInstance(context).messageDao
//    }
//
//    private val chatRepository = Repos.chatRepository
//    private val messageRepository = Repos.messageRepository
//
//    companion object {
//        const val WORK_NAME = "CheckUpdatesWorker"
//    }
//
//    override fun doWork(): Result {
//        return try {
//
//            applicationScope.launch {
//
//                chatRepository.checkUpdates().let {
//
//                    if (it.chatUpdate) {
//                        updateChats()
//                    }
//
//                    if (it.messageUpdate) {
//                        updateMessages()
//                    }
//
//                }
//
//            }
//
//            Result.success()
//
//        } catch (e: HttpException) {
//
//            Result.retry()
//
//        }
//    }
//
//    private suspend fun updateChats() {
//        chatRepository.getChatsClient(token!!).let { chats ->
//
//            chatRepository.updateChatsDao(chats)
//
//        }
//    }
//
//    private suspend fun updateMessages() {
//        messageRepository.getMessagesClient(token!!, messageRepository.countDao())
//            .let { messages ->
//                messageRepository.updateMessagesDao(messages)
//            }
//    }
//}