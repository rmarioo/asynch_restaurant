package spikes.kitchen.api

import kitchen.api.KitchenService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import waiter.ResultWithStatus
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue


@RestController
@RequestMapping("/api")
class KitchenController(val kitchenService: KitchenService) {


    @OptIn(ExperimentalTime::class)
    @GetMapping("async/order/dish")
    fun doOrderAsync(): ResponseEntity<ResultWithStatus> {

        log.info("called doOrder ")
        val measureTimedValue = measureTimedValue {
            kitchenService.doOrder()
        }
        log.info("call duration in milliseconds ${measureTimedValue.duration.inWholeMilliseconds}")

        return ResponseEntity.ok().body(measureTimedValue.value)
    }

    companion object {
        private val log = LoggerFactory.getLogger(KitchenController::class.java)
        private const val LONG_POLLING_TIMEOUT = 5000L
    }


}

