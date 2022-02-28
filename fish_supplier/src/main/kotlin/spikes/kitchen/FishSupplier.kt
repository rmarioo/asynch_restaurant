package spikes.kitchen

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FishSupplier

fun main(args: Array<String>) {
	runApplication<FishSupplier>(*args)
}
