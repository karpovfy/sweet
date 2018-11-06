package com.fyodork.hello.service;


import com.fyodork.hello.domain.Role;
import com.fyodork.hello.domain.User;
import com.fyodork.hello.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private final UserRepo userRepo;

    @Autowired

    private MailSender mailSender;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //return userRepo.findByUsername(username);

        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;

    }


    public boolean addUser(User user)
    {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb!=null)
        {
            return false;
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        if(!StringUtils.isEmpty(user.getEmail()))
        {

            String message=String.format("Hello,%s!\n" +
                    "Welcome to sweet" +
                    "Please visit next link: http://localhost:8080/activate/%s,",
                    user.getUsername(),user.getActivationCode());
            mailSender.send(user.getEmail(),"activationCode",message);
        }
        return true;

    }


    public boolean activateUser(String code) {
       User user= userRepo.findByActivationCode(code);
       if (user==null)
       {
           return false;
       }

       user.setActivationCode(null);
       userRepo.save(user);
        return true;
    }

    public List<User> findAll() {

        return userRepo.findAll();
    }

    public void saveUser(User user, String userNAME, Map<String, String> form) {
        user.setUsername(userNAME);


        Set<String> roles = Arrays.stream(Role.values()).
                map(Role::name).
                collect(Collectors.toSet());

        user.getRoles().clear();


        for (String key:form.keySet())
        {
            if(roles.contains(key))
            {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepo.save(user);
    }



    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Sweater. Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }


    public void updateProfie(User user, String password, String email) {


        String userEmail = user.getEmail();
        boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email));

        if (isEmailChanged)
        {

            user.setEmail(email);
            if (!StringUtils.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());
            }


        }

        if (!StringUtils.isEmpty(password)) {
            user.setPassword(password);
        }

        userRepo.save(user);

        if (isEmailChanged) {
            sendMessage(user);
        }

    }
}
