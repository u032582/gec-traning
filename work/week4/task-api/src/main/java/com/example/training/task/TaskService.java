package com.example.training.task;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.training.common.ResourceNotFoundException;

@Service
public class TaskService {

    private final TaskMapper taskMapper;

    public TaskService(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    public List<Task> findAll() {
        return taskMapper.findAll();
    }

public Task findById(Long id) {
    Task task = taskMapper.findById(id);
    if (task == null) {
        throw new ResourceNotFoundException("タスクが見つかりません: id=" + id);
    }
    return task;
    }

    public Task create(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setDueDate(request.dueDate());
    
        taskMapper.insert(task);
        // 採番された id で取り直し（createdAt / updatedAt も取得）
        return findById(task.getId());
    }

    public Task update(Long id, TaskRequest request) {
        findById(id); // 無ければここで404
    
        Task task = new Task();
        task.setId(id);
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setDueDate(request.dueDate());
    
        taskMapper.update(task);
        return findById(id);
    }

    public void delete(Long id) {
        int deleted = taskMapper.delete(id);
        if (deleted == 0) {
            throw new ResourceNotFoundException("タスクが見つかりません: id=" + id);
        }
    }
}