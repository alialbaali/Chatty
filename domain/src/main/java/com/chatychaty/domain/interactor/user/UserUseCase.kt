package com.chatychaty.domain.interactor.user

import com.chatychaty.domain.model.User

class UserUseCase(
    val signUp: SignUp,
    val signIn: SignIn,
    val updatePhoto: UpdatePhoto,
    val updateName: UpdateName,
    val signOut: SignOut,
    val getUser: GetUser,
    val getChatUser: GetChatUser,
    val getThemeValue: GetThemeValue,
    val setThemeValue: SetThemeValue
)