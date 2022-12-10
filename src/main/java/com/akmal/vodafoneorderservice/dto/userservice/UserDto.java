package com.akmal.vodafoneorderservice.dto.userservice;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * External API user representation.
 *
 * @param id
 * @param email
 * @param firstName
 * @param lastName
 * @param avatar
 */
public record UserDto(
    int id,
    String email,
    @JsonProperty("first_name") String firstName,
    @JsonProperty("last_name") String lastName,
    String avatar
) {

}
