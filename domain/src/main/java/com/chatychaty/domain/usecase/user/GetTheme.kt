package com.chatychaty.domain.interactor.user

import com.chatychaty.domain.repository.UserRepository

class GetTheme(private val userRepository: UserRepository) {

    suspend operator fun invoke() = userRepository.getTheme()

}
