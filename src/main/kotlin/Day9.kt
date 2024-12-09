import java.math.BigInteger

fun main() {
  val input = getInputAsStrings("Day9").map { it.split("").filter { it != "" }.map{ it.toInt() } }[0]

  solveDay9a(input)
  solveDay9b(input)
}

private fun solveDay9a(input: List<Int>) {
  val result = mutableListOf<Int>()
  val files = toFiles(input)

  initializeResult(files, result)

  var freeSpaceIndex = result.indexOf(-1)

  while (freeSpaceIndex != -1) {
    val lastFileIndex = result.indexOfLast { it >= 0 }
    if (lastFileIndex < freeSpaceIndex) break

    result[freeSpaceIndex] = result[lastFileIndex]
    result[lastFileIndex] = -1

    freeSpaceIndex = result.indexOf(-1)
  }

  println(calculateChecksum(result))
}

private fun solveDay9b(input: List<Int>) {
  val result = mutableListOf<Int>()
  val files = toFiles(input)

  initializeResult(files, result)

  files.sortedByDescending { it.id }.forEach { file ->
    val fileStartIndex = result.indexOfFirst { it == file.id }
    val fileSize = file.fileBlocks.size

    val freeSpaceIndex = findFreeSpace(result, fileStartIndex, fileSize)
    if (freeSpaceIndex != -1) {
      moveFile(result, fileStartIndex, freeSpaceIndex, fileSize, file.id)
    }
  }

  println(calculateChecksum(result))
}

private fun initializeResult(files: List<File>, result: MutableList<Int>) {
  files.forEach { file ->
    result.addAll(file.fileBlocks)
    result.addAll(file.freeSpace)
  }
}

private fun findFreeSpace(disk: List<Int>, fileEndIndex: Int, fileSize: Int): Int {
  var freeSpaceStart = -1
  var freeSpaceSize = 0

  for (i in 0 until fileEndIndex) {
    if (disk[i] == -1) {
      if (freeSpaceStart == -1) freeSpaceStart = i
      freeSpaceSize++
      if (freeSpaceSize == fileSize) return freeSpaceStart
    } else {
      freeSpaceStart = -1
      freeSpaceSize = 0
    }
  }

  return -1
}

private fun moveFile(disk: MutableList<Int>, fileStartIndex: Int, freeSpaceIndex: Int, fileSize: Int, fileId: Int) {
  for (i in freeSpaceIndex until freeSpaceIndex + fileSize) {
    disk[i] = fileId
  }

  for (i in fileStartIndex until fileStartIndex + fileSize) {
    disk[i] = -1
  }
}

private data class File(
  val id: Int,
  val fileBlocks: MutableList<Int>,
  val freeSpace: MutableList<Int>
)

private fun toFiles(input: List<Int>): List<File> {
  val files = mutableListOf<File>()
  var fileId = 0

  for (i in input.indices step 2) {
    val size = MutableList(input[i]) { fileId }
    val freeSpace = if (i + 1 < input.size) MutableList(input[i + 1]) { -1 } else mutableListOf()
    files.add(File(fileId++, size, freeSpace))
  }

  return files
}

private fun calculateChecksum(disk: MutableList<Int>): BigInteger {
  return disk.withIndex().sumOf { (index, fileId) -> if (fileId >= 0) index.toBigInteger() * fileId.toBigInteger() else BigInteger.ZERO }
}