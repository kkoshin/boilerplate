package com.github.foodiestudio.application

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import org.junit.Test

@OptIn(ExperimentalStdlibApi::class)
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

        val moshi = Moshi.Builder()
            // 支持动态反射的方式，不加这个的话，则需要 ksp + @JsonClass 才能正确解析
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter = moshi.adapter<BlackjackHand>()

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
        val adapter = Moshi.Builder()
            .add(CardAdapter())
            .addLast(KotlinJsonAdapterFactory())
            .build()
            .adapter(BlackjackHand::class.java)
        val data = adapter.fromJson(json)
        assertEquals(data!!.hidden_card.rank, "6")
        assertEquals(data.hidden_card.suit, Suit.SPADES)
    }

    // 处理 json -> Event 的过程中，会自动尝试 json -> EventJson -> Event 的过程
    @Test
    fun `parse json with custom adapter not string`() {
        val json = """
            {
              "title": "Blackjack tournament",
              "begin_date": "20151010",
              "begin_time": "17:04"
            }
        """.trimIndent()
        val adapter = Moshi.Builder().add(EventJsonAdapter())
            .build()
            .adapter<Event>()

        val event = adapter.fromJson(json)
        assertEquals(event!!.title, "Blackjack tournament")
        assertEquals(event!!.beginDateAndTime, "20151010 17:04")
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

class EventJsonAdapter {
    @FromJson
    fun eventFromJson(eventJson: EventJson): Event {
        return Event(
            title = eventJson.title,
            beginDateAndTime = "${eventJson.begin_date} ${eventJson.begin_time}"
        )
    }

    @ToJson
    fun eventToJson(event: Event): EventJson {
        return EventJson(
            title = event.title,
            begin_date = event.beginDateAndTime.substring(0, 8),
            begin_time = event.beginDateAndTime.substring(9, 14)
        )
    }
}
