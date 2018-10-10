/*
 * Copyright (C) 2014 - 2016 Softwaremill <http://softwaremill.com>
 * Copyright (C) 2016 - 2018 Lightbend Inc. <http://www.lightbend.com>
 */

package docs.javadsl;

// #metadata
import akka.actor.ActorRef;
import akka.kafka.KafkaConsumerActor;
import akka.kafka.Metadata;
import akka.pattern.PatternsCS;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

// #metadata

public class FetchMetadata extends ConsumerExample {

  public static void main(String[] args) {
    new FetchMetadata().demo();
  }

  void demo() {
    // #metadata
    // Create kafka consumer actor to be used with Consumer.plainExternalSource or
    // committableExternalSource
    ActorRef consumer = system.actorOf((KafkaConsumerActor.props(consumerSettings)));

    // ... create source ...
    Duration timeout = Duration.ofSeconds(2);

    CompletionStage<Metadata.Topics> topicsStage =
        PatternsCS.ask(consumer, Metadata.createListTopics(), timeout)
            .thenApply(reply -> ((Metadata.Topics) reply));

    // print response
    topicsStage
        .thenApply(Metadata.Topics::getResponse)
        .thenAccept(
            responseOption ->
                responseOption.ifPresent(
                    map ->
                        map.forEach(
                            (topic, partitionInfo) ->
                                partitionInfo.forEach(
                                    info -> System.out.println(topic + ": " + info.toString())))));

    // #metadata
  }
}
