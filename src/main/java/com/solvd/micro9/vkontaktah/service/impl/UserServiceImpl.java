package com.solvd.micro9.vkontaktah.service.impl;

import com.solvd.micro9.vkontaktah.domain.Gender;
import com.solvd.micro9.vkontaktah.domain.User;
import com.solvd.micro9.vkontaktah.persistence.UserRepository;
import com.solvd.micro9.vkontaktah.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).toList();
    }

    @Override
    public User save(final User user) {
        if (user.getGender() == null) {
            user.setGender(Gender.UNSET);
        }
        return userRepository.save(user);
    }

}
