package spikes.kitchen.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import spikes.kitchen.api.SemanticSearchStorage
import spikes.kitchen.api.PastaService
import java.util.concurrent.Executors

@Configuration
class PastaConfiguration {


   @Bean
   fun smartPizzaShop(): PastaService = PastaService(SemanticSearchStorage(), Executors.newFixedThreadPool(5))

}

