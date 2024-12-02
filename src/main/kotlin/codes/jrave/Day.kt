package codes.jrave

open class Day {
  var input: String = ""

  open fun readInputFileFromResources(path: String): Day {
    input =
      object {}.javaClass.getResource("/$path")?.readText(Charsets.UTF_8)
        ?: throw IllegalArgumentException("File not found!")
    return this
  }
}