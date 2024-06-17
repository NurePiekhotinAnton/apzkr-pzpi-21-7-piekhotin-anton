package org.safehouse.housemicroservice.repository;

import org.safehouse.housemicroservice.model.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HouseRepository extends JpaRepository<House, Long>	{

	List<House> findByUserId(Long userId);
}
