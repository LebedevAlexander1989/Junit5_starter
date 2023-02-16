package org.example.junit.service;

import org.example.junit.dto.User;
import org.example.junit.paramresolver.UserServiceParamResolver;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ExtendWith(UserServiceParamResolver.class)
class UserServiceTest {

    private static final User IVAN = User.of(1, "Ivan", "123");
    private static final User PETR = User.of(2, "Petr", "345");

    private UserService userService;

    @BeforeAll
    static void init() {
        System.out.println("Before all: ") ;
    }

    @BeforeEach
    void prepare(UserService userService) {
        System.out.println("Before each: " + this);
        this.userService = userService;
    }

    @Test
    void usersEmptyIfNotUserAdded() {
        System.out.println("Test 1: " + this);
        var users = userService.getAll();
        assertThat(users).hasSize(0);
    }

    @Test
    void userSizeIfUserAdd() {
        System.out.println("Test 2: " + this);
        userService.add(IVAN);
        userService.add(PETR);

        var users = userService.getAll();
        assertThat(users).hasSize(2);
    }

    @Test
    void usersConvertedToMapById() {
        System.out.println("Test 6: " + this);
        userService.add(IVAN, PETR);
        Map<Integer, User> users = userService.getALLConvertedById();

        MatcherAssert.assertThat(users, hasKey(IVAN.getId()));
        assertAll(
                () -> assertThat(users).containsKeys(IVAN.getId(), PETR.getId()),
                () -> assertThat(users).containsValues(IVAN, PETR)
        );
    }

    @Test
    void throwExceptionIfNameOrPasswordIsNull() {
        System.out.println("Test 7: " + this);
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () ->  userService.login(null, "123")),
                () -> assertThrows(IllegalArgumentException.class, () ->  userService.login(IVAN.getName(), null))
        );
    }

    @AfterEach
    void deleteDataFromDatabase() {
        System.out.println("After each: " + this);
    }

    @AfterAll
    static void closeConnectionPool() {
        System.out.println("After all: ");
    }

    @DisplayName("test user login functionality")
    @Nested
    @Tag("login")
    class LoginTest {
        @Test
        @Tag("login")
        @DisplayName("Login success if user exist")
        void loginSuccessIfUserExist() {
            System.out.println("Test 3: " + this);
            userService.add(IVAN);

            Optional<User> mayBeUser = userService.login(IVAN.getName(), IVAN.getPassword());
            assertThat(mayBeUser).isPresent();
            assertThat(mayBeUser.get()).isEqualTo(IVAN);
        }

        @Test
        @Tag("login")
        void loginFailIfPasswordIsNotCorrect() {
            System.out.println("Test 4: " + this);
            userService.add(PETR);

            Optional<User> mayBeUser = userService.login(PETR.getName(), "123");
            assertThat(mayBeUser).isEmpty();
        }

        @Test
        @Tag("login")
        void loginFailIfUserDoesNotExists() {
            System.out.println("Test 5: " + this);
            userService.add(PETR);

            Optional<User> mayBeUser = userService.login("ptr", PETR.getPassword());
            assertThat(mayBeUser).isEmpty();
        }
    }
}
