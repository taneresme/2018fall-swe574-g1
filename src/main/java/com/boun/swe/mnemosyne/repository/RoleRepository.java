package com.boun.swe.mnemosyne.repository;

import com.boun.swe.mnemosyne.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
