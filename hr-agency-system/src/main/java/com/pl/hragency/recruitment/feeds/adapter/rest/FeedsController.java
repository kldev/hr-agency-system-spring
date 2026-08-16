package com.pl.hragency.recruitment.feeds.adapter.rest;

import com.pl.hragency.recruitment.feeds.domain.model.FeedType;
import com.pl.hragency.recruitment.feeds.handler.GetFeedFileHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@Tag(name = "Feeds")
@RequestMapping("api/public/feeds")
public class FeedsController {
    private final GetFeedFileHandler handler;

    public FeedsController(GetFeedFileHandler handler) {
        this.handler = handler;
    }

    @GetMapping(
            value = "/{slug}.xml",
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<StreamingResponseBody> feedXml(@PathVariable String slug) {
        return handler.handle(slug, FeedType.xml);
    }

    @GetMapping(
            value = "/{slug}.json",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<StreamingResponseBody> feedJson(@PathVariable String slug) {
        return handler.handle(slug, FeedType.json);
    }
}
