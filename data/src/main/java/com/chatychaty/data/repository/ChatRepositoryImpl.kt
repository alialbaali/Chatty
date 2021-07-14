package com.chatychaty.data.repository

import com.chatychaty.data.asBearerToken
import com.chatychaty.data.request
import com.chatychaty.domain.model.Chat
import com.chatychaty.domain.repository.ChatRepository
import com.chatychaty.local.ChatDao
import com.chatychaty.local.UserDao
import com.chatychaty.remote.chat.RemoteChat
import com.chatychaty.remote.chat.RemoteChatDataSource
import com.chatychaty.remote.message.SignalRHub
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ChatRepositoryImpl(
    private val remoteDataSource: RemoteChatDataSource,
    private val chatDao: ChatDao,
    userDao: UserDao,
    override val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val signalRHub: SignalRHub,
) : ChatRepository {

    private val token: Flow<String> = userDao.getUserToken()

    override fun getChats(): Flow<List<Chat>> = chatDao.getChats()

    override fun getArchivedChats(): Flow<List<Chat>> = chatDao.getArchivedChats()

    override fun getChatById(chatId: String): Flow<Chat> = chatDao.getChatById(chatId)

    override suspend fun createChat(username: String): Result<Chat> = request(dispatcher) {
        remoteDataSource.createChat(token.asBearerToken(), username)
    }.mapCatching { it.toDomainChat() }
        .onSuccess {
            withContext(dispatcher) {
                chatDao.insertChat(it)
            }
        }

    override suspend fun archiveChat(chatId: String) = withContext(dispatcher) { chatDao.archiveChat(chatId) }

    override suspend fun unarchiveChat(chatId: String) = withContext(dispatcher) { chatDao.unarchiveChat(chatId) }

    override suspend fun pinChat(chatId: String) = withContext(dispatcher) {
        chatDao.pinChat(chatId)
    }

    override suspend fun unpinChat(chatId: String) = withContext(dispatcher) {
        chatDao.unpinChat(chatId)
    }

    override suspend fun muteChat(chatId: String) = withContext(dispatcher) {
        chatDao.muteChat(chatId)
    }

    override suspend fun unmuteChat(chatId: String) = withContext(dispatcher) {
        chatDao.unmuteChat(chatId)
    }

    override suspend fun refreshChats() {
        request(dispatcher) { remoteDataSource.getChats(token.asBearerToken()) }
            .onSuccess { chats ->
                chats.map {
                    val localChat = chatDao.getChatById(it.chatId)
                        .firstOrNull()

                    it.toDomainChat(localChat?.isArchived ?: false, localChat?.isPinned ?: false, localChat?.isMuted ?: false)
                }
                    .forEach { chatDao.insertChat(it) }
            }
    }

    override suspend fun syncChats() = withContext(dispatcher) {
        signalRHub.collectChat { chat ->
            runBlocking(dispatcher) {
                chatDao.updateChats(listOf(chat.toDomainChat()))
            }
        }
    }

    override suspend fun deleteChats() = withContext(dispatcher) { chatDao.deleteChats() }

    private fun RemoteChat.toDomainChat(isArchived: Boolean = false, isPinned: Boolean = false, isMuted: Boolean = false): Chat {
        return Chat(
            chatId,
            profile.username,
            profile.displayName,
            profile.photoURL,
            isArchived = isArchived,
            isPinned = isPinned,
            isMuted = isMuted,
        )
    }

}