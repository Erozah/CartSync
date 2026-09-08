package com.erozah.kestachet.infrastructure.config;

import com.erozah.kestachet.domain.port.in.CalculateBudgetUseCase;
import com.erozah.kestachet.domain.port.in.ManageListUseCase;
import com.erozah.kestachet.domain.port.in.SyncBatchUseCase;
import com.erozah.kestachet.domain.port.in.ValidateCartUseCase;
import com.erozah.kestachet.domain.port.out.ConsumptionAnalyticsPort;
import com.erozah.kestachet.domain.port.out.ExternalCatalogPort;
import com.erozah.kestachet.domain.port.out.ShoppingListRepositoryPort;
import com.erozah.kestachet.domain.port.out.SyncJournalPort;
import com.erozah.kestachet.domain.service.BudgetEngine;
import com.erozah.kestachet.domain.service.ShoppingListDomainService;
import com.erozah.kestachet.domain.service.SyncEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class DomainConfig {

    @Bean
    public BudgetEngine budgetEngine() {
        return new BudgetEngine();
    }

    @Bean
    public SyncEngine syncEngine(ShoppingListRepositoryPort repositoryPort, SyncJournalPort journalPort) {
        return new SyncEngine(repositoryPort, journalPort);
    }

    @Bean
    public ShoppingListDomainService shoppingListDomainService(
        ShoppingListRepositoryPort repositoryPort,
        ExternalCatalogPort catalogPort,
        ConsumptionAnalyticsPort analyticsPort,
        BudgetEngine budgetEngine
    ) {
        return new ShoppingListDomainService(repositoryPort, catalogPort, analyticsPort, budgetEngine);
    }
}
