package com.pl.hragency.development.scenario.jobdescription;

import com.pl.hragency.company.api.CompanyApi;
import com.pl.hragency.jobdescription.api.CreateJobDescriptionInput;
import com.pl.hragency.jobdescription.api.JobDescriptionApi;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
public class ProductionJobDescriptionScenario {

    private final JobDescriptionApi jobDescriptionApi;
    private final CompanyApi companyApi;

    public ProductionJobDescriptionScenario(
            JobDescriptionApi jobDescriptionApi,
            CompanyApi companyApi
    ) {
        this.jobDescriptionApi = jobDescriptionApi;
        this.companyApi = companyApi;
    }

    public void create(
            UUID organizationId,
            List<UUID> userIds
    ) {
        var companies = companyApi.findAllIds(
                organizationId,
                20
        );

        if (companies.isEmpty() || userIds.isEmpty()) {
            return;
        }

        var companyIndex = 0;
        var userIndex = 0;

        create(
                organizationId,
                userIds.get(++userIndex % userIds.size()),
                new CreateJobDescriptionInput(
                        companies.get(++companyIndex % companies.size()),
                        "Spawacz MAG",
                        "Mid",
                        "Praca przy spawaniu konstrukcji stalowych metodą MAG. Oferta skierowana do osób z doświadczeniem w spawaniu elementów stalowych.",
                        List.of(
                                "Spawanie metodą MAG",
                                "Czytanie rysunku technicznego",
                                "Obsługa elektronarzędzi",
                                "Kontrola jakości wykonanych spoin",
                                "Przygotowanie elementów do spawania"
                        ),
                        List.of(
                                "Spawanie konstrukcji stalowych zgodnie z dokumentacją",
                                "Przygotowanie materiału do spawania",
                                "Wykonywanie spoin zgodnie z wymaganiami jakościowymi",
                                "Kontrola wykonanych połączeń",
                                "Dbanie o stanowisko pracy",
                                "Przestrzeganie zasad BHP"
                        ),
                        List.of(
                                "Doświadczenie w spawaniu metodą MAG",
                                "Umiejętność czytania rysunku technicznego",
                                "Aktualne uprawnienia spawalnicze będą dodatkowym atutem",
                                "Gotowość do pracy zmianowej",
                                "Dokładność i odpowiedzialność",
                                "Brak przeciwwskazań do pracy fizycznej"
                        ),
                        "Opole",
                        "ON_SITE",
                        BigDecimal.valueOf(8500),
                        BigDecimal.valueOf(12000)
                )
        );

        create(
                organizationId,
                userIds.get(userIndex++ % userIds.size()),
                new CreateJobDescriptionInput(
                        companies.get(companyIndex++ % companies.size()),
                        "Operator CNC",
                        "Mid",
                        "Poszukujemy Operatora CNC do pracy przy produkcji elementów metalowych. Praca na nowoczesnych maszynach CNC.",
                        List.of(
                                "Obsługa maszyn CNC",
                                "Czytanie rysunku technicznego",
                                "Podstawy programowania CNC",
                                "Pomiar detali",
                                "Kontrola jakości"
                        ),
                        List.of(
                                "Obsługa tokarek i frezarek CNC",
                                "Ustawianie parametrów maszyny",
                                "Kontrola wymiarów produkowanych elementów",
                                "Wprowadzanie korekt do programu",
                                "Przygotowanie materiału do produkcji",
                                "Prowadzenie podstawowej dokumentacji"
                        ),
                        List.of(
                                "Doświadczenie jako operator CNC",
                                "Umiejętność czytania rysunku technicznego",
                                "Znajomość podstaw obróbki skrawaniem",
                                "Umiejętność posługiwania się przyrządami pomiarowymi",
                                "Gotowość do pracy zmianowej",
                                "Dokładność"
                        ),
                        "Nysa",
                        "ON_SITE",
                        BigDecimal.valueOf(8000),
                        BigDecimal.valueOf(11500)
                )
        );

        create(
                organizationId,
                userIds.get(userIndex++ % userIds.size()),
                new CreateJobDescriptionInput(
                        companies.get(companyIndex++ % companies.size()),
                        "Monter Konstrukcji Stalowych",
                        "Mid",
                        "Praca przy montażu konstrukcji stalowych dla obiektów przemysłowych i magazynowych.",
                        List.of(
                                "Montaż konstrukcji stalowych",
                                "Czytanie rysunku technicznego",
                                "Obsługa elektronarzędzi",
                                "Pomiar i dopasowanie elementów",
                                "Prace ślusarskie"
                        ),
                        List.of(
                                "Montaż elementów konstrukcji stalowych",
                                "Dopasowywanie elementów zgodnie z dokumentacją",
                                "Wiercenie i cięcie elementów",
                                "Wykonywanie prostych prac ślusarskich",
                                "Kontrola poprawności montażu",
                                "Przestrzeganie zasad bezpieczeństwa"
                        ),
                        List.of(
                                "Doświadczenie w montażu konstrukcji stalowych",
                                "Umiejętność czytania rysunku technicznego",
                                "Znajomość elektronarzędzi",
                                "Sprawność fizyczna",
                                "Gotowość do pracy na wysokości",
                                "Prawo jazdy będzie dodatkowym atutem"
                        ),
                        "Kędzierzyn-Koźle",
                        "ON_SITE",
                        BigDecimal.valueOf(7500),
                        BigDecimal.valueOf(11000)
                )
        );

        create(
                organizationId,
                userIds.get(userIndex++ % userIds.size()),
                new CreateJobDescriptionInput(
                        companies.get(companyIndex++ % companies.size()),
                        "Pracownik Produkcji",
                        "Junior",
                        "Oferta pracy na stanowisku Pracownika Produkcji w zakładzie produkcyjnym. Możliwość przyuczenia do pracy.",
                        List.of(
                                "Praca na linii produkcyjnej",
                                "Obsługa prostych maszyn",
                                "Kontrola wizualna produktów",
                                "Pakowanie produktów",
                                "Przestrzeganie instrukcji stanowiskowych"
                        ),
                        List.of(
                                "Obsługa stanowiska produkcyjnego",
                                "Montaż i pakowanie produktów",
                                "Kontrola jakości wyrobów",
                                "Uzupełnianie materiałów produkcyjnych",
                                "Dbanie o porządek na stanowisku",
                                "Przestrzeganie zasad BHP"
                        ),
                        List.of(
                                "Gotowość do pracy zmianowej",
                                "Sprawność manualna",
                                "Dokładność",
                                "Odpowiedzialność",
                                "Gotowość do pracy fizycznej",
                                "Doświadczenie produkcyjne będzie dodatkowym atutem"
                        ),
                        "Brzeg",
                        "ON_SITE",
                        BigDecimal.valueOf(6500),
                        BigDecimal.valueOf(8500)
                )
        );

        create(
                organizationId,
                userIds.get(userIndex++ % userIds.size()),
                new CreateJobDescriptionInput(
                        companies.get(companyIndex++ % companies.size()),
                        "Magazynier",
                        "Mid",
                        "Poszukujemy Magazyniera do obsługi magazynu materiałów produkcyjnych i wyrobów gotowych.",
                        List.of(
                                "Obsługa wózka widłowego",
                                "Praca ze skanerem magazynowym",
                                "Kompletacja zamówień",
                                "Przyjmowanie dostaw",
                                "Kontrola stanów magazynowych"
                        ),
                        List.of(
                                "Przyjmowanie i wydawanie towaru",
                                "Kompletowanie zamówień",
                                "Rozładunek dostaw",
                                "Przygotowywanie materiałów dla produkcji",
                                "Obsługa systemu magazynowego",
                                "Dbanie o porządek w magazynie"
                        ),
                        List.of(
                                "Doświadczenie w pracy magazynowej",
                                "Uprawnienia UDT na wózki widłowe",
                                "Gotowość do pracy zmianowej",
                                "Umiejętność pracy ze skanerem",
                                "Dobra organizacja pracy",
                                "Odpowiedzialność"
                        ),
                        "Opole",
                        "ON_SITE",
                        BigDecimal.valueOf(7000),
                        BigDecimal.valueOf(10000)
                )
        );

        create(
                organizationId,
                userIds.get(userIndex++ % userIds.size()),
                new CreateJobDescriptionInput(
                        companies.get(companyIndex++ % companies.size()),
                        "Kontroler Jakości",
                        "Mid",
                        "Stanowisko w dziale kontroli jakości odpowiedzialnym za kontrolę wyrobów produkcyjnych.",
                        List.of(
                                "Kontrola jakości",
                                "Czytanie rysunku technicznego",
                                "Pomiary przyrządami kontrolnymi",
                                "Dokumentacja jakościowa",
                                "Analiza niezgodności"
                        ),
                        List.of(
                                "Kontrola jakości wyrobów",
                                "Wykonywanie pomiarów",
                                "Dokumentowanie wyników kontroli",
                                "Identyfikowanie niezgodności",
                                "Współpraca z działem produkcji",
                                "Udział w działaniach korygujących"
                        ),
                        List.of(
                                "Doświadczenie w kontroli jakości",
                                "Umiejętność czytania rysunku technicznego",
                                "Znajomość przyrządów pomiarowych",
                                "Dokładność i skrupulatność",
                                "Umiejętność analizy problemów",
                                "Znajomość podstaw systemów jakości będzie atutem"
                        ),
                        "Krapkowice",
                        "ON_SITE",
                        BigDecimal.valueOf(7500),
                        BigDecimal.valueOf(11000)
                )
        );
    }

    private void create(
            UUID organizationId,
            UUID userId,
            CreateJobDescriptionInput input
    ) {
        jobDescriptionApi.create(
                organizationId,
                userId,
                input
        );
    }
}