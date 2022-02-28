package kitchen.api

import kitchen.api.Food.FISH
import kitchen.api.Food.PASTA
import org.slf4j.LoggerFactory
import org.springframework.web.client.RestTemplate
import spikes.kitchen.api.KitchenController
import waiter.ResponseStatus.Companion.completed
import waiter.ResponseStatus.Companion.inProgress
import waiter.ResultWithStatus
import waiter.SearchResult
import kotlin.math.max

class KitchenService {
    fun doOrder(): ResultWithStatus {

        val fishResponse = call(FISH.supplierUrl)
        val pastaResponse = call(PASTA.supplierUrl)

        return if (isCompleted(fishResponse) && isCompleted(pastaResponse)
        ) {
            cookFor(1)
            completed(SearchResult(fishResponse?.result + pastaResponse?.result))
        } else {
            inProgress(maxRetryTime(fishResponse, pastaResponse))
        }
    }

    private fun call(supplierUrl: String) =
        RestTemplate().getForEntity(supplierUrl, ResultWithStatus::class.java).body

    private fun cookFor(cookTime: Long) {
        log.info("all ingredients are ready cooking ingredients for $cookTime seconds")
        Thread.sleep(cookTime*1000)
        log.info("cooked ingredient")
    }

    private fun maxRetryTime(
        fishResponse: ResultWithStatus?,
        pastaResponse: ResultWithStatus?
    ): Int {
        val waitForFish = if (isWaiting(fishResponse)) retryAfterSeconds(fishResponse) else 0
        val waitForPasta = if (isWaiting(pastaResponse)) retryAfterSeconds(pastaResponse) else 0
        return max(waitForFish!!, waitForPasta!!)
    }

    private fun retryAfterSeconds(fishResponse: ResultWithStatus?) =
        fishResponse?.responseStatus?.retryAfterSeconds

    private fun isWaiting(fishResponse: ResultWithStatus?) =
        status(fishResponse).equals("waiting")

    private fun isCompleted(fishResponse: ResultWithStatus?) =
        status(fishResponse).equals("completed")

    private fun status(fishResponse: ResultWithStatus?) = fishResponse?.responseStatus?.status


    companion object {
        private val log = LoggerFactory.getLogger(KitchenController::class.java)
    }
}
