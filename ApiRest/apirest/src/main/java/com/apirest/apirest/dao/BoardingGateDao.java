package com.apirest.apirest.dao;

import com.apirest.apirest.entities.BoardingGate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardingGateDao extends JpaRepository <BoardingGate,Integer> {
}
