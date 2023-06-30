package io.conduktor.demos.kafka.wikimedia;

import com.launchdarkly.eventsource.EventSource;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WikimediaChangesProducer {

  private final WikimediaChangeHandler wikimediaChangeHandler;

  public void produce() {
    String url = "https://stream.wikimedia.org/v2/stream/recentchange";
    EventSource.Builder builder = new EventSource.Builder(wikimediaChangeHandler, URI.create(url));
    EventSource eventSource = builder.build();
    eventSource.start();
  }
}