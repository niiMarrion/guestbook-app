package com.bt.guestbook.repository;

import com.bt.guestbook.model.User;
import com.bt.guestbook.utils.PassEncoding;
import com.bt.guestbook.utils.Roles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateUser(){
        User user = new User();
        user.setEmail("toffee@icecream.co.uk");
        user.setPassword(PassEncoding.getInstance().passwordEncoder.encode("harbourBar!!!"));
        user.setUsername("user");
        user.setRole(Roles.ROLE_USER.getValue());
        User savedUser = repository.save(user);

        User existingUser
                = entityManager.find(User.class, savedUser.getId());

        Assertions.assertTrue(existingUser.getEmail().equals(user.getEmail()));

    }

    @Test
    public void it_should_find_user_byEmail() {
        User user = new User();
        user.setEmail("testmail@test.com");
        user = entityManager.persistAndFlush(user);
        Assertions.assertTrue(repository.findByEmail(user.getEmail()).equals(user));
    }

    @Test
    public void find_user_username() {
        User user = new User();
        user.setUsername("test");
        user = entityManager.persistAndFlush(user);
        Assertions.assertTrue(repository.findByUsername(user.getUsername()).equals(user));
    }

}