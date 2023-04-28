package com.pluxity.webflux;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureWebTestClient
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    @Rollback
    void createUserTest() {
        User user = User.builder()
                .name("test")
                .email("test@abc.com")
                .build();

        StepVerifier.create(userService.createUser(user))
                .assertNext(savedUser -> {
                    assertNotNull(savedUser.getId());
                    assertEquals(user.getName(), savedUser.getName());
                    assertEquals(user.getEmail(), savedUser.getEmail());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("초당 1000명의 유저 저장. 총 60초")
    void saveUsersAtHighRate() {
        int usersPerSecond = 1000;
        int totalDurationInSeconds = 60;
        int totalUsersToSave = usersPerSecond * totalDurationInSeconds;

        Flux<User> usersToSave = Flux.range(1, totalUsersToSave)
                .flatMap(i -> {
                    User user = User.builder()
                            .name("User_" + i)
                            .email("user_" + i + "@example.com")
                            .build();
                    return userService.createUser(user);
                }, usersPerSecond);

        StepVerifier.create(usersToSave)
                .expectNextCount(totalUsersToSave)
                .verifyComplete();
    }

}
