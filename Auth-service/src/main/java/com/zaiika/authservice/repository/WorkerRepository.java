package com.zaiika.authservice.repository;

import com.zaiika.authservice.model.worker.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    @Query(value = """
            select *
            from workers
            where place_id = :#{#placeId}
            """, nativeQuery = true)
    List<Worker> findAllByPlaceId(long placeId);
}
