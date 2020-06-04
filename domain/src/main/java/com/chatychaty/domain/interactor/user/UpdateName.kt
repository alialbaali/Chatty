package com.chatychaty.domain.interactor.user

import com.chatychaty.domain.repository.UserRepository

class UpdateName(private val userRepository: UserRepository) {

    suspend operator fun invoke(name: String) = userRepository.updateName(name)

}
