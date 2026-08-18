package com.pl.hragency.seeder

import org.postgresql.core.BaseConnection
import org.postgresql.copy.CopyManager
import java.io.Reader
import java.sql.Connection

class StreamingCopy(
    private val connection: Connection
) {
    fun copy(
        table: String,
        columns: String,
        reader: Reader
    ): Long {
        val baseConnection = connection.unwrap(BaseConnection::class.java)
        val copyManager = CopyManager(baseConnection)

        val sql = """
            COPY $table ($columns)
            FROM STDIN WITH (FORMAT csv)
        """.trimIndent()

        return copyManager.copyIn(sql, reader)
    }
}
