import kotlin.math.pow

private const val NUMBER_OF_BLINKS = 75 // use 25 to solve part A and 75 to solve part B

fun main() {
  val input = getInputAsStrings("Day11").flatMap { it.split(" ").map { it.toLong() } }
  solveDay11(input)
}

private fun solveDay11(input: List<Long>) {
  var result = input.groupingBy { it }.eachCount().mapValues { it.value.toLong() }

  repeat(NUMBER_OF_BLINKS) {
    val tempResult = mutableMapOf<Long, Long>()

    for ((stone, count) in result) {
      when {
        stone == 0L -> {
          tempResult.merge(1L, count, Long::plus)
        }

        hasEvenNumberOfDigits(stone) -> {
          val (first, second) = splitStoneIntoTwo(stone)
          tempResult.merge(first, count, Long::plus)
          tempResult.merge(second, count, Long::plus)
        }

        else -> {
          val newStone = multiplyBy2024(stone)
          tempResult.merge(newStone, count, Long::plus)
        }
      }
    }

    result = tempResult
  }

  println(result.values.sum())
}

private fun splitStoneIntoTwo(stone: Long): Pair<Long, Long> {
  val halfLength = stone.toString().length / 2

  val divisor = 10.0.pow(halfLength).toLong()

  val first = stone / divisor
  val second = stone % divisor

  return Pair(first, second)
}

private fun multiplyBy2024(stone: Long): Long {
  return stone * 2024L
}

private fun hasEvenNumberOfDigits(stone: Long): Boolean {
  return stone.toString().length % 2 == 0
}
