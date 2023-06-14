package com.solvd.micro9.vkontaktah.web.controller;

import com.solvd.micro9.vkontaktah.domain.User;
import com.solvd.micro9.vkontaktah.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @QueryMapping("getAllUsers")
    public List<User> getAll(@Argument final int size,
                             @Argument final int page) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getAll(pageable);
    }

    @MutationMapping("saveUser")
    public User save(@Argument final User user) {
        return userService.save(user);
    }

}
