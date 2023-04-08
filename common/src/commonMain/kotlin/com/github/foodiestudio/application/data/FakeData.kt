package com.github.foodiestudio.application.data

object FakeData {

    val captions: List<String> =
        "In video production, A-roll is the primary footage of a project’s main subject, while B-roll shots are supplemental footage. B-roll provides filmmakers with flexibility in the editing process and is often spliced together with A-roll footage to bolster the story, create dramatic tension, or further illustrate a point. Stories that rely entirely on A-roll footage might feel off-balance; this is why shooting B-roll is important.".split(
            " "
        )

    // milliseconds
    val bRollData: List<IntRange> = listOf(
        100..200, 300..500
    )

    val trackData = TrackData(trackData = listOf(bRollData, bRollData, bRollData, bRollData))

}

data class TrackData(
    val trackData: List<List<IntRange>>
)