package com.pl.hragency.shared.rest;

import java.util.List;

public record ApiValidationError(String message,
                              List<ValidationError> errors){

    public record ValidationError(
            String field,
            String message
    ) {}

}
