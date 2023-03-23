package com.example.bretterverse.chatroom

data class Message(
    val senderId: String = "",
    val senderName: String = "",
    val text: String = "",
    val timestamp: Long = 0L
)
