package com.pl.hragency.seeder

import java.nio.charset.StandardCharsets
import java.util.UUID

object Ids {
    fun of(type: String, vararg parts: Any): UUID =
        UUID.nameUUIDFromBytes(
            "$type:${parts.joinToString(":")}".toByteArray(StandardCharsets.UTF_8)
        )
}