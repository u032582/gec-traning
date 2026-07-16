package com.example.training.task;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.training.common.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;


    @Test
void findById_存在するときタスクを返す() {
    Task task = new Task();
    task.setId(1L);
    task.setTitle("テスト");
    task.setStatus("todo");

    when(taskMapper.findById(1L)).thenReturn(task);

    Task result = taskService.findById(1L);

    assertEquals(1L, result.getId());
    assertEquals("テスト", result.getTitle());
}
@Test
void findById_存在しないとき例外() {
    when(taskMapper.findById(999L)).thenReturn(null);

    assertThrows(ResourceNotFoundException.class,
            () -> taskService.findById(999L));
}
@Test
void create_insertが呼ばれ採番後のタスクを返す() {
    TaskRequest request = new TaskRequest("新規タスク", null, "todo", null);

    // insert が呼ばれたら、渡された Task に id=10 を書き込む（採番のまね）
    doAnswer(invocation -> {
        Task arg = invocation.getArgument(0);
        arg.setId(10L);
        return 1;
    }).when(taskMapper).insert(any(Task.class));

    // 取り直し用
    Task saved = new Task();
    saved.setId(10L);
    saved.setTitle("新規タスク");
    saved.setStatus("todo");
    when(taskMapper.findById(10L)).thenReturn(saved);

    Task result = taskService.create(request);

    assertEquals(10L, result.getId());
    assertEquals("新規タスク", result.getTitle());
    verify(taskMapper).insert(any(Task.class));
}
@Test
void update_存在しないとき例外_updateは呼ばれない() {
    when(taskMapper.findById(999L)).thenReturn(null);

    TaskRequest request = new TaskRequest("更新", null, "doing", null);

    assertThrows(ResourceNotFoundException.class,
            () -> taskService.update(999L, request));

    verify(taskMapper, never()).update(any(Task.class));
}
@Test
void delete_削除件数0なら例外() {
    when(taskMapper.delete(999L)).thenReturn(0);

    assertThrows(ResourceNotFoundException.class,
            () -> taskService.delete(999L));
}
}