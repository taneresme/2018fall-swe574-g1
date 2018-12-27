package com.boun.swe.mnemosyne.repository;

import com.boun.swe.mnemosyne.enums.MemoryType;
import com.boun.swe.mnemosyne.model.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {

    List<Memory> findByTypeAndIsPublishedTrue(final MemoryType type);

    @Query(value = "SELECT m FROM Memory m where m.user.id = ?1")
    List<Memory> findAllMemoriesByUserId(final Long userId);

    @Query(value = "SELECT m FROM Memory m where m.type = ?1 and m.user.id = ?2")
    List<Memory> findAllMemoriesByTypeAndUserId(final MemoryType memoryType, final Long userId);

}
