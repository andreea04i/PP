fun operatiiLista() {
    val lista = listOf(1, 21, 75, 39, 7, 2, 35, 3, 31, 7, 8)

    val filtrat = lista.filter { it >= 5 }
    val perechi = filtrat.chunked(2).map { it[0] to it[1] }
    val produse = perechi.map { it.first * it.second }
    val suma = produse.sum()

    println("1) Lista filtrată (>=5): $filtrat")
    println("1) Perechi: $perechi")
    println("1) Produse: $produse")
    println("1) Suma produselor: $suma")
}
fun main() {
    operatiiLista()  
}
