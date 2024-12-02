import kotlin.io.path.Path
import kotlin.io.path.readText

private const val FILE_EXTENSION = ".txt"
private const val FILE_PATH = "src/main/resources/"

fun getInputAsStrings(file: String): List<String> {
    return getInputAsStrings(file, "\n")
}

fun getInputAsStrings(file: String, splitter: String): List<String> {
    return try {
        Path("$FILE_PATH$file$FILE_EXTENSION").readText().split(splitter)
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}