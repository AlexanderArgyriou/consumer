package com.argyriou.consumer;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }
    @PostConstruct
    public void consume() throws InterruptedException {
        WebClient wc = WebClient.create();
        for (int i = 0; i < 10; ++i) {
            Thread.sleep(1000);
            wc
                    .get()
                    .uri("http://localhost:9999/reactive/payload")
                    .header("id", String.valueOf(i))
                    .accept(MediaType.TEXT_EVENT_STREAM)
                    .retrieve()
                    .bodyToFlux(Item.class)
                    .subscribe(item ->
                            System.out.printf("current consumer thread : %s responded to event" +
                                    ", [ info : %s ] %n", Thread.currentThread().getName(), item)
                    );
        }
    }
}
