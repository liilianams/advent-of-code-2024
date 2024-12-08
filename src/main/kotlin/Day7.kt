import java.math.BigInteger

fun main() {
  val input = getInputAsStrings("Day7")
    .map { it.split(":") }
    .map { (keyPart, valuesPart) ->
      val key = keyPart.trim()
      val values = valuesPart.trim().split(" ")
      key to values
    }

  solveDay7(input, listOf("+", "*"))
  solveDay7(input, listOf("+", "*", "||"))
}

private fun solveDay7(input: List<Pair<String, List<String>>>, operators: List<String>) {
  var result = BigInteger.ZERO

  for ((target, numbers) in input) {
    val solution = findSolution(target, numbers, operators)
    if (solution.isNotEmpty()) result += target.toBigInteger()
  }
  println(result)
}

private fun findSolution(target: String, numbers: List<String>, operators: List<String>): List<String> {
  if (numbers.isEmpty()) return listOf()

  val operatorCombinations = generateOperatorCombinations(numbers.size - 1, operators)

  for (combinations in operatorCombinations) {
    val calculation = buildCalculation(numbers, combinations)
    val result = evaluateCalculation(calculation)
    if (result == target.toBigInteger()) {
      return calculation
    }
  }

  return listOf()
}

private fun generateOperatorCombinations(numberOfNumbers: Int, operators: List<String>): List<List<String>> {
  val result = mutableListOf<List<String>>()
  generateCombinationsRecursive(numberOfNumbers, operators, emptyList(), result)
  return result
}

private fun generateCombinationsRecursive(
  numberOfNumbers: Int,
  operators: List<String>,
  currentCombination: List<String>,
  result: MutableList<List<String>>
) {
  if (currentCombination.size == numberOfNumbers) {
    result.add(currentCombination)
    return
  }

  for (op in operators) {
    val newCombination = currentCombination + op
    generateCombinationsRecursive(numberOfNumbers, operators, newCombination, result)
  }
}

private fun buildCalculation(numbers: List<String>, operators: List<String>): List<String> {
  val calculation = mutableListOf<String>()
  for (i in numbers.indices) {
    calculation.add(numbers[i])
    if (i < operators.size) {
      calculation.add(operators[i])
    }
  }
  return calculation
}

private fun evaluateCalculation(calculation: List<String>): BigInteger {
  var result = calculation[0].toBigInteger()

  for (i in 1 until calculation.size step 2) {
    val operator = calculation[i]
    val number = calculation[i + 1].toBigInteger()
    result = when (operator) {
      "+" -> result + number
      "*" -> result * number
      "||" -> BigInteger(result.toString() + number.toString())
      else -> throw IllegalArgumentException("Invalid operator: $operator")
    }
  }

  return result
}
