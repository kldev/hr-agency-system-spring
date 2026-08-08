package com.pl.hragency.development.scenario;


import com.pl.hragency.development.scenario.jobdescription.ProductionJobDescriptionScenario;
import com.pl.hragency.development.scenario.jobdescription.TechnicalJobDescriptionScenario;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class JobDescriptionScenario {

    private final TechnicalJobDescriptionScenario technicalScenario;
    private final ProductionJobDescriptionScenario productionScenario;

    public JobDescriptionScenario(
            TechnicalJobDescriptionScenario technicalScenario,
            ProductionJobDescriptionScenario productionScenario
    ) {
        this.technicalScenario = technicalScenario;
        this.productionScenario = productionScenario;
    }

    public void create(
            UUID organizationId,
            List<UUID> userIds
    ) {
        if (userIds.isEmpty()) {
            return;
        }

        technicalScenario.create(
                organizationId,
                userIds
        );

        productionScenario.create(
                organizationId,
                userIds
        );
    }
}