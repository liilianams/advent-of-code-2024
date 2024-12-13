private const val BUTTON_A_COST = 3
private const val BUTTON_B_COST = 1
private const val PRIZE_ADDITION = 10_000_000_000_000L // Use 0 for part 1

fun main() {
  val input = getInputAsStrings("Day13")

  solveDay13(input, PRIZE_ADDITION)
}

fun solveDay13(input: List<String>, prizeAddition: Long) {
  val machines = toClawMachines(input, prizeAddition)

  var result = 0L

  for (machine in machines) {
    val minCost = calculateMinimumCost(machine)
    if (minCost != null) {
      result += minCost
    }
  }

  println(result)
}

private fun calculateMinimumCost(machine: ClawMachine): Long? {
  val (buttonAX, buttonAY) = machine.buttonA
  val (buttonBX, buttonBY) = machine.buttonB
  val (prizeX, prizeY) = machine.prize

  // Sample equation:
  // 94a + 22b = 8400  (buttonAX * a + buttonBX * b = prizeX)
  // 43a + 67b = 5400  (buttonAY * a + buttonBY * b = prizeY)

  // Determinant: det = (94 * 67) - (34 * 22)
  val determinant = buttonAX * buttonBY - buttonAY * buttonBX

  if (determinant == 0L) return null

  // a = ((8400 * 67) - (5400 * 22)) / determinant
  // b = ((5400 * 94) - (8400 * 34)) / determinant
  val xA = (prizeX * buttonBY - prizeY * buttonBX) / determinant
  val xB = (prizeY * buttonAX - prizeX * buttonAY) / determinant

  // Final check that equation is satisfied
  // 8400 == (94a + 22b)
  // 5400 == (34a + 67b)
  if (
    xA < 0 ||
    xB < 0 ||
    prizeX != buttonAX * xA + buttonBX * xB ||
    prizeY != buttonAY * xA + buttonBY * xB
  ) {
    return null
  }

  return xA * BUTTON_A_COST + xB * BUTTON_B_COST
}

private fun toClawMachines(input: List<String>, prizeAddition: Long): List<ClawMachine> {
  val machines = mutableListOf<ClawMachine>()
  var buttonA: Pair<Long, Long>? = null
  var buttonB: Pair<Long, Long>? = null
  var prize: Pair<Long, Long>? = null

  for (line in input) {
    if (line.isBlank()) continue

    when {
      line.startsWith("Button A:") -> {
        buttonA = parseMovementAndPrize(line.substringAfter("Button A: "), "+")
      }

      line.startsWith("Button B:") -> {
        buttonB = parseMovementAndPrize(line.substringAfter("Button B: "), "+")
      }

      line.startsWith("Prize:") -> {
        prize = parseMovementAndPrize(line.substringAfter("Prize: "), "=")
      }
    }

    if (buttonA != null && buttonB != null && prize != null) {
      machines.add(ClawMachine(buttonA, buttonB, prize.first + prizeAddition to prize.second + prizeAddition))
    }
  }

  return machines
}

private fun parseMovementAndPrize(string: String, operator: String): Pair<Long, Long> {
  val parts = string.split(", ")
  val x = parts[0].split(operator)[1].toLong()
  val y = parts[1].split(operator)[1].toLong()
  return Pair(x, y)
}

data class ClawMachine(
  val buttonA: Pair<Long, Long>,
  val buttonB: Pair<Long, Long>,
  val prize: Pair<Long, Long>
)
