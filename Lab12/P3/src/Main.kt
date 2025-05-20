import kotlin.math.pow
import kotlin.math.sqrt

data class Point(val x: Double, val y: Double)

fun dist(p1: Point, p2: Point): Double =
    sqrt((p2.x - p1.x).pow(2) + (p2.y - p1.y).pow(2))

fun perimetruPoligon(puncte: List<Point>): Double {
    return puncte.zipWithNext { p1, p2 -> dist(p1, p2) }.sum()
}

fun citestePuncte(): List<Point> {
    val n = readLine()?.toIntOrNull() ?: return emptyList()
    val listaPuncte = mutableListOf<Point>()
    repeat(n) {
        val (x, y) = readLine()?.split(" ")?.mapNotNull { it.toDoubleOrNull() } ?: listOf()
        if (x != null && y != null) listaPuncte.add(Point(x, y))
    }
    return listaPuncte
}

fun testPerimetru() {
    println("3) Introdu numărul de puncte și coordonatele (ex: 4 / 0 0 / 0 1 / 1 0 / 1 1):")
    val puncte = citestePuncte()
    val perimetru = perimetruPoligon(puncte)
    println("Perimetrul poligonului (fără ultima latură): $perimetru")

    if (puncte.isNotEmpty()) {
        val perimetruInchis = perimetru + dist(puncte.last(), puncte.first())
        println("Perimetrul poligonului (închis): $perimetruInchis")
    }
}
fun main() {
    testPerimetru()
}