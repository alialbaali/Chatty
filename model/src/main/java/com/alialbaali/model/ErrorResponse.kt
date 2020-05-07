package com.alialbaali.model

data class ErrorResponse(
  val status : Int,

  val errors : List<String>
)