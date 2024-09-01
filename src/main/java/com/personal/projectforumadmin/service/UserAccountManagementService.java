package com.personal.projectforumadmin.service;

import com.personal.projectforumadmin.dto.UserAccountDto;
import com.personal.projectforumadmin.dto.properties.ProjectProperties;
import com.personal.projectforumadmin.dto.response.UserAccountClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserAccountManagementService {

    private final RestTemplate restTemplate;
    private final ProjectProperties projectProperties;

    public List<UserAccountDto> getUserAccounts() {

        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.forum().url() + "/api/userAccounts")
                .queryParam("size", 10000) // TODO: A way to pass a size large enough to retrieve the entire post. It's incomplete.
                .build()
                .toUri();
        UserAccountClientResponse response = restTemplate.getForObject(uri, UserAccountClientResponse.class);

        return Optional.ofNullable(response).orElseGet(UserAccountClientResponse::empty).userAccounts();
    }

    public UserAccountDto getUserAccount(String userId) {

        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.forum().url() + "/api/userAccounts/" + userId)
                .build()
                .toUri();
        UserAccountDto response = restTemplate.getForObject(uri, UserAccountDto.class);

        return Optional.ofNullable(response)
                .orElseThrow(() -> new NoSuchElementException("There is no posting- userId: " + userId));
    }

    public void deleteUserAccount(String userId) {

        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.forum().url() + "/api/userAccounts/" + userId)
                .build()
                .toUri();
        restTemplate.delete(uri);
    }
}
