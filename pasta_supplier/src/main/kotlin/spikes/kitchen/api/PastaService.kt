package spikes.kitchen.api

import org.slf4j.LoggerFactory
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForEntity
import spikes.kitchen.api.ResponseStatus.Companion.completed
import spikes.kitchen.api.ResponseStatus.Companion.inProgress
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService

class PastaService(private val semanticSearchStorage: SemanticSearchStorage,
                   private val bakers: ExecutorService) {



    fun find(searchCriteria: String): ResultWithStatus {

        val searchResult: SearchResult? = semanticSearchStorage.semanticFind(searchCriteria)
        return if (searchResult != null)
            completed(searchResult)
        else {
            if (semanticSearchStorage.checkOrCreateTaskInProgress(searchCriteria)) {
                log.warn("found a search already in progress for $searchCriteria i am going to avoid another remote call")
                inProgress(timeToProcess(searchCriteria))
            } else {
                asyncRemoteSearch(searchCriteria)
                    .thenAccept { response: String -> semanticSearchStorage.updateTaskWithResponse(searchCriteria, response) }
                inProgress(timeToProcess(searchCriteria))
            }
        }

    }

    private fun asyncRemoteSearch(bakedGood: String): CompletableFuture<String> = CompletableFuture.supplyAsync(
        {

            val requiredTime = timeToProcess(bakedGood)
            log.info("start to work on $bakedGood i will need $requiredTime seconds")
            doCall("https://reqres.in/api/users?delay=$requiredTime")
            log.info("completed  $bakedGood in $requiredTime seconds")
            val response = "Bake for $bakedGood complete and order dispatched. Enjoy!"
            response
        }, bakers
    )

    private fun doCall(url: String) {
        log.info("calling  $url")
        val result = RestTemplate().getForEntity<String>(url)
    }


    private fun timeToProcess(bakedGood: String) = 10


    companion object {
        private val log = LoggerFactory.getLogger(PastaService::class.java)

    }

    data class ResponseAndTime(val response: String?, val creationTime: LocalDateTime?)
}
