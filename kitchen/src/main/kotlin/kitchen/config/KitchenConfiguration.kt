package spikes.kitchen.config

import kitchen.api.KitchenService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KitchenConfiguration {


    @Bean
    fun kitchenService(): KitchenService {

        return KitchenService()
    }
}

