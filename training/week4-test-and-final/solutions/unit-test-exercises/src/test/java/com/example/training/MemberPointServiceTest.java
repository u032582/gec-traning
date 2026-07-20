package com.example.training;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberPointServiceTest {

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberPointService service;

    @Test
    void 加算成功_既存100ptに50pt加算で150を返しupdateが呼ばれる() {
    // 1. 仕込み（モックの振る舞いを決める）
    when(memberMapper.findById(1)).thenReturn(new Member(1, "佐藤", 100));

    // 2. 実行
    int result = service.addPoint(1, 50);

    // 3. 確認（戻り値 + 過程）
    assertEquals(150, result);
    verify(memberMapper).updatePoint(1, 150);
}
@Test
void 会員なし_findByIdがnullなら例外_updateは呼ばれない() {
    when(memberMapper.findById(99)).thenReturn(null);

    assertThrows(IllegalArgumentException.class, () -> service.addPoint(99, 50));

    verify(memberMapper, never()).updatePoint(anyInt(), anyInt());
}
@Test
void 加算0以下_例外_DBアクセスは一切しない() {
    assertThrows(IllegalArgumentException.class, () -> service.addPoint(1, 0));

    verify(memberMapper, never()).findById(anyInt());
    verify(memberMapper, never()).updatePoint(anyInt(), anyInt());
}
}
