package com.example.loginapp;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.userName = ?1")
	User findByUserName(String name);

}
