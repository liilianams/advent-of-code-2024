fun main() {
  val rules = getInputAsStrings("Day5_rules")
  val pages = getInputAsStrings("Day5_pages").map { it.split(",").map { page -> page.toInt() } }

  solveDay5a(rules, pages)
  solveDay5b(rules, pages)
}

private fun solveDay5a(rules: List<String>, pages: List<List<Int>>) {
  val rulesMap = mapRules(rules)

  val correctlyOrderedPages = pages.filter { validateOrder(it, rulesMap) }
  val result = correctlyOrderedPages.sumOf { findMiddleValue(it) }

  println(result)
}

private fun solveDay5b(rules: List<String>, pages: List<List<Int>>) {
  val rulesMap = mapRules(rules)

  val inCorrectlyOrderedPages = pages.filter { !validateOrder(it, rulesMap) }
  val correctlyOrderedPages = inCorrectlyOrderedPages.map { it.sortedWith(comparator(rulesMap)) }
  val result = correctlyOrderedPages.sumOf { findMiddleValue(it) }

  println(result)
}

private fun comparator(rulesMap: Map<Int, List<Int>>) =
  Comparator<Int> { a, b ->
    when {
      rulesMap[a]?.contains(b) == true -> -1
      rulesMap[b]?.contains(a) == true -> 1
      else -> 0
    }
  }

private fun mapRules(rules: List<String>) =
  rules.fold(mutableMapOf<Int, MutableList<Int>>()) { acc, pair ->
    val (key, value) = pair.split("|").map { it.toInt() }
    acc.computeIfAbsent(key) { mutableListOf() }.add(value)
    acc
  }

private fun validateOrder(pages: List<Int>, rulesMap: Map<Int, List<Int>>): Boolean {
  var isCorrectOrder = true

  for (i in 0 until pages.size - 1) {
    val firstInt = pages[i]
    val secondInt = pages[i + 1]

    val nextNumberIsValid = rulesMap[firstInt]?.contains(secondInt) == true

    if (!nextNumberIsValid) {
      isCorrectOrder = false
      break
    }
  }

  return isCorrectOrder
}

private fun findMiddleValue(numbers: List<Int>): Int {
  return numbers[numbers.size / 2]
}