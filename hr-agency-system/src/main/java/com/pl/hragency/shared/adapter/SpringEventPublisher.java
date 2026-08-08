package com.pl.hragency.shared.adapter;


import com.pl.hragency.shared.event.DomainEvent;
import com.pl.hragency.shared.event.EventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SpringEventPublisher implements EventPublisher {
    private final ApplicationEventPublisher publisher;

    public SpringEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(DomainEvent event) {
        publisher.publishEvent(event);
    }

    @Override
    public void publish(List<DomainEvent> events) {
        for (var event: events) {
            publisher.publishEvent(event);
        }
    }
}