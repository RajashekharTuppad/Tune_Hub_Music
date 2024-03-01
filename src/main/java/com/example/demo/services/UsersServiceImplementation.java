package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entities.Users;
import com.example.demo.repositories.UsersRepository;

@Service
public class UsersServiceImplementation implements UsersService {
    @Autowired
    UsersRepository repo;

    @Override
    public String addUser(Users user) {
        repo.save(user);
        return "user added successfully";
    }

    @Override
    public boolean emailExists(String email) {
        return repo.findByEmail(email) != null;
    }

    @Override
    public boolean validateUser(String email, String password) {
        Users user = repo.findByEmail(email);
        return user != null && user.getPassword().equals(password);
    }

    @Override
    public String getRole(String email) {
        Users user = repo.findByEmail(email);
        return user.getRole();
    }

    @Override
    public Users getUser(String email) {
        return repo.findByEmail(email);
    }

    @Override
    public void updateUser(Users user) {
        repo.save(user);
    }

	@Override
	public boolean isUserPremium(String userEmail) {
		// TODO Auto-generated method stub
		return false;
	}
}
