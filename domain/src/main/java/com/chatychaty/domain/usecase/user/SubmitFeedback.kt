package com.chatychaty.domain.interactor.user

import com.chatychaty.domain.repository.UserRepository

class SubmitFeedback(private val userRepository: UserRepository) {

    suspend operator fun invoke(stars: Int, feedback: String) = userRepository.submitFeedback(stars, feedback)

}