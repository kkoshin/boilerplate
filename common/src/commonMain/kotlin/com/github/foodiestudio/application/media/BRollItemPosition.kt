package com.github.foodiestudio.application.media

data class BRollItemPosition(
    val offset: Int,
    val width: Int
) {
    companion object {
        val Unspecific = BRollItemPosition(0, 0)
    }
}