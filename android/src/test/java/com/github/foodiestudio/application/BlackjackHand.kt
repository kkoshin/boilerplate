package com.github.foodiestudio.application

// 使用动态反射的话，不需要用到这个 @JsonClass

//@JsonClass(generateAdapter = true)
class BlackjackHand(
    val hidden_card: Card
)

//@JsonClass(generateAdapter = true)
class Card(
    val rank: String,
    val suit: Suit
)

enum class Suit {
    CLUBS, DIAMONDS, HEARTS, SPADES;
}