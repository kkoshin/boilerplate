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
    val bRollData: List<EffectData> = listOf(
        EffectData(10..20), EffectData(30..50)
    )

    val trackGroupData = TrackData(trackData = listOf(bRollData, bRollData, bRollData, bRollData))
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
