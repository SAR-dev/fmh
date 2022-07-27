package com.formalhaut.fmh.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.formalhaut.fmh.entity.UserEntity;
import com.formalhaut.fmh.model.Login;
import com.formalhaut.fmh.model.User;
import com.formalhaut.fmh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
public class UserController {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/test")
    public User get(){
        UserEntity userEntity = this.userRepository.findByUsername("zenn");
        return userEntity.toUser();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) throws Exception {
        authenticate(login.getUsername(), login.getPassword());
        UserEntity userEntity = this.userRepository.findByUsername(login.getUsername());
        String token = JWT.create().withIssuer("com.formalhaut.fmh").withClaim("username", userEntity.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)).sign(Algorithm.HMAC256("secret"));
        return ResponseEntity.ok(token);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}
