package com.erozah.kestachet.domain.port.out;

import com.erozah.kestachet.domain.model.Product;
import java.util.Optional;

public interface ExternalCatalogPort {
    Optional<Product> findByGtin(String gtin);
}
