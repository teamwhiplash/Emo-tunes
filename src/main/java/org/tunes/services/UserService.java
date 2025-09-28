package org.tunes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tunes.models.User;
import org.tunes.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveOrUpdateUser(User user) {
        Optional<User> existingUser = Optional.ofNullable(userRepository.findBySpotifyId(user.getSpotifyId()));

        if (existingUser.isPresent()) {
            User dbUser = existingUser.get();
            dbUser.setRefreshToken(user.getRefreshToken());
            return (User) userRepository.save(dbUser);
        } else {
            System.out.println(user);
            return (User)userRepository.save(user);

        }
    }
}

