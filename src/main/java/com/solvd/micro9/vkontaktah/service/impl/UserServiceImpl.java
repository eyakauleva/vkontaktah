package com.solvd.micro9.vkontaktah.service.impl;

import com.solvd.micro9.vkontaktah.domain.Gender;
import com.solvd.micro9.vkontaktah.domain.User;
import com.solvd.micro9.vkontaktah.domain.exception.ResourceNotFoundException;
import com.solvd.micro9.vkontaktah.persistence.UserRepository;
import com.solvd.micro9.vkontaktah.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAll(final Pageable pageable) {
        return userRepository.findAll(pageable)
                .toList();
    }

    @Override
    public User findById(final String id) {
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "User [id=" + id + "] does not exist"
                        )
                );
    }

    @Override
    public User save(final User user) {
        if (user.getGender() == null) {
            user.setGender(Gender.UNSET);
        }
        user.setId(UUID.randomUUID().toString());
        return userRepository.save(user);
    }

}
