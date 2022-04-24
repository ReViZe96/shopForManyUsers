package com.springsecurity.practise.services;

import com.springsecurity.practise.entities.Clothes;
import com.springsecurity.practise.entities.Role;
import com.springsecurity.practise.repositories.RoleRepository;
import com.springsecurity.practise.repositories.UserRepository;
import lombok.AllArgsConstructor;
import com.springsecurity.practise.entities.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Transactional
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public List<User> findByRole(String role) {
        Role currRole = roleRepository.findByName(role);
        List<User> allUsers = userRepository.findAll();
        List<User> searchedUsers = new ArrayList<>();
        for (int i = 0; i < allUsers.size(); i++) {
            Collection<Role> currRoles = allUsers.get(i).getAuthorities();
            for (Role r : currRoles) {
                if (r.equals(currRole)) {
                    searchedUsers.add(allUsers.get(i));
                    break;
                }
            }
        }
        return searchedUsers;
    }

    @Transactional
    public User getCurrentUser(Principal principal) {
        return userRepository.findByUsername(principal.getName());
    }

    @Transactional
    public boolean saveUser(User user) {
        List<User> allUsers = userRepository.findAll();
        boolean res = true;
        for (User u : allUsers) {
            if (u.getUsername().equals(user.getUsername()) || u.getEmail().equals(user.getEmail())) {
                res = false;
                break;
            }
        }
        if (res) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            userRepository.save(user);
        }
        return res;
    }

    @Transactional
    public void saveCloth(User user, Clothes clothes) {
        user.addClothe(clothes);
    }

    @Transactional
    public void addRole(Long userId, String role) {
        User currUser = userRepository.findById(userId).get();
        Role currRole = roleRepository.findByName(role);
        Collection<Role> usersRoles = currUser.getAuthorities();
        boolean equals = false;
        for (Role r : usersRoles) {
            if (r.equals(currRole)) {
                equals = true;
                break;
            }
        }
        if (!equals) {
            currUser.getAuthorities().add(currRole);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteCurrRole(Long roleId, Long userId) {
        Role currRole = roleRepository.findById(roleId).get();
        User currUser = userRepository.findById(userId).get();
        currUser.getAuthorities().remove(currRole);
    }

    @Modifying
    @Transactional
    public void setUsername(Long id, String newUsername) {
        User curUser = userRepository.findById(id).get();
        curUser.setUsername(newUsername);
    }

    @Transactional
    public void setEmail(Long id, String newEmail) {
        User curUser = userRepository.findById(id).get();
        curUser.setEmail(newEmail);
    }

    @Transactional
    public void setPassword(Long id, String newPassword) {
        User curUser = userRepository.findById(id).get();
        curUser.setPassword(new BCryptPasswordEncoder().encode(newPassword));
    }

    //доступ к БД через UserDetailsService: один из тёх способов настройки доступа.
    // Здесь - пользователи и роли хранятся в отдельных таблицах БД
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Неверный логин и/или пароль");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                mapRolesToAuthorities(user.getAuthorities()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}