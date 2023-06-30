package io.conduktor.demos.kafka.opensearch;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class OpenSearchConsumerApplication implements CommandLineRunner {

  private final OpenSearchConsumer openSearchConsumer;

  public static void main(String[] args) {
    SpringApplication.run(OpenSearchConsumerApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    openSearchConsumer.consume();
  }
}
