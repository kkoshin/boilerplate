package com.github.foodiestudio.application

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MoshiTest {
    @Test
    fun `basic parse json`() {
       val json = """
           {
             "hidden_card": {
               "rank": "6",
               "suit": "SPADES"
             },
             "visible_cards": [
               {
                 "rank": "4",
                 "suit": "CLUBS"
               },
               {
                 "rank": "A",
                 "suit": "HEARTS"
               }
             ]
           }
       """.trimIndent()

        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<BlackjackHand> = moshi.adapter(BlackjackHand::class.java)
        // 文档错了？下面这个调用方式不太 work
//        val jsonAdapter: JsonAdapter<BlackjackHand> = moshi.adapter<BlackjackHand>()

        val blackjackHand = jsonAdapter.fromJson(json)
        assertEquals(blackjackHand!!.hidden_card.suit, Suit.SPADES)
    }

    @Test
    fun `parse json with custom adapter`() {
        val json = """
            {
              "hidden_card": "6S",
              "visible_cards": [
                "4C",
                "AH"
              ]
            }
        """.trimIndent()
        val adapter = Moshi.Builder().add(CardAdapter()).build().adapter(BlackjackHand::class.java)
        val data = adapter.fromJson(json)
        assertEquals(data!!.hidden_card.rank, "6")
        assertEquals(data.hidden_card.suit, Suit.SPADES)
    }
}

// 处理缩写
class CardAdapter {
    @ToJson
    fun toJson(card: Card): String {
        return card.rank + card.suit.name.substring(0, 1)
    }

    @FromJson
    fun fromJson(card: String): Card {
        val rank = card[0].toString()
        return when (card[1]) {
            'C' -> Card(rank, Suit.CLUBS)
            'D' -> Card(rank, Suit.DIAMONDS)
            'S' -> Card(rank, Suit.SPADES)
            'H' -> Card(rank, Suit.HEARTS)
            else -> throw JsonDataException("unkown suit: $card")
        }
    }
}
