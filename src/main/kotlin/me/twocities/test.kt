package me.twocities


fun main(args: Array<String>) {
  val metadata = Part1::class.loadMetadata()
  println("k: ${metadata.k()}")
  println("mv: ${metadata.mv().joinToString(separator = ",")}")
  println("bv: ${metadata.bv().joinToString(separator = ",")}")
  println("xs: ${metadata.xs()}")
  println("xi: ${metadata.xi()}")
  println("d1: ${metadata.d1().joinToString(separator = ",")}")
  println("d2: ${metadata.d2().joinToString(separator = ",")}")

  println("Foo:\n${Foo::class.classProto()}")
  println("Bar:\n${Bar::class.classProto()}")
}

private class Foo(private val nullable: String?, var varValue: Int) {
  lateinit var color: Color

  fun fun1() {
  }

  enum class Color {
    RED, BLACK, WHITE
  }
}

data class Bar(private var id: Long, val name: String)
