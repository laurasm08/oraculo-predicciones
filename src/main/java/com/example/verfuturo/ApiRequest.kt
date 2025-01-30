package com.example.verfuturo

data class ApiRequest(
    val model: String = "open-mistral-nemo",
    val max_tokens: Int,
    val messages: List<Message>,
    val temperature: Double ? = null
)

