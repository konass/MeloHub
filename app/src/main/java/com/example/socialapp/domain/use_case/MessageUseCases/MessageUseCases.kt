package com.example.socialapp.domain.use_case.MessageUseCases

data class MessageUseCases (
    val sentMessage: SentMessage,
    val getSenderId: GetSenderId,
val getMessages: GetMessages,
val getUser: GetUser,
val getLatestMessage: GetLatestMessage,
val getReceiverData: GetReceiverData,
val saveChat: SaveChat)