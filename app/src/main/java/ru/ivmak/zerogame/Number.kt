package ru.ivmak.zerogame

data class Number(
    val id: Int,
    val number: Int,
    val onClick: () -> Unit
)
