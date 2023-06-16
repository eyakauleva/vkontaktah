package com.solvd.micro9.vkontaktah.service;

import com.solvd.micro9.vkontaktah.domain.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    List<User> getAll(Pageable pageable);

    User findById(String id);

    User save(User user);

}
