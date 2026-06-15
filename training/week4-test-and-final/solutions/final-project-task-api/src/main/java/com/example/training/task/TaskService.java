package com.example.training.task;

import java.util.List;
import org.springframework.stereotype.Service;

/**
 * タスクに関する判断・処理を担当する Service。
 *
 * <p>3層構造の真ん中。Controller（窓口）から呼ばれ、必要に応じて
 * {@link TaskMapper}（DB出し入れ）を使う。
 *
 * <p>「存在しないIDなら例外を投げる」といった業務ルールはここに集約する。
 */
@Service
public class TaskService {

    private final TaskMapper taskMapper;

    // コンストラクタインジェクション（テスト時にモックを差し込みやすい形）。
    public TaskService(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    /** 全件取得。 */
    public List<Task> findAll() {
        return taskMapper.findAll();
    }

    /**
     * 1件取得。見つからなければ {@link TaskNotFoundException}（→404）。
     */
    public Task findById(Long id) {
        Task task = taskMapper.findById(id);
        if (task == null) {
            throw new TaskNotFoundException(id);
        }
        return task;
    }

    /**
     * 新規作成。リクエストの内容を Task に詰めて登録し、採番済みの Task を返す。
     */
    public Task create(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());

        taskMapper.insert(task);   // insert後、task.id に採番された値が入る
        // 作成直後の最新状態（created_at等を含む）を返すため取り直す。
        return findById(task.getId());
    }

    /**
     * 更新。対象が無ければ {@link TaskNotFoundException}（→404）。
     */
    public Task update(Long id, TaskRequest request) {
        // 先に存在チェック（無ければここで404）。
        findById(id);

        Task task = new Task();
        task.setId(id);
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());

        taskMapper.update(task);
        return findById(id);
    }

    /**
     * 削除。対象が無ければ {@link TaskNotFoundException}（→404）。
     */
    public void delete(Long id) {
        int deleted = taskMapper.delete(id);
        if (deleted == 0) {
            throw new TaskNotFoundException(id);
        }
    }
}
