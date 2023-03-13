package com.project.zaiika.repositories.worker;

import com.project.zaiika.models.worker.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE workers
            SET place_role_id = :#{#worker.placeRoleId}
            WHERE worker_id = :#{#worker.id}
            """, nativeQuery = true)
    void updateWorker(Worker worker);

    List<Worker> findAllByPlaceId(long placeId);

    Worker findWorkerByPlaceIdAndId(long placeId, long workerId);

    @Modifying
    @Transactional
    void deleteWorkerByPlaceIdAndId(long placeId, long workerId);

    Worker findById(long id);

    List<Worker> findAllByPlaceRoleId(long roleId);
}
