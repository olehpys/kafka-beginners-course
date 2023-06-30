package io.conduktor.demos.kafka.opensearch;

import com.google.gson.JsonParser;
import java.io.IOException;
import java.time.Duration;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.opensearch.action.bulk.BulkRequest;
import org.opensearch.action.bulk.BulkResponse;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.opensearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenSearchConsumer {

  @Value("${kafka.topic}")
  private String topic;
  @Value("${opensearch.index}")
  private String indexName;
  private final RestHighLevelClient openSearchClient;
  private final KafkaConsumer<String, String> kafkaConsumer;

  @PostConstruct
  public void createIndexIfNotExists() throws IOException {
    boolean indexExists = openSearchClient.indices()
        .exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);

    if (!indexExists) {
      CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
      openSearchClient.indices()
          .create(createIndexRequest, RequestOptions.DEFAULT);
      log.info("The Wikimedia Index has been created!");
    } else {
      log.info("The Wikimedia Index already exists");
    }
  }

  public void consume() throws IOException {
    kafkaConsumer.subscribe(Set.of(topic));

    while (true) {

      ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(3000));

      int recordCount = records.count();
      log.info("Received " + recordCount + " record(s)");

      BulkRequest bulkRequest = new BulkRequest();

      for (ConsumerRecord<String, String> record : records) {

        // send the record into OpenSearch

        // strategy 1
        // define an ID using Kafka Record coordinates
//                    String id = record.topic() + "_" + record.partition() + "_" + record.offset();

        try {
          // strategy 2
          // we extract the ID from the JSON value
          String id = extractId(record.value());

          IndexRequest indexRequest = new IndexRequest("wikimedia")
              .source(record.value(), XContentType.JSON)
              .id(id);

//                        IndexResponse response = openSearchClient.index(indexRequest, RequestOptions.DEFAULT);

          bulkRequest.add(indexRequest);

//                        log.info(response.getId());
        } catch (Exception e) {
          log.error("Got exception: {}", e, e);
          throw e;
        }
      }

      if (bulkRequest.numberOfActions() > 0) {
        BulkResponse bulkResponse = openSearchClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        log.info("Inserted " + bulkResponse.getItems().length + " record(s).");

        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        // commit offsets after the batch is consumed
        kafkaConsumer.commitSync();
        log.info("Offsets have been committed!");
      }
    }
  }

  private String extractId(String json) {
    // gson library
    return JsonParser.parseString(json)
        .getAsJsonObject()
        .get("meta")
        .getAsJsonObject()
        .get("id")
        .getAsString();
  }
}