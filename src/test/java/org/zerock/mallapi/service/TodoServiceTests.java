package org.zerock.mallapi.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.TodoDTO;
import org.zerock.mallapi.repository.ProductRepository;
import org.zerock.mallapi.repository.search.TodoSearch;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class TodoServiceTests {

    @Autowired
    TodoService todoService;

    @Test
    public void testGet(){

        log.info(todoService.get(50L));
    }

    @Test
    public void testRegister(){

        TodoDTO todoDTO = TodoDTO.builder()
                .title("title...")
                .content("content...")
                .dueDate(LocalDate.of(2024,8,17))
                .build();

        log.info(todoService.register(todoDTO));
    }

    @Test
    public void testGetList(){

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        log.info(todoService.getList(pageRequestDTO));

    }



}
