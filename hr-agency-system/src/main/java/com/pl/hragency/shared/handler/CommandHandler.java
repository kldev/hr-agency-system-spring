package com.pl.hragency.shared.handler;

import com.pl.hragency.shared.rest.ExecutionContext;

public interface CommandHandler<C, R> {

    R handle(
            ExecutionContext context,
            C command
    );
}
