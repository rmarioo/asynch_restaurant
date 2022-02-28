package spikes.kitchen

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PastaSupplier

fun main(args: Array<String>) {
	runApplication<PastaSupplier>(*args)
}
