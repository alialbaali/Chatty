package com.chatychaty.domain.interactor.user

import com.chatychaty.domain.repository.UserRepository

class SetTheme(private val userRepository: UserRepository) {

    suspend operator fun invoke(value: String) = userRepository.setTheme(value)

}
