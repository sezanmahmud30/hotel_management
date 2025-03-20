package com.sezanmahmud.hotel_management.service;

import com.sezanmahmud.hotel_management.entity.AuthenticationResponse;
import com.sezanmahmud.hotel_management.entity.Role;
import com.sezanmahmud.hotel_management.entity.Token;
import com.sezanmahmud.hotel_management.entity.User;
import com.sezanmahmud.hotel_management.jwt.JwtService;
import com.sezanmahmud.hotel_management.repository.TokenRepository;
import com.sezanmahmud.hotel_management.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, TokenRepository tokenRepository, AuthenticationManager authenticationManager, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }


    private void saveUserToken(String jwt, User user){
        Token token = new Token();
        token.setToken(jwt);
        token.setLogout(false);
        token.setUser(user);

        tokenRepository.save(token);
    }

    private void removeAllTokenByUser(User user){
        List<Token> validTokens = tokenRepository.findAllTokenByUser(user.getId());

        if (validTokens.isEmpty()){
            return;
        }
        validTokens.forEach(t->{
            t.setLogout(true);
        });
        tokenRepository.saveAll(validTokens);
    }

    public AuthenticationResponse resgister(User user){
        //we check that already any user exists with this email id
        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            return new AuthenticationResponse(null, "User Already Exists");
        }

        //Encode user password to DB
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.valueOf("USER"));
        user.setLock(true);
        user.setActive(false);

        userRepository.save(user);

        String jwt = jwtService.generateToken(user);

        saveUserToken(jwt,user);

        sendActivationEmail(user);
        return new AuthenticationResponse(jwt, "User Registration was successful");
    }

    private void sendActivationEmail(User user) {
        String activationLink="http://localhost:8088/active/" + user.getId();
        String mailText = "<h2>Dear</h2>" + user.getName()+", "
                +"<p>Please Click on the following link to confirm your registration</p>"
                +"<a href=\""+activationLink+"\">Active Account</a>";
        String subject="Confirm Registration";

        try {
            emailService.sendSimpleEmail(user.getEmail(), subject, mailText);
        }catch (MessagingException e){
            throw new RuntimeException();
        }
    }

    public AuthenticationResponse authenticate(User request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        // Generate token for  current user
        String jwt = jwtService.generateToken(user);

        //Remove all existing token for this user
        removeAllTokenByUser(user);

        saveUserToken(jwt, user);
        return new AuthenticationResponse(jwt, "User Login successful");
    }


    public String activeUser(long id){

        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found with this id:" + id));

        if (user != null){
            user.setActive(true);
            userRepository.save(user);
            return "User Activated successfully!";
        }else {
            return "Invalid Activation token!";
        }

    }
}
