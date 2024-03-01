package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entities.Users;
import com.example.demo.repositories.UsersRepository;



public interface UsersService {
	
	  public static final UsersRepository usersRepository = null;
	  
	  
    String addUser(Users user);
    boolean emailExists(String email);
    boolean validateUser(String email, String password);
    String getRole(String email);
    Users getUser(String email);
   
    @Transactional
    public static void updateUser(Users user) {
        usersRepository.save(user);
    }

        
	boolean isUserPremium(String userEmail);
}
