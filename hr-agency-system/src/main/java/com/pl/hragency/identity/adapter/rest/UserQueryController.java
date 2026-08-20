package com.pl.hragency.identity.adapter.rest;

import com.pl.hragency.identity.application.query.UserListItem;
import com.pl.hragency.identity.application.query.UserListQuery;
import com.pl.hragency.identity.application.query.UserQueryService;
import com.pl.hragency.shared.rest.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User")
public class UserQueryController {
    private final UserQueryService userQueryService;

    public UserQueryController(
            UserQueryService userQueryService) {

        this.userQueryService = userQueryService;
    }

    @GetMapping
    public PageResponse<UserListItem> getUsers(@Validated
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20", required = false)  @Max(500) int size
    ) {

        var pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        new Sort.Order(Sort.Direction.ASC, "lastName"),
                        new Sort.Order(Sort.Direction.ASC, "firstName")
                )
        );

        var result = userQueryService.findAll(
                new UserListQuery(search, pageable)
        );

        return new PageResponse<> (
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

}
