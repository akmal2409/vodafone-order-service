package com.akmal.vodafoneorderservice.dto.userservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;

public record PagedResponse<T>(
    int page,
    @JsonProperty("per_page") int perPage,
    int total,
    @JsonProperty("total_pages") int totalPages,
    Collection<T> data
) {

}
