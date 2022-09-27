package com.dadapp.seniorproject.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepo extends JpaRepository<com.dadapp.seniorproject.event.Event, Long> {
    List<com.dadapp.seniorproject.event.Event> findAll();
}
