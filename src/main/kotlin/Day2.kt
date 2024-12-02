private const val MIN_CHANGE = 1
private const val MAX_CHANGE = 3

private enum class Direction {
    INCREASING, DECREASING
}

fun main() {
    val input = getInputAsStrings("Day2")

    solveDay2a(input)
    solveDay2b(input)
}

private fun solveDay2a(input: List<String>) {
    val result = input.count { it ->
        val values = it.split("\\s+".toRegex()).map(String::toInt)
        isValidChange(values)
    }

    println(result)
}

private fun solveDay2b(input: List<String>) {
    val result = input.count { it ->
        val values = it.split("\\s+".toRegex()).map(String::toInt)

        val canBeMadeSafe = values.indices.any { indexToRemove ->
            val modifiedValues = values.toMutableList()
            modifiedValues.removeAt(indexToRemove)
            isValidChange(modifiedValues)
        }

        isValidChange(values) || canBeMadeSafe
    }

    println(result)
}

private fun isValidChange(levels: List<Int>): Boolean {
    if (levels.size < 2) return true

    val direction = getDirection(levels[0], levels[1])

    return levels.zipWithNext().all { (a, b) ->
        checkDiff(direction, a - b)
    }
}

private fun checkDiff(direction: Direction, diff: Int) =
    if (direction == Direction.INCREASING) {
        diff in MIN_CHANGE..MAX_CHANGE
    } else {
        diff in -MAX_CHANGE..-MIN_CHANGE
    }

private fun getDirection(val1: Int, val2: Int) =
    if (val1 - val2 > 0) {
        Direction.INCREASING
    } else {
        Direction.DECREASING
    }