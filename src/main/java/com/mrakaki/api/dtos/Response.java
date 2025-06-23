package com.mrakaki.api.dtos;

import java.util.Optional;

public record Response<Type>(Type data, Optional<Error> error, int actualPage, int totalPages, String eTag) {

}
