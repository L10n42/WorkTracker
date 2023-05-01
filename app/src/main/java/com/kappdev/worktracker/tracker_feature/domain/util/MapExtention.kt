package com.kappdev.worktracker.tracker_feature.domain.util

fun Map<Int, Long>.padKeys(): Map<String, Long> {
    return this.mapKeys { entry ->
        entry.key.toString().padStart(2, '0')
    }
}

fun Map<Int, Long>.trimZeros(leaveOne: Boolean = true): Map<Int, Long> {
    var startIndex = 0
    var endIndex = this.size - 1

    fun isInMap(index: Int) = index in 0 until this.size

    for ((index, value) in this) {
        if (value != 0L) {
            val newIndex = index - 1
            startIndex = if (isInMap(newIndex) && leaveOne) newIndex else index
            break
        }
    }

    for ((index, value) in this.asIterable().reversed()) {
        if (value != 0L) {
            val newIndex = index + 1
            endIndex = if (isInMap(newIndex) && leaveOne) newIndex else index
            break
        }
    }

    return this.filterKeys { it in startIndex..endIndex }
}