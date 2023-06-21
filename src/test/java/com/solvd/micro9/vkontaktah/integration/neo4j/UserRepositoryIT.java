package com.solvd.micro9.vkontaktah.integration.neo4j;

import com.solvd.micro9.vkontaktah.domain.Gender;
import com.solvd.micro9.vkontaktah.domain.User;
import com.solvd.micro9.vkontaktah.persistence.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = Neo4ITConfig.class)
public class UserRepositoryIT extends Neo4jTestcontainers {

    @Autowired
    private UserRepository userRepository;

    @Test
    void verifyAllUsersAreReceived() {
        List<User> users = userRepository.findAll();
        Assertions.assertNotNull(users);
        Assertions.assertEquals(userRepository.count(), users.size());
    }

    @ParameterizedTest(
            name = "verifyUserIsFoundById "
                    + "[id={0},login={1},firstName={2},lastName={3},gender={4},age={5}]"
    )
    @CsvSource({
            "10, liza123, Liza, Ya, FEMALE, 20"
    })
    void verifyUserIsFoundById(
            final String id, final String login, final String firstName,
            final String lastName, final Gender gender, final int age
    ) {
        Optional<User> user = userRepository.findById(id);
        Assertions.assertNotNull(user);
        Assertions.assertTrue(user.isPresent());
        Assertions.assertAll(
                () -> Assertions.assertEquals(user.get().getId(), id),
                () -> Assertions.assertEquals(user.get().getLogin(), login),
                () -> Assertions.assertEquals(user.get().getFirstName(), firstName),
                () -> Assertions.assertEquals(user.get().getLastName(), lastName),
                () -> Assertions.assertEquals(user.get().getGender(), gender),
                () -> Assertions.assertEquals(user.get().getAge(), age)
        );
    }

    @ParameterizedTest(name = "verifyUserIsNotFoundById [id={0}]")
    @ValueSource(strings = {"634243HJGDGSD120000000000"})
    void verifyUserIsNotFoundById(final String id) {
        Optional<User> user = userRepository.findById(id);
        Assertions.assertNotNull(user);
        Assertions.assertFalse(user.isPresent());
    }

    @ParameterizedTest(
            name = "verifyUserIsSaved "
                    + "[id={0},login={1},firstName={2},lastName={3},gender={4},age={5}]"
    )
    @CsvSource({
            "1489283, newUser, Petr, Petrov, MALE, 31"
    })
    void verifyUserIsSaved(
            final String id, final String login, final String firstName,
            final String lastName, final Gender gender, final int age
    ) {
        User user = User.builder()
                .id(id)
                .login(login)
                .firstName(firstName)
                .lastName(lastName)
                .gender(gender)
                .age(age)
                .build();
        userRepository.save(user);
        this.verifyUserIsFoundById(id, login, firstName, lastName, gender, age);
    }

}
