private val directions = listOf(
  0 to -1,  // Up
  0 to 1,   // Down
  -1 to 0,  // Left
  1 to 0,   // Right
)

fun main() {
  val input = getInputAsStrings("Day10").map { it.split("").filter { it != "" } }

  solveDay10a(input)
  solveDay10b(input)
}

private fun solveDay10a(input: List<List<String>>) {
  val trailheads = findTrailheads(input)
  val result = trailheads.map { trailhead -> calculateTrailheadScore(trailhead, input) }
  println(result.sum())
}

private fun solveDay10b(input: List<List<String>>) {
  val trailheads = findTrailheads(input)
  val result = trailheads.map { trailhead -> calculateTrailheadRating(trailhead, input) }
  println(result.sum())
}

private fun calculateTrailheadScore(trailhead: Pair<Int, Int>, input: List<List<String>>): Int {
  val visited = mutableSetOf<Pair<Int, Int>>()
  val stack = mutableListOf(trailhead)
  var result = 0

  while (stack.isNotEmpty()) {
    val current = stack.removeLast()

    if (current in visited) continue
    visited.add(current)

    val (x, y) = current
    if (input[y][x] == "9") {
      result++
    }

    for (direction in directions) {
      val (currentX, currentY) = current
      val (dirX, dirY) = direction

      val next = currentX + dirX to currentY + dirY
      if (canTraverse(current, next, input) && next !in visited) {
        stack.add(next)
      }
    }
  }

  return result
}

private fun calculateTrailheadRating(trailhead: Pair<Int, Int>, input: List<List<String>>): Int {
  val visited = mutableSetOf<Pair<Int, Int>>()
  val stack = mutableListOf(trailhead)
  var result = 0

  while (stack.isNotEmpty()) {
    val current = stack.removeLast()
    result += trackUniqueTrail(current, input, visited)
  }

  return result
}

private fun trackUniqueTrail(
  current: Pair<Int, Int>,
  input: List<List<String>>,
  visited: MutableSet<Pair<Int, Int>>
): Int {
  val (x, y) = current

  if (input[y][x] == "9") {
    return 1
  }

  visited.add(current)
  var trailCount = 0

  for (direction in directions) {
    val (dirX, dirY) = direction
    val next = (x + dirX) to (y + dirY)

    if (canTraverse(current, next, input) && next !in visited) {
      trailCount += trackUniqueTrail(next, input, visited)
    }
  }

  visited.remove(current)
  return trailCount
}

private fun findTrailheads(input: List<List<String>>): List<Pair<Int, Int>> {
  return input.indices.flatMap { y -> input[y].indices.mapNotNull { x -> if (input[y][x] == "0") x to y else null } }
}

private fun canTraverse(current: Pair<Int, Int>, next: Pair<Int, Int>, input: List<List<String>>): Boolean {
  if (!isWithinGrid(next, input)) return false

  val (currentX, currentY) = current
  val (nextX, nextY) = next

  val currentValue = input[currentY][currentX].toIntOrNull()
  val nextValue = input[nextY][nextX].toIntOrNull()

  return nextValue != null && currentValue != null && nextValue == currentValue + 1
}

private fun isWithinGrid(point: Pair<Int, Int>, input: List<List<String>>): Boolean {
  val gridWidth = input.first().size
  val gridHeight = input.size
  val (x, y) = point
  return x in 0 until gridWidth && y in 0 until gridHeight
}

