package com.zaiika.workerservice.service.user;

import com.zaiika.workerservice.model.UserDto;
import com.zaiika.workerservice.model.WorkerCredentials;

public interface UserService {
    UserDto getUser();

    long saveWorker(WorkerCredentials worker);


}
