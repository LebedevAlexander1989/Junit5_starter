package org.example.junit.service;

import org.example.junit.dto.User;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.junit.jupiter.api.Assertions.assertAll;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class UserServiceTest {

    private static final User IVAN = User.of(1, "Ivan", "123");
    private static final User PETR = User.of(2, "Petr", "345");

    private UserService userService;

    @BeforeAll
    static void init() {
        System.out.println("Before all: ") ;
    }

    @BeforeEach
    void prepare() {
        userService = new UserService();
        System.out.println("Before each: " + this);
    }

    @Test
    void usersEmptyIfNotUserAdded() {
        System.out.println("Test 1: " + this);
        var users = userService.getAll();
    //    assertTrue(users.isEmpty(), "Users empty should be empty");
        assertThat(users).hasSize(0);
    }

    @Test
    void userSizeIfUserAdd() {
        System.out.println("Test 2: " + this);
        userService.add(IVAN);
        userService.add(PETR);

        var users = userService.getAll();
      //  assertEquals(2, users.size());
        assertThat(users).hasSize(2);
    }

    @Test
    void loginSuccessIfUserExist() {
        System.out.println("Test 3: " + this);
        userService.add(IVAN);

        Optional<User> mayBeUser = userService.login(IVAN.getName(), IVAN.getPassword());
       // assertTrue(mayBeUser.isPresent());
      //  assertEquals(IVAN, mayBeUser.get());
        assertThat(mayBeUser).isPresent();
        assertThat(mayBeUser.get()).isEqualTo(IVAN);
    }

    @Test
    void loginFailIfPasswordIsNotCorrect() {
        System.out.println("Test 4: " + this);
        userService.add(PETR);

        Optional<User> mayBeUser = userService.login(PETR.getName(), "123");
       // assertTrue(mayBeUser.isEmpty());
        assertThat(mayBeUser).isEmpty();
    }

    @Test
    void loginFailIfUserDoesNotExists() {
        System.out.println("Test 5: " + this);
        userService.add(PETR);

        Optional<User> mayBeUser = userService.login("ptr", PETR.getPassword());
       // assertTrue(mayBeUser.isEmpty());
        assertThat(mayBeUser).isEmpty();
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

    @AfterEach
    void deleteDataFromDatabase() {
        System.out.println("After each: " + this);
    }

    @AfterAll
    static void closeConnectionPool() {
        System.out.println("After all: ");
    }
}
