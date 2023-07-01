package io.conduktor.demos.kafka.opensearch;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

  private final KafkaProperties kafkaProperties;

  @Bean
  public KafkaConsumer<String, String> kafkaConsumer() {
    return new KafkaConsumer<>(kafkaProperties.buildConsumerProperties());
  }
}
