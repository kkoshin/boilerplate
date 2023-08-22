package com.github.foodiestudio.application

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import junit.framework.TestCase.assertEquals
import org.junit.Test

class MoshiTest {
    @Test
    fun `to json`() {
        val json = "{\n" +
                "  \"hidden_card\": {\n" +
                "    \"rank\": \"6\",\n" +
                "    \"suit\": \"SPADES\"\n" +
                "  },\n" +
                "  \"visible_cards\": [\n" +
                "    {\n" +
                "      \"rank\": \"4\",\n" +
                "      \"suit\": \"CLUBS\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"rank\": \"A\",\n" +
                "      \"suit\": \"HEARTS\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"

        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<BlackjackHand> = moshi.adapter(BlackjackHand::class.java)
        // 文档错了？下面这个调用方式不太 work
//        val jsonAdapter: JsonAdapter<BlackjackHand> = moshi.adapter<BlackjackHand>()

        val blackjackHand = jsonAdapter.fromJson(json)
        assertEquals(blackjackHand!!.hidden_card.suit, Suit.SPADES)
    }
}
