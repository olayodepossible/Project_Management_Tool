package com.possible.ppmtool.services;

import com.possible.ppmtool.exceptions.UserNameAlreadyExistException;
import com.possible.ppmtool.model.User;
import com.possible.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){

        try {
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setUsername(newUser.getUsername());
            newUser.setConfirmPassword("");
            return  userRepository.save(newUser);

        }catch (Exception e){
            throw new UserNameAlreadyExistException("Username '"+newUser.getUsername()+"' already Exist");
        }
    }
}
