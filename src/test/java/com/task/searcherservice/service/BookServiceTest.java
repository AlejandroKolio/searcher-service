package com.task.searcherservice.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.task.searcherservice.exception.FieldNotFoundException;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;

/**
 * @author Alexander Shakhov
 */
@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;
    @NonNull
    private String wrongFormatOrAbsentField;

    @BeforeEach
    public void setUp() {
        wrongFormatOrAbsentField = "{\n" + "  \"resultCount\": 5,\n" + "  \"empty\": [\n" + "    \n" + "  ]\n" + "}";
    }

    @Test
    @Description("Should throw FieldNotFoundException if field has been changed by 3d party")
    public void getTopAlbumsResourseNotFoundExceptionTest() {
        assertThatThrownBy(() -> bookService.getTopBooks(wrongFormatOrAbsentField, 10)).isInstanceOf(
                FieldNotFoundException.class).hasMessageContaining("Field not found.");
    }
}
