private const val GRID_HEIGHT = 103   // 7 for test input
private const val GRID_WIDTH = 101    // 11 for test input
private const val SECONDS = 10403

fun main() {
  val input = getInputAsStrings("Day14")

  solveDay14a(input)
  solveDay14b(input)
}

private fun solveDay14a(input: List<String>) {
  val robots = toRobots(input)

  repeat(SECONDS) {
    for (robot in robots) {
      robot.move()
    }
  }

  val robotsPerQuadrant = groupRobotsPerQuadrant(robots)
  val result = robotsPerQuadrant.map { it.value.size }.reduce { acc, size -> acc * size }

  println(result)
}

private fun solveDay14b(input: List<String>) {
  val robots = toRobots(input)
  var counter = 0

  repeat(SECONDS) {
    counter++
    for (robot in robots) {
      robot.move()
      if (areAllRobotsInUniquePositions(robots)) {
        printGrid(robots)
        println(counter)
        return@repeat
      }
    }
  }
}

private fun areAllRobotsInUniquePositions(robots: List<Robot>): Boolean {
  val seenPositions = mutableSetOf<Pair<Int, Int>>()
  for (robot in robots) {
    if (!seenPositions.add(robot.currentPosition)) {
      return false
    }
  }
  return true
}

private fun groupRobotsPerQuadrant(robots: List<Robot>): Map<Quadrant, List<Robot>> {
  val result = mutableMapOf<Quadrant, MutableList<Robot>>()
  val quadrants = toQuadrants()

  robots.forEach { robot ->
    val robotPosition = robot.currentPosition
    val matchingQuadrant = quadrants.find { it.isWithinQuadrant(robotPosition) }

    if (matchingQuadrant != null) {
      result.computeIfAbsent(matchingQuadrant) { mutableListOf() }.add(robot)
    }
  }

  return result
}

private fun toRobots(input: List<String>): List<Robot> {
  return input.map {
    val (p, v) = it.split(" ")
    val (px, py) = p.substringAfter("p=").split(",")
    val (vx, vy) = v.substringAfter("v=").split(",")

    Robot(px.toInt() to py.toInt(), vx.toInt() to vy.toInt())
  }
}

fun toQuadrants(): List<Quadrant> {
  val halfWidth = GRID_WIDTH / 2
  val halfHeight = GRID_HEIGHT / 2

  return listOf(
    Quadrant("top-left", minX = 0, maxX = halfWidth - 1, minY = 0, maxY = halfHeight - 1),
    Quadrant("top-right", minX = halfWidth + 1, maxX = GRID_WIDTH - 1, minY = 0, maxY = halfHeight - 1),
    Quadrant("bottom-left", minX = 0, maxX = halfWidth - 1, minY = halfHeight + 1, maxY = GRID_HEIGHT - 1),
    Quadrant("bottom-right", minX = halfWidth + 1, maxX = GRID_WIDTH - 1, minY = halfHeight + 1, maxY = GRID_HEIGHT - 1)
  )
}

data class Robot(
  val startingPosition: Pair<Int, Int>,
  val velocity: Pair<Int, Int>,
  var currentPosition: Pair<Int, Int> = startingPosition
) {

  fun move() {
    val newX = (currentPosition.first + velocity.first).mod(GRID_WIDTH)
    val newY = (currentPosition.second + velocity.second).mod(GRID_HEIGHT)

    currentPosition = Pair(newX, newY)
  }
}

data class Quadrant(
  val name: String,
  val minX: Int,
  val maxX: Int,
  val minY: Int,
  val maxY: Int,
) {

  fun isWithinQuadrant(point: Pair<Int, Int>): Boolean {
    val (x, y) = point
    return x in minX..maxX && y in minY..maxY
  }
}

private fun printGrid(robots: List<Robot>) {
  val grid = Array(GRID_HEIGHT) { CharArray(GRID_WIDTH) { '.' } }

  for (robot in robots) {
    val (x, y) = robot.currentPosition
    grid[y][x] = '#'
  }

  for (row in grid) {
    println(row.joinToString(""))
  }
}
