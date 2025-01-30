package com.example.verfuturo

data class ApiResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)
