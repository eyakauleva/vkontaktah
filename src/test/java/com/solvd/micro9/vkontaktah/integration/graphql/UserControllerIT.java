package com.solvd.micro9.vkontaktah.integration.graphql;

import com.solvd.micro9.vkontaktah.domain.User;
import com.solvd.micro9.vkontaktah.service.UserService;
import com.solvd.micro9.vkontaktah.web.controller.UserController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;

@GraphQlTest(UserController.class)
@Import(ControllerITConfig.class)
public class UserControllerIT {

    @Autowired
    private GraphQlTester tester;

    @MockBean
    private UserService userService;

    @ParameterizedTest
    @MethodSource("com.solvd.micro9.vkontaktah.TestDataProvider#getUsers")
    void verifyAllUsersAreReceived(final List<User> usersToMock) {
        String query = "{ getAllUsers(size:10, page:0) "
                + "{ id login firstName lastName gender age } }";
        Mockito.when(userService.getAll(Mockito.any(Pageable.class)))
                .thenReturn(usersToMock);
        List<User> resultUsers = this.tester.document(query)
                .execute()
                .path("data.getAllUsers[*]")
                .entityList(User.class)
                .get();
        Mockito.verify(userService, Mockito.times(1))
                .getAll(Mockito.any(Pageable.class));
        Assertions.assertNotNull(resultUsers);
        Assertions.assertTrue(resultUsers.size() > 0);
    }

    @ParameterizedTest
    @CsvSource(value =
            {
                "null, 10",
                "10, null",
                "null, null",
                "0, 0",
                "1, -10",
                "0, -10"
            },
            nullValues = {"null"}
    )
    void verifyArgsAreValidatedWhenGetAllUsers(
            final Integer size, final Integer page
    ) {
        String query = "{ getAllUsers(size:" + size + ", page:" + page + ") "
                + "{ id login firstName lastName gender age } }";
        Assertions.assertThrows(
                Throwable.class,
                () -> this.tester.document(query)
                        .execute()
                        .path("data.getAllUsers[*]")
                        .entityList(User.class)
                        .get()
        );
    }

    @Test
    void verifyUserIsSaved() {
        String login = "iviviv1";
        String firstName = "Ivan1";
        String lastName = "Ivanov1";
        String query = "mutation { saveUser (user: { login: \"" + login
                + "\" firstName: \"" + firstName + "\" lastName: \"" + lastName + "\" }) "
                + "{ id login firstName lastName gender age } }";
        Mockito.when(userService.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        User resultUser = this.tester.document(query)
                .execute()
                .path("data.saveUser")
                .entity(User.class)
                .get();
        Mockito.verify(userService, Mockito.times(1))
                .save(Mockito.any(User.class));
        Assertions.assertNotNull(resultUser);
        Assertions.assertEquals(login, resultUser.getLogin());
        Assertions.assertEquals(firstName, resultUser.getFirstName());
        Assertions.assertEquals(lastName, resultUser.getLastName());
    }

    @ParameterizedTest
    @CsvSource(value =
            {
                "null, firstname, lastname, 25",
                "'', firstname, lastname, 25",
                "login, null, lastname, 25",
                "login, '', lastname, 25",
                "login, firstname, null, 25",
                "login, firstname, '', 25",
                "login, firstname, lastname, -10",
                "login, firstname, lastname, 100",
                "login, firstname, lastname, 20.54",
            },
            nullValues = {"null"}
    )
    void verifyArgsAreValidatedWhenSaveUser(
            final String login, final String firstName,
            final String lastName, final Float age
    ) {
        String query = "mutation { saveUser (user: { login: \"" + login
                + "\" firstName: \"" + firstName + "\" lastName: \""
                + lastName + "\" age: " + age + " }) "
                + "{ id login firstName lastName gender age } }";
        Assertions.assertThrows(
                Throwable.class,
                () -> this.tester.document(query)
                        .execute()
                        .path("data.saveUser")
                        .entity(User.class)
                        .get()
        );
    }

}
