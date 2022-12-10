package com.akmal.vodafoneorderservice.repository.implementation;

import com.akmal.vodafoneorderservice.dto.userservice.PagedResponse;
import com.akmal.vodafoneorderservice.dto.userservice.UserDto;
import com.akmal.vodafoneorderservice.exception.UserInMemoryCacheInitialisationException;
import com.akmal.vodafoneorderservice.model.User;
import com.akmal.vodafoneorderservice.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Upon bean construction the class executes a blocking call to fetch the users from external service
 * and place them in a concurrent map.
 * In a real life setting this would not be the case since I would bring the required data to my service via eventing.
 */
@RequiredArgsConstructor
@Slf4j
public class InMemoryCachingUserRepository implements UserRepository {
  private final ConcurrentMap<String, User> emailToUserMap = new ConcurrentHashMap<>();
  private final RestTemplate restTemplate;

  /**
   * The method is invoked upon bean construction.
   * Firstly, it contacts the underlying external user service and fetches the user records.
   * If the server responds with anything other than 2xx response code, we throw an exception and stop the application from executing further.
   * Furthermore, if the data is not present and the API does not adhere to the predefined contract, we also throw an exception.
   * Lastly, if response data is in place, we populate the concurrent map, the reason why we are not using any locks or CAS operations
   * is because this method is ensured to run before the applcation starts accepting requests and will run in a single thread.
   */
  @PostConstruct
  public void fillCache() {
    log.debug("Initialising user in-memory cache");

    // as a side not, in real life I would never do it like this but to show my preference of data locality I am forced to do so
    final ResponseEntity<PagedResponse<UserDto>> response = this.restTemplate.exchange("/api/users?page=0&per_page=1000000",
        HttpMethod.GET, null,
        new ParameterizedTypeReference<PagedResponse<UserDto>>() {});

    if (response.getStatusCode().isError()) throw new UserInMemoryCacheInitialisationException("Failed to initialise in memory user repository. The API responded with status code "
                                                                                                   + response.getStatusCode());
    else if (response.getBody() == null || response.getBody().data() == null) throw new UserInMemoryCacheInitialisationException
                                                                                        ("Failed to initialise in-memory user repository. The API responded with no data");

    for (UserDto userDto: response.getBody().data()) {
      this.emailToUserMap.put(userDto.email(), new User(userDto.id(), userDto.firstName(), userDto.lastName(), userDto.email()));
    }

    log.debug("Finished initialisation of the in-memory cache. User count {}", response.getBody().data().size());
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return Optional.ofNullable(emailToUserMap.get(email));
  }
}
