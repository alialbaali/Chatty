package com.chatychaty.domain.interactor.user

import com.chatychaty.domain.repository.UserRepository


class UpdatePhoto(private val userRepository: UserRepository) {

    suspend operator fun invoke(byteArray: ByteArray, filename: String) = userRepository.updateImage(byteArray, filename)
}
