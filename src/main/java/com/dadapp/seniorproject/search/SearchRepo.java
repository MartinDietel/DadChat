package com.dadapp.seniorproject.search;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchRepo extends JpaRepository <Search, Long> {


    List<Search> findAll();

}
