package com.boun.swe.mnemosyne.repository;

import com.boun.swe.mnemosyne.model.Memory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {
    List<Memory> findByIsPublishedTrueAndIsPublicTrue();
}
