package com.github.foodiestudio.application

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import java.util.Locale

class ComposeColorAdapter {

    @FromJson
    fun fromHexColor(argb: String): Color {
        return runCatching { Color(android.graphics.Color.parseColor(argb)) }.getOrDefault(Color.Unspecified)
    }

    @ToJson
    fun toHexColor(color: Color): String {
        return "#" + Integer.toHexString(color.toArgb()).uppercase(Locale.ROOT).padStart(8, '0')
    }
}

// 区分普通的 int 类型
@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class HexColor

class Rectangle(
    @Json(ignore = true) // or @Transient
    val size: Int,
    @HexColor val color: Int
)

class ColorAdapter {
    @ToJson
    fun toJson(@HexColor rgb: Int): String {
        return "#%06x".format(rgb)
    }

    @FromJson
    @HexColor
    fun fromJson(rgb: String): Int {
        return rgb.substring(1).toInt(16)
    }
}