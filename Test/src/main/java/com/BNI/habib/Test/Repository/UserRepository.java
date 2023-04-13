package com.BNI.habib.Test.Repository;

import com.BNI.habib.Test.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdAndIsActive(long id, boolean isActive);
    Page<User> findAll(Pageable pageable);
}
