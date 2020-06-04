package com.chatychaty.domain.model

data class ErrorResponse(
  val status : Int,

  val errors : List<String>
)