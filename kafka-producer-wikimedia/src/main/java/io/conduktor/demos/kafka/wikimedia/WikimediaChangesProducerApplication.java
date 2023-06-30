package io.conduktor.demos.kafka.wikimedia;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class WikimediaChangesProducerApplication implements CommandLineRunner {

  private final WikimediaChangesProducer producer;

  public static void main(String[] args) {
    SpringApplication.run(WikimediaChangesProducerApplication.class, args);
  }

  @Override
  public void run(String... args) {
    producer.produce();
  }
}
