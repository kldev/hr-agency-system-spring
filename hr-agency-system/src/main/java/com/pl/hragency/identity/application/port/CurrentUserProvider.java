package com.pl.hragency.identity.application.port;

import com.pl.hragency.identity.api.CurrentUser;

public interface CurrentUserProvider {
    CurrentUser get();
}
