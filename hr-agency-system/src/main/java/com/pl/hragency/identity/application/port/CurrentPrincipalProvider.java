package com.pl.hragency.identity.application.port;

import com.pl.hragency.identity.api.CurrentIntegrationClient;
import com.pl.hragency.identity.api.CurrentUser;

public interface CurrentPrincipalProvider {
    CurrentUser getRequiredUser();
    CurrentIntegrationClient getRequiredIntegration();
}
