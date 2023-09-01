package com.practice.session2.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface  UserRepository extends JpaRepository<User, Long> {

    public User findByUserName(String userName);

    //OQL (Object Query Language)
    @Query(value = "SELECT u FROM Users u WHERE u.userName = ?1")
    public User findByUserName2(String userName);

    //(SQL)
    @Query(value = "SELECT * FROM Users u WHERE u.userName = ?1", nativeQuery = true)
    public User findByUserName3(String userName);


}
