package com.erozah.kestachet.domain.port.in;

public interface ValidateCartUseCase {
    void validateAndArchiveCart(String listId, String userId);
}
