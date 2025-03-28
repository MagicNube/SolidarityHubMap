package org.pinguweb.backend.service;

import org.pinguweb.backend.model.Task;
import org.pinguweb.backend.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository) {this.taskRepository = taskRepository;}
    public Task saveTask(Task task) {return this.taskRepository.save(task);}
}
