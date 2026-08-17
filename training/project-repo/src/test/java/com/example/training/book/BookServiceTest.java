package com.example.training.book;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * BookService の集計メソッドの単体テスト。
 * DBには触れず、Mapper をモックにして「数字がそのまま返るか」を確かめる。
 */
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void 分類別集計_Mapperの数字をそのまま返す() {
        CategoryStatsResponse tech = new CategoryStatsResponse("技術書", 3, 6, 4);
        when(bookMapper.countByCategory()).thenReturn(List.of(tech));

        List<CategoryStatsResponse> result = bookService.countByCategory();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCategory()).isEqualTo("技術書");
        assertThat(result.get(0).getTitleCount()).isEqualTo(3);
        assertThat(result.get(0).getTotalCount()).isEqualTo(6);
        assertThat(result.get(0).getAvailableCount()).isEqualTo(4);
        verify(bookMapper).countByCategory();
    }
}
