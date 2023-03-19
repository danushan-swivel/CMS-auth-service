package com.cms.auth.repository;

import com.cms.auth.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query(value = "SELECT * FROM user u WHERE u.is_deleted=false AND u.user_name =?1", nativeQuery = true)
    User findByUserName(String userName);

    boolean existsByUserNameAndIsDeletedFalse(String userName);

    @Query(value = "SELECT * FROM user u WHERE u.is_deleted=false", nativeQuery = true)
    Page<User> findAll(Pageable pageable);
}
