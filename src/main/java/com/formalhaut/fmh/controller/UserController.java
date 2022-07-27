package com.formalhaut.fmh.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.formalhaut.fmh.entity.UserEntity;
import com.formalhaut.fmh.model.LoginRequest;
import com.formalhaut.fmh.model.UserResponse;
import com.formalhaut.fmh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;

@RestController
public class UserController {

    @Value("${fmh.app.jwtExpirationMs}")
    public Long JWT_TOKEN_VALIDITY;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/test")
    public UserResponse get(){
        UserEntity userEntity = this.userRepository.findByUsername("zenn");
        return userEntity.toUser();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception {
        authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        UserEntity userEntity = this.userRepository.findByUsername(loginRequest.getUsername());
        String accessToken = JWT.create().withIssuer("com.formalhaut.fmh").withClaim("username", userEntity.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)).sign(Algorithm.HMAC256("secret"));
        HashMap<String, String> map = new HashMap<>();
        map.put("tokenType", "Bearer");
        map.put("accessToken", accessToken);
        return ResponseEntity.ok(map);
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
