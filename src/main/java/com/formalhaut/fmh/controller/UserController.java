package com.formalhaut.fmh.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.formalhaut.fmh.entity.UserEntity;
import com.formalhaut.fmh.model.LoginRequest;
import com.formalhaut.fmh.model.UserIssueToken;
import com.formalhaut.fmh.repository.UserRepository;
import com.formalhaut.fmh.service.UserDetailsServiceImpl;
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
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @GetMapping("/test")
    public UserEntity get(){
        UserEntity userEntity = this.userRepository.findByUsername("zenn");
        return userEntity;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) throws Exception {
        authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        UserEntity userEntity = this.userRepository.findByUsername(loginRequest.getUsername());
        String accessToken = userDetailsService.createAccessToken(userEntity.getUsername());
        HashMap<String, String> map = new HashMap<>();
        map.put("tokenType", "Bearer");
        map.put("accessToken", accessToken);
        return ResponseEntity.ok(map);
    }

//    @PostMapping("/refreshToken")
//    public UserIssueToken refreshToken(@RequestBody UserIssueToken userIssueToken) {
//        final DecodedJWT jwt = JWT.decode(userIssueToken.getAccessToken());
//
//        try {
//            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("secret")).build();
//            DecodedJWT decodedJWT = verifier.verify(jwt);
//        } catch (TokenExpiredException e) {
//            // allow expired access token for user authentication
//        } catch (Exception e) {
//            throw e;
//        }
//
//        if (userDetailsService.verifyRefreshToken(jwt.getSubject(), userIssueToken.getRefreshToken())) {
//            return userDetailsService.issueToken(jwt.getSubject());
//        } else {
//            throw new BadCredentialsException("Invalid refresh token!");
//        }
//    }

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
