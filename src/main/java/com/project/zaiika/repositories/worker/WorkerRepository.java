package com.project.zaiika.repositories.worker;

import com.project.zaiika.models.worker.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    List<Worker> findAllByPlaceId(long placeId);

    @Modifying
    @Transactional
    void deleteWorkerById(long workerId);

    Worker findById(long id);

    List<Worker> findAllByPlaceRoleId(long roleId);
}
