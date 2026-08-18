package com.pl.hragency.seeder

import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

object Csv {
    fun value(value: Any?): String {
        if (value == null) return ""
        val text = when (value) {
            is OffsetDateTime -> value.truncatedTo(ChronoUnit.MILLIS).toString()
            else -> value.toString()
        }
        return "\"" + text.replace("\"", "\"\"") + "\""
    }

    fun row(vararg values: Any?): String =
        values.joinToString(",") { value(it) } + "\n"

    fun jsonArray(vararg values: String): String =
        values.joinToString(
            prefix = "[",
            postfix = "]",
            separator = ","
        ) { "\"${it.replace("\"", "\\\\\"")}\"" }

    fun jsonObject(vararg values: Pair<String, String>): String =
        values.joinToString(
            prefix = "{",
            postfix = "}",
            separator = ","
        ) { (key, value) -> "\"$key\":\"${value.replace("\"", "\\\\\"")}\"" }

    fun now(seed: Int, offset: Long): OffsetDateTime {
        val base = OffsetDateTime.of(
            2024, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC
        )

        val secondsInTwoYears = 2L * 365 * 24 * 60 * 60

        val seconds = Math.floorMod(
            seed.toLong() * 1_000L + offset,
            secondsInTwoYears
        )

        return base.plusSeconds(seconds)
    }
}