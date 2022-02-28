package waiter

import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

internal class WaiterTest {

    @OptIn(ExperimentalTime::class)
    @Test
    internal fun name() {
        val waiter = Waiter()

        val (results, duration) = measureTimedValue {
            waiter.doKitchenOrder()
        }

        println("in ${duration.inWholeSeconds} seconds received ${results} ")


    }
}
