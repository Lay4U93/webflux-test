package com.pluxity.webflux;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;



    public Mono<User> createUser(User user){
        return userRepository.save(user);
    }

    public Mono<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public Flux<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Mono<User> updateUser(Long id, User user) {
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.update(user.getName(), user.getEmail());
                    return userRepository.save(existingUser);
                });
    }

    public Mono<Void> deleteUser(Long id){
        return userRepository.deleteById(id);
    }

}
