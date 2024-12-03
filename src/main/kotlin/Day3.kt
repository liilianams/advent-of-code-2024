private val MULTIPLICATION_REGEX = Regex("""mul\(\d+,\d+\)""")
private val DO_REGEX = Regex("""do\(\)""")
private val DONT_REGEX = Regex("""don't\(\)""")

fun main() {
  val input = getInputAsStrings("Day3")

  solveDay3a(input)
  solveDay3b(input)
}

private fun solveDay3a(input: List<String>) {
  val joinedInput = input.joinToString()

  val instructions = Regex("(?=mul\\()").split(joinedInput)

  val result = instructions.mapNotNull {
    MULTIPLICATION_REGEX.find(it)?.let { instruction ->
      val values = parseValuesFromInstruction(instruction.groupValues[0])
      multiply(values[0], values[1])
    }
  }.sum()

  println(result)
}

private fun solveDay3b(input: List<String>) {
  val joinedInput = input.joinToString()

  val instructions = Regex("(?=mul)|(?=don't\\(\\))|(?=do\\(\\))").split(joinedInput)
  var shouldMultiply = true;

  val result = instructions.mapNotNull {
    when {
      DO_REGEX.find(it) != null -> shouldMultiply = true
      DONT_REGEX.find(it) != null -> shouldMultiply = false
    }

    MULTIPLICATION_REGEX.find(it)?.let { instruction ->
      val values = parseValuesFromInstruction(instruction.groupValues[0])
      if (shouldMultiply) multiply(values[0], values[1]) else 0
    }
  }.sum()

  println(result)
}

private fun multiply(value1: String, value2: String) = value1.toInt() * value2.toInt()

private fun parseValuesFromInstruction(instruction: String) = instruction
    .replace("mul(", "")
    .replace(")", "")
    .split(",")