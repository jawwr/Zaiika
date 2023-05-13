package com.zaiika.workerservice.repository;

import com.zaiika.workerservice.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    @Query(value = """
            select *
            from workers
            where place_id = :#{#placeId}
            """, nativeQuery = true)
    List<Worker> findAllByPlaceId(long placeId);

    @Modifying
    @Transactional
    void deleteWorkerById(long workerId);

    Worker findById(long id);

    @Query(value = """
            select *
            from workers
            where user_id = :#{#userId}
            """, nativeQuery = true)
    Worker getWorkerByUserId(long userId);

    List<Worker> findAllByPlaceRoleId(long roleId);

    Worker findWorkerByUserId(long userId);

    @Query(value = """
            select count(*) <> 0
            from workers
            where user_id = :#{#userId}
            """, nativeQuery = true)
    boolean isWorkerExist(long userId);
}
