package spikes.kitchen.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import spikes.kitchen.api.SemanticSearchStorage
import spikes.kitchen.api.FishService
import java.util.concurrent.Executors

@Configuration
class FishSupplierConfiguration {


   @Bean
   fun smartPizzaShop(): FishService = FishService(SemanticSearchStorage(), Executors.newFixedThreadPool(5))

}

