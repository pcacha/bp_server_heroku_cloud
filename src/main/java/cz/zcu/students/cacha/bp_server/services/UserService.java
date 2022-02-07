package cz.zcu.students.cacha.bp_server.services;

import cz.zcu.students.cacha.bp_server.domain.User;
import cz.zcu.students.cacha.bp_server.repositories.RoleRepository;
import cz.zcu.students.cacha.bp_server.repositories.UserRepository;
import cz.zcu.students.cacha.bp_server.security.JwtTokenProvider;
import cz.zcu.students.cacha.bp_server.responses.JWTLoginSuccessResponse;
import cz.zcu.students.cacha.bp_server.view_models.UsernamePasswordVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static cz.zcu.students.cacha.bp_server.security.SecurityConstants.TOKEN_PREFIX;
import static cz.zcu.students.cacha.bp_server.shared.RolesConstants.ROLE_TRANSLATOR;

/**
 * Class represent service which is responsible for user operations
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Registeres an user in the system
     * @param user valid user
     */
    public void save(User user) {
        // encode password
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        // set translator role and save new user
        user.getRoles().add(roleRepository.findByName(ROLE_TRANSLATOR).get());
        userRepository.save(user);
    }

    /**
     * Checks authentication and returns jwt based on given credentials
     * @param usernamePasswordVM username and password
     * @return jwt
     */
    public JWTLoginSuccessResponse login(UsernamePasswordVM usernamePasswordVM) {
        // try to authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usernamePasswordVM.getUsername(),
                        usernamePasswordVM.getPassword()
                )
        );

        // generate new jwt for logged in user and return it
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);

        return new JWTLoginSuccessResponse(jwt);
    }

    /**
     * Returns new jwt for authenticated user
     * @return jwt
     */
    public JWTLoginSuccessResponse getFreshToken() {
        return new JWTLoginSuccessResponse(TOKEN_PREFIX + tokenProvider.generateToken(SecurityContextHolder.getContext().getAuthentication()));
    }

    /**
     * Updates user's username and email
     * @param user logged in user
     * @param username updated username
     * @param email updated email
     */
    public void updateUser(User user, String username, String email) {
        user.setUsername(username);
        user.setEmail(email);
        userRepository.save(user);
    }

    /**
     * Changes user's password
     * @param user logged in user
     * @param password changed password
     */
    public void updatePassword(User user, String password) {
        // encode and save new password
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }
}
