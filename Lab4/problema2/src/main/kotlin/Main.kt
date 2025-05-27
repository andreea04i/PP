interface PaymentMethod {
    fun pay(amount: Double): Boolean
}

class CashPayment(private var availableCash: Double) : PaymentMethod {
    override fun pay(amount: Double): Boolean {
        return if (availableCash >= amount) {
            availableCash -= amount
            println("Plătit $$amount în numerar.")
            true
        } else {
            println("Numerar insuficient.")
            false
        }
    }
}

class CardPayment(private val cardNumber: String) : PaymentMethod {
    override fun pay(amount: Double): Boolean {
        println("Plătit $$amount folosind cardul $cardNumber.")
        return true
    }
}

class Movie(val title: String, val duration: Int, val price: Double)

class Ticket(val movie: Movie, val seatNumber: String, val price: Double)

class User(val name: String, private val paymentMethod: PaymentMethod) {
    fun buyTicket(movie: Movie, seat: String): Ticket? {
        return if (paymentMethod.pay(movie.price)) {
            println("$name a cumpărat un bilet pentru ${movie.title} la locul $seat.")
            Ticket(movie, seat, movie.price)
        } else {
            println("$name nu a putut cumpăra biletul.")
            null
        }
    }
}

fun main() {
    val movie = Movie("Stich-The Movie", 148, 10.0)
    val user1 = User("Daria", CashPayment(20.0))
    val user2 = User("Andreea", CardPayment("1234-4444-7777-9999"))

    user1.buyTicket(movie, "B4")
    user2.buyTicket(movie, "B5")
}