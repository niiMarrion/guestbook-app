package com.bt.guestbook.repository;

import com.bt.guestbook.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String username);

    User findByEmail(String email);
}
