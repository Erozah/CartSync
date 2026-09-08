package com.erozah.kestachet.infrastructure.adapter.in.web;

import com.erozah.kestachet.domain.model.Product;
import com.erozah.kestachet.domain.port.out.ExternalCatalogPort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final ExternalCatalogPort catalogPort;

    public CatalogController(ExternalCatalogPort catalogPort) {
        this.catalogPort = catalogPort;
    }

    @GetMapping("/{gtin}")
    public ResponseEntity<Product> lookupProduct(@PathVariable("gtin") String gtin) {
        return catalogPort.findByGtin(gtin)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
