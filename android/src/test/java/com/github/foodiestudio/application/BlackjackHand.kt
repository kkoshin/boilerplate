package com.github.foodiestudio.application

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class BlackjackHand(
    val hidden_card: Card
)

@JsonClass(generateAdapter = true)
class Card(
    val suit: Suit
)

enum class Suit {
    CLUBS, DIAMONDS, HEARTS, SPADES;
}