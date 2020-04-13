package com.possible.ppmtool.services;

import com.possible.ppmtool.model.User;
import com.possible.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User existingUserDetails = userRepository.findByUsername(username);

        if(existingUserDetails == null){
            throw  new UsernameNotFoundException("User not found");
        }

        return existingUserDetails;
    }

    @Transactional
    public User loadUserById(Long id){
//        User existingUser = userRepository.findById(id).get();
        User existingUser = userRepository.getById(id);  // another option

        if(existingUser == null){
            throw  new UsernameNotFoundException("User not found");
        }
        return existingUser;
    }
}
