package com.yhproject.mywiki.domain.user

interface UserRepository {
    fun findById(id: Long): User?
    fun findByEmail(email: String): User?
    fun save(user: User): User
    fun existsById(userId: Long): Boolean
}