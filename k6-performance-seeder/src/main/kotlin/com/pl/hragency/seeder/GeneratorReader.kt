package com.pl.hragency.seeder

import java.io.Reader

class GeneratorReader(
    private val generator: Sequence<String>
) : Reader() {

    private val iterator = generator.iterator()
    private var buffer = ""
    private var position = 0

    override fun read(cbuf: CharArray, off: Int, len: Int): Int {
        require(off >= 0 && len >= 0 && off + len <= cbuf.size)

        if (len == 0) return 0

        while (position >= buffer.length) {
            if (!iterator.hasNext()) return -1
            buffer = iterator.next()
            position = 0
        }

        val count = minOf(len, buffer.length - position)
        buffer.toCharArray(cbuf, off, position, position + count)
        position += count
        return count
    }

    override fun close() {
    }
}