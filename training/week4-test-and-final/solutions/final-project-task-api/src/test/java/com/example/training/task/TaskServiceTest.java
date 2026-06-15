package com.example.training.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * 総合課題の参考: TaskService の単体テスト（Mockito使用、DB不要）。
 *
 * <p>TaskMapper をモック化し、Service の業務ルール（存在チェック・例外）が
 * 期待どおりかを確認する。新人が書くべき「必須テスト観点」を満たした例。
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask(Long id, String title, String status) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setStatus(status);
        return task;
    }

    @Test
    @DisplayName("findAll: Mapperの結果をそのまま返す")
    void findAll_returnsList() {
        List<Task> tasks = List.of(sampleTask(1L, "A", "todo"), sampleTask(2L, "B", "done"));
        when(taskMapper.findAll()).thenReturn(tasks);

        List<Task> result = taskService.findAll();

        assertEquals(2, result.size());
        verify(taskMapper).findAll();
    }

    @Test
    @DisplayName("findById: 存在すればそのタスクを返す")
    void findById_found() {
        when(taskMapper.findById(1L)).thenReturn(sampleTask(1L, "A", "todo"));

        Task result = taskService.findById(1L);

        assertEquals("A", result.getTitle());
    }

    @Test
    @DisplayName("findById: 存在しなければ TaskNotFoundException")
    void findById_notFound() {
        when(taskMapper.findById(99L)).thenReturn(null);

        assertThrows(TaskNotFoundException.class, () -> taskService.findById(99L));
    }

    @Test
    @DisplayName("create: insertでid採番→そのidで取り直して返す")
    void create_success() {
        TaskRequest request = new TaskRequest();
        request.setTitle("新規タスク");
        request.setStatus("todo");

        // insert が呼ばれたら、渡された Task に id=10 を採番する動きを再現。
        doAnswer(invocation -> {
            Task arg = invocation.getArgument(0);
            arg.setId(10L);
            return 1;
        }).when(taskMapper).insert(any(Task.class));

        // create は最後に findById(10) で取り直すので、その戻り値を仕込む。
        when(taskMapper.findById(10L)).thenReturn(sampleTask(10L, "新規タスク", "todo"));

        Task result = taskService.create(request);

        assertEquals(10L, result.getId());
        assertEquals("新規タスク", result.getTitle());
        verify(taskMapper).insert(any(Task.class));
    }

    @Test
    @DisplayName("update: 存在すれば更新し、取り直して返す")
    void update_success() {
        // 1回目の存在チェックと、更新後の取り直しの2回 findById が呼ばれる。
        when(taskMapper.findById(1L))
                .thenReturn(sampleTask(1L, "旧", "todo"))   // 存在チェック
                .thenReturn(sampleTask(1L, "新", "doing")); // 取り直し

        TaskRequest request = new TaskRequest();
        request.setTitle("新");
        request.setStatus("doing");

        Task result = taskService.update(1L, request);

        assertEquals("新", result.getTitle());
        assertEquals("doing", result.getStatus());
        verify(taskMapper).update(any(Task.class));
    }

    @Test
    @DisplayName("update: 存在しなければ例外。updateは呼ばれない")
    void update_notFound() {
        when(taskMapper.findById(99L)).thenReturn(null);

        TaskRequest request = new TaskRequest();
        request.setTitle("x");
        request.setStatus("todo");

        assertThrows(TaskNotFoundException.class, () -> taskService.update(99L, request));
        verify(taskMapper, never()).update(any(Task.class));
    }

    @Test
    @DisplayName("delete: 削除件数1なら正常終了")
    void delete_success() {
        when(taskMapper.delete(1L)).thenReturn(1);

        taskService.delete(1L);

        verify(taskMapper).delete(eq(1L));
    }

    @Test
    @DisplayName("delete: 削除件数0なら TaskNotFoundException")
    void delete_notFound() {
        when(taskMapper.delete(99L)).thenReturn(0);

        assertThrows(TaskNotFoundException.class, () -> taskService.delete(99L));
    }
}
