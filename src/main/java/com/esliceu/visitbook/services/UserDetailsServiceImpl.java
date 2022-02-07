package com.esliceu.visitbook.services;

import com.esliceu.visitbook.configuration.MyConfig;
import com.esliceu.visitbook.models.Person;
import com.esliceu.visitbook.repos.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.authentication.PasswordEncoderParser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    PersonRepo personRepo;

    @Autowired
    HttpSession session;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepo.getPersonByEmail(username);
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(person.getRole()));
        session.setAttribute("person",person);

        return new User(person.getEmail(), person.getPassword(), roles);
    }

    public void newUser(String email, String password, String firstName, String lastName, MultipartFile photo) throws Exception{
            Person person = new Person();
            person.setEmail(email);
            person.setPassword(MyConfig.getPasswordEncoder().encode(password));
            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.setPhoto(photo.getBytes());
            person.setRole(String.valueOf(Person.Role.ROLE_USER));
            personRepo.save(person);
    }

}
