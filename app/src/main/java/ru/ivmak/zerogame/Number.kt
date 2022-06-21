package ru.ivmak.zerogame

data class Number(
    val id: Int,
    val number: Int,
    val color: Int,
    val onClick: () -> Unit
)
