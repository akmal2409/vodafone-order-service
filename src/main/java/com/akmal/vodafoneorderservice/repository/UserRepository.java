package com.akmal.vodafoneorderservice.repository;

import com.akmal.vodafoneorderservice.model.User;
import java.util.Optional;

/**
 * Interface that defines a contract for a user repository to perform CRUD operations.
 * NOTE: Even though the assignment stated to call the API endpoint to check if the user email is valid, I would not personally do that
 * and I would definitely denormalize the data and bring subset of the user information into the microservice's internal db.
 * Doing that I will improve resiliency and reduce the domain coupling between services.
 */
public interface UserRepository {

  Optional<User> findByEmail(String email);

  void save(User user);
}
