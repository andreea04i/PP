interface GenericGate {
    fun GetInputs(inputs: List<Int>): Boolean
}

class ANDGate : GenericGate {
    override fun GetInputs(inputs: List<Int>): Boolean {
        for (i in inputs) {
            if (i != 1) return false
        }
        return true
    }
}

abstract class Gate(protected val impl: GenericGate) {
    abstract fun evaluate(): Boolean
}

class GateConstructor(private val impl: GenericGate) {
    private val inputs = mutableListOf<Int>()

    fun addInput(input: Int): GateConstructor {
        inputs.add(input)
        return this
    }

    fun construieste(): Gate {
        return when (inputs.size) {
            2 -> Gate2(impl, inputs)
            3 -> Gate3(impl, inputs)
            4 -> Gate4(impl, inputs)
            8 -> Gate8(impl, inputs)
            else -> throw IllegalArgumentException("Invalid number of inputs")
        }
    }
}

class Gate2(impl: GenericGate, private val inputs: List<Int>) : Gate(impl) {
    override fun evaluate(): Boolean = StateMachine().evaluate(inputs, impl)
}

class Gate3(impl: GenericGate, private val inputs: List<Int>) : Gate(impl) {
    override fun evaluate(): Boolean = StateMachine().evaluate(inputs, impl)
}

class Gate4(impl: GenericGate, private val inputs: List<Int>) : Gate(impl) {
    override fun evaluate(): Boolean = StateMachine().evaluate(inputs, impl)
}

class Gate8(impl: GenericGate, private val inputs: List<Int>) : Gate(impl) {
    override fun evaluate(): Boolean = StateMachine().evaluate(inputs, impl)
}

interface State {
    fun next(input: Int): State
    fun isFinal(): Boolean
}

class AcceptedState : State {
    override fun next(input: Int): State = if (input == 1) this else DeniedState()
    override fun isFinal(): Boolean = true
}

class DeniedState : State {
    override fun next(input: Int): State = this
    override fun isFinal(): Boolean = false
}

class StateMachine() {
    fun evaluate(inputs: List<Int>, impl: GenericGate): Boolean {
        var state: State = AcceptedState()
        for (input in inputs) {
            state = state.next(input)
        }
        return state.isFinal() && impl.GetInputs(inputs)
    }
}

fun GateOutput(inputsNumber: Int) {
    val impl = ANDGate()
    val combinations = generate(inputsNumber)

    println("AND Gate with $inputsNumber inputs:")

    for (comb in combinations) {
        val constructor = GateConstructor(impl)
        comb.forEach { constructor.addInput(it) }
        val gate = constructor.construieste()
        val output = gate.evaluate()
        println("${comb.joinToString(" ")} -> ${if (output) "true" else "false"}")
    }

    println()
}

fun generate(n: Int): List<List<Int>> {
    val res = mutableListOf<List<Int>>()
    val total = 1 shl n
    for (i in 0 until total) {
        val combination = mutableListOf<Int>()
        for (j in n - 1 downTo 0) {
            combination.add((i shr j) and 1)
        }
        res.add(combination)
    }
    return res
}

fun main() {
    GateOutput(2)
    GateOutput(3)
    GateOutput(4)
    GateOutput(8)
}