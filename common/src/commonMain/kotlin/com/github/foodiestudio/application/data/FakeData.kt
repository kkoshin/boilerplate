package com.github.foodiestudio.application.data

import androidx.compose.ui.graphics.painter.Painter

object FakeData {

    private val testData =
        "In video production, A-roll is the primary footage of a project’s main subject, while B-roll shots are supplemental footage. B-roll provides filmmakers with flexibility in the editing process and is often spliced together with A-roll footage to bolster the story, create dramatic tension, or further illustrate a point. Stories that rely entirely on A-roll footage might feel off-balance; this is why shooting B-roll is important."

    private var pts = 0L
    val captions: List<CaptionBlock> = testData.split(" ", "\n").map {
        CaptionBlock(it, pts, it.length * 10).also { p ->
            pts += p.duration
        }
    }

    // milliseconds
    private val bRollData1: List<EffectData> = listOf(
        EffectData.StyledText(30L..80L),
        EffectData.StyledText(190L..320L),
    )
    private val bRollData2: List<EffectData> = listOf(
        EffectData.StyledText(0L..40L),
        EffectData.StyledText(70L..120L),
    )

    val trackGroupData = listOf(
        Track(bRollData1, TrackType.StyledText),
        Track(bRollData2, TrackType.StyledText),
    )
}

sealed class EffectData(
    val duration: LongRange
) {
    class StyledText(duration: LongRange, val text: String = "Sample") : EffectData(duration)
    class Gif(val img: Painter, duration: LongRange) : EffectData(duration)
    class Sticker(val img: Painter, duration: LongRange) : EffectData(duration)
    class Picture(val img: Painter, duration: LongRange) : EffectData(duration)
    class Audio(val text: String, duration: LongRange) : EffectData(duration)
}

enum class TrackType {
    StyledText, Gif, Sticker, Picture, Audio
}

data class Track(
    val track: List<EffectData>,
    val trackType: TrackType,
)

typealias TrackData = List<Track>

data class CaptionBlock(
    val text: String,
    val pts: Long,
    val duration: Int,
    val isSilence: Boolean = false
)

fun CaptionBlock.calculatePts(fraction: Float): Long = pts + (duration * fraction).toLong()
