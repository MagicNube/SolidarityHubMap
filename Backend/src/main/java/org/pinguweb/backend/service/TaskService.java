package org.pinguweb.backend.service;

import lombok.Getter;
import org.pinguweb.backend.model.Task;
import org.pinguweb.backend.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Getter
    private final TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository) {this.taskRepository = taskRepository;}
    public List<Task> findAll(){return taskRepository.findAll();}
    public Optional<Task> findByID(Integer ID){return taskRepository.findById(ID);}
}
