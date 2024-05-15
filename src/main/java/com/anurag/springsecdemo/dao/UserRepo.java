package com.anurag.springsecdemo.dao;


import com.anurag.springsecdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Integer>{

    User findByUsername(String username);


}
