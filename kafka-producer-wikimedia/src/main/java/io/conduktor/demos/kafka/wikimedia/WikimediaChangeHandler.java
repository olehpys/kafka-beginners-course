package io.conduktor.demos.kafka.wikimedia;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WikimediaChangeHandler implements EventHandler {

  @Value("${kafka.topic}")
  private String topic;
  private final KafkaTemplate<String, String> kafkaTemplate;

  @Override
  public void onOpen() {
    // nothing here
  }

  @Override
  public void onClosed() {
    // nothing here
  }

  @Override
  public void onMessage(String event, MessageEvent messageEvent) {
    String data = messageEvent.getData();
    kafkaTemplate.send(topic, data);
  }

  @Override
  public void onComment(String comment) {
    // nothing here
  }

  @Override
  public void onError(Throwable t) {
    t.printStackTrace();
  }
}