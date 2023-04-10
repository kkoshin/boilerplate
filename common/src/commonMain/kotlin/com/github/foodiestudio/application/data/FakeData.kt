package com.github.foodiestudio.application.data

object FakeData {

    private val testData =
        "In video production, A-roll is the primary footage of a project’s main subject, while B-roll shots are supplemental footage. B-roll provides filmmakers with flexibility in the editing process and is often spliced together with A-roll footage to bolster the story, create dramatic tension, or further illustrate a point. Stories that rely entirely on A-roll footage might feel off-balance; this is why shooting B-roll is important."

    private var pts = 0
    val captions: List<CaptionBlock> = testData.split(" ", "\n").map {
        CaptionBlock(it, pts, it.length * 10).also { p ->
            pts += p.duration
        }
    }

    // milliseconds
    private val bRollData1: List<EffectData> = listOf(
        EffectData(30..80),
        EffectData(190..320),
    )
    private val bRollData2: List<EffectData> = listOf(
        EffectData(0..40),
        EffectData(70..120),
    )

    val trackGroupData = TrackData(trackData = listOf(bRollData1, bRollData2))
}

data class EffectData(
    val duration: IntRange,
    val type: String = "gif"
)

data class TrackData(
    val trackData: List<List<EffectData>>
)

data class CaptionBlock(
    val text: String,
    val pts: Int,
    val duration: Int,
    val isSilence: Boolean = false
)

fun CaptionBlock.calculatePts(fraction: Float): Int = pts + (duration * fraction).toInt()
