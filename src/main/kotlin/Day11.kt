import java.math.BigInteger

private const val NUMBER_OF_BLINKS = 25

fun main() {
  val input = getInputAsStrings("Day11").flatMap { it.split(" ").map { it.toBigInteger() } }

  solveDay11(input)
}

private fun solveDay11(input: List<BigInteger>) {
  var result = ArrayDeque(input)
  var round = 0;

  repeat(NUMBER_OF_BLINKS) {
    val tempResult = ArrayDeque<BigInteger>()
    println(round++)

    while (result.isNotEmpty()) {
      val stone = result.removeFirst()
      when {
        stone == BigInteger.ZERO -> tempResult.add(BigInteger.ONE)
        hasEvenNumberOfDigits(stone) -> {
          val (first, second) = splitStoneIntoTwo(stone)
          tempResult.add(first)
          tempResult.add(second)
        }
        else -> tempResult.add(multiplyBy2024(stone))
      }
    }

    result = tempResult
  }

  println(result.size)
}

private fun splitStoneIntoTwo(stone: BigInteger): Pair<BigInteger, BigInteger> {
  val length = stone.toString().length
  val halfLength = length / 2

  val divisor = BigInteger.TEN.pow(halfLength)

  val first = stone / divisor
  val second = stone % divisor

  return Pair(first, second)
}

private fun multiplyBy2024(stone: BigInteger): BigInteger {
  return stone * BigInteger.valueOf(2024L)
}

private fun hasEvenNumberOfDigits(int: BigInteger): Boolean {
  return int.toString().length % 2 == 0
}

