package org.tunes.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tunes.models.Users;
import org.tunes.repositories.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Users saveOrUpdateUser(Users user) {
        Optional<Users> existingUser = Optional.ofNullable(userRepository.findBySpotifyId(user.getSpotifyId()));

        if (existingUser.isPresent()) {
            Users dbUser = existingUser.get();
            dbUser.setRefreshToken(user.getRefreshToken());
            return (Users) userRepository.save(dbUser);
        } else {
            System.out.println(user);
            return (Users)userRepository.save(user);

        }
    }
}

