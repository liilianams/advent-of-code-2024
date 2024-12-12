private val directions = listOf(
  0 to -1,  // Up
  0 to 1,   // Down
  -1 to 0,  // Left
  1 to 0,   // Right
)

fun main() {
  val input = getInputAsStrings("Day12").map { it.split("").filter { it != "" } }

  solveDay12a(input)
}

private fun solveDay12a(input: List<List<String>>) {
  val letterCoordinates = groupCoordinatesByLetters(input)
  val regions = findRegionsPerLetter(letterCoordinates)
  val countOfFencesPerRegion = countFencesOfEachRegion(regions)

  val result = countOfFencesPerRegion.flatMap { it.value.map { it.first * it.second } }.sum()

  println(result)
}

private fun groupCoordinatesByLetters(input: List<List<String>>): MutableMap<String, MutableList<Pair<Int, Int>>> {
  val letterCoordinates = mutableMapOf<String, MutableList<Pair<Int, Int>>>()

  for ((y, row) in input.withIndex()) {
    for ((x, currentLetter) in row.withIndex()) {
      letterCoordinates.getOrPut(currentLetter) { mutableListOf() }.add(x to y)
    }
  }

  return letterCoordinates
}

private fun findRegionsPerLetter(letterCoordinates: Map<String, List<Pair<Int, Int>>>): Map<String, List<List<Pair<Int, Int>>>> {
  return letterCoordinates.mapValues { (_, coordinates) ->
    val visited = mutableSetOf<Pair<Int, Int>>()
    val regions = mutableListOf<List<Pair<Int, Int>>>()

    coordinates.forEach { coordinate ->
      if (coordinate !in visited) {
        val stack = mutableListOf(coordinate)
        val region = mutableListOf<Pair<Int, Int>>()

        while (stack.isNotEmpty()) {
          val current = stack.removeLast()
          if (visited.add(current)) {
            region.add(current)
            directions.map { (dx, dy) -> current.first + dx to current.second + dy }
              .filter { it in coordinates && it !in visited }
              .forEach { stack.add(it) }
          }
        }

        regions.add(region)
      }
    }

    regions
  }
}

private fun countFencesOfEachRegion(
  regions: Map<String, List<List<Pair<Int, Int>>>>
): Map<String, List<Pair<Int, Int>>> {
  val fencesPerRegion = mutableMapOf<String, MutableList<Pair<Int, Int>>>()

  for ((currentLetter, regionList) in regions) {
    val regionData = mutableListOf<Pair<Int, Int>>()

    for (region in regionList) {
      var fences = 0
      val area = region.size

      for ((x, y) in region) {
        for ((dx, dy) in directions) {
          val nextX = x + dx
          val nextY = y + dy

          if ((nextX to nextY) !in region) {
            fences++
          }
        }
      }
      regionData.add(area to fences)
    }
    fencesPerRegion[currentLetter] = regionData
  }

  return fencesPerRegion
}