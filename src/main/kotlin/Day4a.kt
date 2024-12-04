private const val XMAS = "XMAS"
private const val SAMX = "SAMX"

fun main() {
  val input = getInputAsStrings("Day4")
  solveDay4a(input)
}

private fun solveDay4a(grid: List<String>) {
  val result = mutableListOf<Pair<Int, Int>>()

  for (y in grid.indices) {
    for (x in grid[y].indices) {
      if (checkHorizontal(x, y, grid)) result.add(x to y)
      if (checkVertical(x, y, grid)) result.add(x to y)
      if (checkDiagonalTopLeftToBottomRight(x, y, grid)) result.add(x to y)
      if (checkDiagonalTopRightToBottomLeft(x, y, grid)) result.add(x to y)
    }
  }

  println(result.count())
}

private fun checkHorizontal(x: Int, y: Int, grid: List<String>): Boolean {
  if (x + 3 >= grid[y].length) return false // Out of bounds

  val left = grid[y][x]
  val middleLeft = grid[y][x + 1]
  val middleRight = grid[y][x + 2]
  val right = grid[y][x + 3]

  val word = "$left$middleLeft$middleRight$right"
  return word == XMAS || word == SAMX
}

private fun checkVertical(x: Int, y: Int, grid: List<String>): Boolean {
  if (y + 3 >= grid.size) return false // Out of bounds

  val top = grid[y][x]
  val middleTop = grid[y + 1][x]
  val middleBottom = grid[y + 2][x]
  val bottom = grid[y + 3][x]

  val word = "$top$middleTop$middleBottom$bottom"
  return word == XMAS || word == SAMX
}

private fun checkDiagonalTopLeftToBottomRight(x: Int, y: Int, grid: List<String>): Boolean {
  if (x + 3 >= grid[0].length || y + 3 >= grid.size) return false // Out of bounds

  val topLeft = grid[y][x]
  val middleLeft = grid[y + 1][x + 1]
  val middleRight = grid[y + 2][x + 2]
  val bottomRight = grid[y + 3][x + 3]

  val word = "$topLeft$middleLeft$middleRight$bottomRight"
  return word == XMAS || word == SAMX
}

private fun checkDiagonalTopRightToBottomLeft(x: Int, y: Int, grid: List<String>): Boolean {
  if (x - 3 < 0 || y + 3 >= grid.size) return false // Out of bounds

  val topRight = grid[y][x]
  val middleRight = grid[y + 1][x - 1]
  val middleLeft = grid[y + 2][x - 2]
  val bottomLeft = grid[y + 3][x - 3]

  val word = "$topRight$middleRight$middleLeft$bottomLeft"
  return word == XMAS || word == SAMX
}
