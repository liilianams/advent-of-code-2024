import MovementDirection.*

private const val OBSTACLE = "#"
private const val GUARD = "^"

private enum class MovementDirection {
  UP, DOWN, LEFT, RIGHT
}

fun main() {
  val input = getInputAsStrings("Day6").map { it.split("").filter { it != "" } }

  solveDay6a(input)
  solveDay6b(input)
}

private fun solveDay6a(input: List<List<String>>) {
  println(findVisitedPositions(input).size)
}

private fun solveDay6b(input: List<List<String>>) {
  val result = mutableSetOf<Pair<Int, Int>>()
  val visitedPositions = findVisitedPositions(input)

  for (position in visitedPositions) {
    if (input[position.first][position.second] != OBSTACLE && causesLoop(input, position)) {
      result.add(position)
    }
  }

  println(result.size)
}

private fun causesLoop(input: List<List<String>>, obstruction: Pair<Int, Int>): Boolean {
  val visitedPositions = mutableSetOf<Triple<Int, Int, MovementDirection>>()
  val modifiedInput = input.map { it.toMutableList() }
  modifiedInput[obstruction.first][obstruction.second] = OBSTACLE

  var currentPosition = findGuardStartingPosition(input)
  var direction = UP

  while (true) {
    val currentState = Triple(currentPosition.first, currentPosition.second, direction)

    if (currentState in visitedPositions) return true

    visitedPositions.add(currentState)

    val (x, y) = currentPosition

    val nextPosition = when (direction) {
      UP -> x - 1 to y
      RIGHT -> x to y + 1
      DOWN -> x + 1 to y
      LEFT -> x to y - 1
    }

    if (isOutOfBounds(modifiedInput, nextPosition)) {
      break
    }

    if (modifiedInput[nextPosition.first][nextPosition.second] == OBSTACLE) {
      direction = turnRight(direction)
      continue
    }

    currentPosition = nextPosition
  }

  return false
}

private fun findVisitedPositions(input: List<List<String>>): Set<Pair<Int, Int>> {
  val result = mutableSetOf<Pair<Int, Int>>()
  var currentPosition = findGuardStartingPosition(input)
  var direction = UP

  result.add(currentPosition)

  while (true) {
    val (x, y) = currentPosition

    val nextPosition = when (direction) {
      UP -> x - 1 to y
      RIGHT -> x to y + 1
      DOWN -> x + 1 to y
      LEFT -> x to y - 1
    }

    if (isOutOfBounds(input, nextPosition)) {
      break
    }

    if (input[nextPosition.first][nextPosition.second] == OBSTACLE) {
      direction = turnRight(direction)
      continue
    }

    currentPosition = nextPosition
    result.add(currentPosition)
  }

  return result
}

private fun findGuardStartingPosition(input: List<List<String>>): Pair<Int, Int> {
  for (row in input.indices) {
    for (col in input[row].indices) {
      if (input[row][col] == GUARD) {
        return row to col
      }
    }
  }
  throw IllegalArgumentException("Starting position not found")
}

private fun isOutOfBounds(input: List<List<String>>, position: Pair<Int, Int>): Boolean {
  val (row, col) = position
  return row < 0 || row >= input.size || col < 0 || col >= input[row].size
}

private fun turnRight(direction: MovementDirection): MovementDirection {
  return when (direction) {
    UP -> RIGHT
    RIGHT -> DOWN
    DOWN -> LEFT
    LEFT -> UP
  }
}