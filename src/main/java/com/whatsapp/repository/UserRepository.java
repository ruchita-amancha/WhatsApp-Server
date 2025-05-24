package com.whatsapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.whatsapp.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>{

	User findByEmail(String email);
	
	@Query("select u from User u where u.full_name like %:query%" )
	List<User> searchUser(@Param("query") String query);
}
