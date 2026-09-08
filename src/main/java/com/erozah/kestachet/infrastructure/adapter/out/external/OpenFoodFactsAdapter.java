package com.erozah.kestachet.infrastructure.adapter.out.external;

import com.erozah.kestachet.domain.model.Product;
import com.erozah.kestachet.domain.port.out.ExternalCatalogPort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Component
public class OpenFoodFactsAdapter implements ExternalCatalogPort {

    private final RestClient restClient;

    public OpenFoodFactsAdapter() {
        this.restClient = RestClient.builder()
                .baseUrl("https://world.openfoodfacts.org/api/v2")
                .defaultHeader("User-Agent", "Kestachet-App - Version 1.0")
                .build();
    }

    @Override
    public Optional<Product> findByGtin(String gtin) {
        if (gtin == null || gtin.isBlank()) {
            return Optional.empty();
        }

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restClient.get()
                    .uri("/product/{gtin}.json", gtin)
                    .retrieve()
                    .body(Map.class);

            if (response != null && Integer.valueOf(1).equals(response.get("status"))) {
                @SuppressWarnings("unchecked")
                Map<String, Object> productMap = (Map<String, Object>) response.get("product");
                if (productMap != null) {
                    String name = (String) productMap.getOrDefault("product_name_fr", productMap.get("product_name"));
                    String brand = (String) productMap.get("brands");
                    String category = (String) productMap.get("categories");
                    String imageUrl = (String) productMap.get("image_front_url");

                    if (name != null && !name.isBlank()) {
                        return Optional.of(new Product(gtin, name, brand, category, BigDecimal.ZERO, imageUrl));
                    }
                }
            }
        } catch (Exception ignored) {
            // En cas d'indisponibilité de l'API externe, on n'échoue pas le domaine
        }

        return Optional.empty();
    }
}
