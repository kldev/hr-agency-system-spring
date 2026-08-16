package com.pl.hragency.shared.handler;

import com.pl.hragency.shared.rest.ExecutionContext;

public interface ResourceCommandHandler<I, C, R> {

    R handle(
            ExecutionContext context,
            I id,
            C command
    );
}
