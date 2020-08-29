package com.chatychaty.domain.interactor.user

import com.chatychaty.domain.model.User
import com.chatychaty.domain.repository.UserRepository


class SignIn(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: User) = userRepository.signIn(user)
}