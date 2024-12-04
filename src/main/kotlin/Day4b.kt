private const val MAS = "MAS"
private const val SAM = "SAM"

fun main() {
  val input = getInputAsStrings("Day4")

  solveDay4b(input)
}

private fun solveDay4b(grid: List<String>) {
  val result = mutableListOf<Pair<Int, Int>>()

  for (y in grid.indices) {
    for (x in grid[y].indices) {
      if (grid[y][x] == 'A' && // The letter 'A' has to be the center of the X
        checkDiagonalTopLeftToBottomRight(x, y, grid) &&
        checkDiagonalTopRightToBottomLeft(x, y, grid)
      ) result.add(x to y)
    }
  }

  println(result.count())
}

private fun checkDiagonalTopLeftToBottomRight(x: Int, y: Int, grid: List<String>): Boolean {
  if (isOutOfBounds(x, y, grid)) {
    return false
  }

  val topLeft = grid[y - 1][x - 1]
  val middle = grid[y][x]
  val bottomRight = grid[y + 1][x + 1]

  val word = "$topLeft$middle$bottomRight"

  return word == MAS || word == SAM
}

private fun checkDiagonalTopRightToBottomLeft(x: Int, y: Int, grid: List<String>): Boolean {
  if (isOutOfBounds(x, y, grid)) {
    return false
  }

  val topRight = grid[y - 1][x + 1]
  val middle = grid[y][x]
  val bottomLeft = grid[y + 1][x - 1]

  val word = "$topRight$middle$bottomLeft"

  return word == MAS || word == SAM
}

private fun isOutOfBounds(x: Int, y: Int, grid: List<String>) =
  x - 1 < 0 || y - 1 < 0 || x + 1 >= grid[0].length || y + 1 >= grid.size

