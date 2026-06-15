package com.example.training.task;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * タスク管理APIの窓口（Controller）。
 *
 * <p>URLとHTTPメソッドを、Serviceの処理に振り分けるのが役目。
 * 業務ロジックはここに書かず {@link TaskService} に任せる（3層構造）。
 *
 * <p>ベースパスは {@code /api/tasks}。
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /** 一覧取得: GET /api/tasks → 200 OK + タスク配列 */
    @GetMapping
    public List<Task> list() {
        return taskService.findAll();
    }

    /** 単体取得: GET /api/tasks/{id} → 200 OK（無ければ404） */
    @GetMapping("/{id}")
    public Task get(@PathVariable Long id) {
        return taskService.findById(id);
    }

    /**
     * 作成: POST /api/tasks → 201 Created + 作成したタスク。
     * Location ヘッダに新しいタスクのURLを入れる（RESTの作法）。
     * {@code @Valid} で TaskRequest の入力チェックが走る（違反は400）。
     */
    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody TaskRequest request) {
        Task created = taskService.create(request);
        URI location = URI.create("/api/tasks/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    /** 更新: PUT /api/tasks/{id} → 200 OK + 更新後タスク（無ければ404、入力不正は400） */
    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return taskService.update(id, request);
    }

    /** 削除: DELETE /api/tasks/{id} → 204 No Content（無ければ404） */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
