package com.librarymanagement.API.repository;

import com.librarymanagement.API.entity.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronRepository extends JpaRepository<Patron, Long> {
}