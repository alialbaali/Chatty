package com.chatychaty.domain.interactor.user

import com.chatychaty.domain.repository.UserRepository

class GetUser(private val userRepository: UserRepository) {

    suspend operator fun invoke() = userRepository.getUser()

}
