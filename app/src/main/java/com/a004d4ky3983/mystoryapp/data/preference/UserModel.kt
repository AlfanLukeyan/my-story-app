package com.a004d4ky3983.mystoryapp.data.preference

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)
