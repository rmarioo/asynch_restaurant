package waiter

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

class Waiter() {

    fun doKitchenOrder(): ResponseEntity<ResultWithStatus> {

        val url = "http://localhost:8080/api/async/order/dish"
        log.info("calling service $url")
        var responseEntity = remoteSearchToUrl(url)

        val resultWithStatus: ResultWithStatus = responseEntity.body!!

        return if (isStatusCompleted(resultWithStatus))
            responseEntity
        else {
            printDebugInfo(resultWithStatus)
            Thread.sleep(retryInMilliseconds(resultWithStatus))
            log.info("calling service $url")
            remoteSearchToUrl(url)
        }
    }

    private fun isStatusCompleted(resultWithStatus: ResultWithStatus) =
        statusFrom(resultWithStatus) == "completed"

    private fun statusFrom(resultWithStatus: ResultWithStatus) =
        resultWithStatus.responseStatus.status

    private fun printDebugInfo(resultWithStatus: ResultWithStatus) {
        log.info("received ${resultWithStatus.responseStatus}")
        log.info("i will retry in ${retryInMilliseconds(resultWithStatus)} milliseconds")
    }

    private fun retryInMilliseconds(resultWithStatus: ResultWithStatus): Long {
        val retryAfterSeconds = resultWithStatus.responseStatus.retryAfterSeconds
        val retryInMilliseconds = retryAfterSeconds.toLong() * 1000 + 1000
        return retryInMilliseconds
    }

    private fun remoteSearchToUrl(url: String) =
        RestTemplate().getForEntity(url, ResultWithStatus::class.java)

    companion object {
        private val log = LoggerFactory.getLogger(Waiter::class.java)

    }
}


