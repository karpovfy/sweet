package com.fyodork.hello.repos;

import com.fyodork.hello.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {


    User findByUsername(String username);
}
