import kotlin.math.abs

fun main() {
    val input = getInputAsStrings("Day1")

    solveDay1a(input)
    solveDay1b(input)
}

private fun solveDay1a(input: List<String>) {
    val (firstIds, secondIds) = getLocationsAsTwoLists(input)

    val sortedFirstIds = firstIds.sorted()
    val sortedSecondIds = secondIds.sorted()

    val result = sortedFirstIds.zip(sortedSecondIds).sumOf { (first, second) -> abs(first - second) }

    println(result)
}

private fun solveDay1b(input: List<String>) {
    val (firstIds, secondIds) = getLocationsAsTwoLists(input)

    val secondListIdCountMap = secondIds.groupingBy { it }.eachCount()

    val result = firstIds.sumOf { it.times(secondListIdCountMap[it] ?: 0) }

    println(result)
}

private fun getLocationsAsTwoLists(input: List<String>) = input.map {
    val values = it.split("\\s+".toRegex()).map(String::toInt)
    values[0] to values[1]
}.unzip()