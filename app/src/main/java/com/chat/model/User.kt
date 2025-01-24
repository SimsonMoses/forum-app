package com.chat.model

class User {
    var userId: String = ""
    var name: String = ""
    var email: String = ""
    var avatar: String = ""
    lateinit var category: List<String>
}