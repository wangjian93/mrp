package com.ivo.mrp.repository;

import com.ivo.mrp.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author wj
 * @version 1.0
 */
public interface PositionRepository extends JpaRepository<Position, Long> {

    @Query(value = "select distinct p.position from Position p where p.fab=:fab and p.type=:type")
    List<String> getPosition(@Param("fab") String fab, @Param("type") int type);


    @Query(value = "select distinct p.position from Position p")
    List<String> getPositionAll();
}
