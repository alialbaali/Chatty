package com.apps.chatychaty.model

data class ErrorResponse(
  val status : Int,

  val errors : List<String>
)