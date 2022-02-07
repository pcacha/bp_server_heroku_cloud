package cz.zcu.students.cacha.bp_server.services;

import cz.zcu.students.cacha.bp_server.domain.User;
import cz.zcu.students.cacha.bp_server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Class represent service which is responsible for authorization operations
 */
@Service
public class AuthUserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    /**
     * Gets user by his username
     * @param username username
     * @return user details of found user
     * @throws UsernameNotFoundException exception
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // try to find user
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()) {
            // throw exception if user not found
            throw new UsernameNotFoundException("User not found");
        }
        return userOptional.get();
    }

    /**
     * Get user by his id
     * @param id user id
     * @return found user
     */
    public User loadUserById(Long id){
        // try to find user
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isEmpty())
        {
            // throw exception if user not found
            throw new UsernameNotFoundException("User not found");
        }
        return userOptional.get();
    }
}
