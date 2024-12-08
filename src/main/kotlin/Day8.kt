fun main() {
  val input = getInputAsStrings("Day8").map { it.split("").filter { it != "" } }

  solveDay8a(input)
  solveDay8b(input)
}

private fun solveDay8a(input: List<List<String>>) {
  val result = mutableSetOf<Pair<Int, Int>>()
  val antennaMap = toAntennaMap(input)

  for ((_, points) in antennaMap) {
    for (i in points.indices) {
      for (j in points.indices) {
        if (i != j) {
          val point1 = points[i]
          val point2 = points[j]

          // Calculate deltas between two points
          val deltaX = point2.first - point1.first
          val deltaY = point2.second - point1.second

          // Find two antinodes in opposite directions
          val antinode1 = Pair(point1.first - deltaX, point1.second - deltaY)
          val antinode2 = Pair(point2.first + deltaX, point2.second + deltaY)

          if (isWithinGrid(antinode1, input)) {
            result.add(antinode1)

          }
          if (isWithinGrid(antinode2, input)) {
            result.add(antinode2)
          }
        }
      }
    }
  }

  println(result.size)
}

private fun solveDay8b(input: List<List<String>>) {
  val result = mutableSetOf<Pair<Int, Int>>()
  val antennaMap = toAntennaMap(input)

  for ((_, points) in antennaMap) {
    // If there is more than one antenna of the same frequency, they are also all antinodes
    if (points.size > 1) {
      result.addAll(points)
    }

    for (i in points.indices) {
      for (j in points.indices) {
        if (i != j) {
          val point1 = points[i]
          val point2 = points[j]

          val deltaX = point2.first - point1.first
          val deltaY = point2.second - point1.second

          // Keep adding antinodes until the edge of the grid in both directions
          findAllAntinodes(point1, -deltaX, -deltaY, input, result)
          findAllAntinodes(point2, deltaX, deltaY, input, result)
        }
      }
    }
  }

  println(result.size)
}

private fun findAllAntinodes(
  currentPoint: Pair<Int, Int>,
  deltaX: Int,
  deltaY: Int,
  input: List<List<String>>,
  result: MutableSet<Pair<Int, Int>>
) {
  val (x, y) = currentPoint

  val nextPoint = Pair(x + deltaX, y + deltaY)

  if (isWithinGrid(nextPoint, input)) {
    result.add(nextPoint)

    findAllAntinodes(nextPoint, deltaX, deltaY, input, result)
  }
}

private fun isWithinGrid(point: Pair<Int, Int>, input: List<List<String>>): Boolean {
  val gridWidth = input.first().size
  val gridHeight = input.size

  val (x, y) = point
  return x in 0 until gridWidth && y in 0 until gridHeight
}

private fun toAntennaMap(input: List<List<String>>): Map<String, MutableList<Pair<Int, Int>>> {
  val antennaMap = mutableMapOf<String, MutableList<Pair<Int, Int>>>()

  for (y in input.indices) {
    for (x in input[y].indices) {
      val element = input[y][x]
      if (element[0].isLetterOrDigit()) {
        antennaMap.computeIfAbsent(element) { mutableListOf() }.add(x to y)
      }
    }
  }

  return antennaMap
}
