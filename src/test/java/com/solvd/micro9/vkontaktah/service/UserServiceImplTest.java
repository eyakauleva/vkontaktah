package com.solvd.micro9.vkontaktah.service;

import com.solvd.micro9.vkontaktah.domain.Gender;
import com.solvd.micro9.vkontaktah.domain.User;
import com.solvd.micro9.vkontaktah.persistence.UserRepository;
import com.solvd.micro9.vkontaktah.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @ParameterizedTest
    @MethodSource("com.solvd.micro9.vkontaktah.TestDataProvider#getUsers")
    void verifyAllUsersAreReceived(final List<User> usersToMock) {
        Pageable pageable = PageRequest.of(0, 100);
        Mockito.when(userRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(
                        new PageImpl<>(
                                usersToMock,
                                pageable,
                                usersToMock.size()
                        )
                );
        List<User> resultUsers = userService.getAll(pageable);
        Mockito.verify(userRepository, Mockito.times(1))
                .findAll(Mockito.any(Pageable.class));
        Assertions.assertNotNull(resultUsers);
    }

    @ParameterizedTest
    @MethodSource("com.solvd.micro9.vkontaktah.TestDataProvider#getUser")
    void verifyUserIsSaved(final User userToSave) {
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        User resultUser = userService.save(userToSave);
        Assertions.assertNotNull(resultUser);
        Assertions.assertNotNull(userToSave.getId());
        Assertions.assertEquals(Gender.UNSET, userToSave.getGender());
    }

}
