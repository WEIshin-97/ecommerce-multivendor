package com.snorlax.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snorlax.modal.Deal;

public interface DealRepository extends JpaRepository<Deal, Long> {

}
