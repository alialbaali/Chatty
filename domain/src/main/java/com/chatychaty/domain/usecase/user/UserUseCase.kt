package com.chatychaty.domain.interactor.user

class UserUseCase(
    val signUp: SignUp,
    val signIn: SignIn,
    val updatePhoto: UpdatePhoto,
    val updateName: UpdateName,
    val signOut: SignOut,
    val getUser: GetUser,
    val getChatUser: GetChatUser,
    val getTheme: GetTheme,
    val setTheme: SetTheme,
    val submitFeedback: SubmitFeedback,
    val deleteAccount: DeleteAccount
)