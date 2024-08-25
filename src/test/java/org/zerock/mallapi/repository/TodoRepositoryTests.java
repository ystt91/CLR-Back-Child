package org.zerock.mallapi.repository;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.mallapi.domain.Todo;

import java.time.LocalDate;
import java.util.Optional;


@SpringBootTest
@Log4j2
public class TodoRepositoryTests {

    @Autowired
    private TodoRepository repository;
    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void test1(){

        Assertions.assertNotNull(todoRepository);

        log.info(todoRepository.getClass().getName());
    }

    @Test
    public void testInsert(){

        for (int i = 0; i < 100; i++) {

            Todo todo = Todo.builder()
                    .title("Title..." + i)
                    .content("Content..." + i)
                    .dueDate(LocalDate.of(2023,12,10))
                    .build();

            Todo result = todoRepository.save(todo); //리턴 타입은 entity

            log.info(result);

        }

    }

    @Test
    public void testRead(){
        Long tno = 1L;

        Optional<Todo> result = todoRepository.findById(tno);

        Todo todo = result.orElseThrow();

        log.info(todo);
    }

    @Test
    public void testUpdate(){
        //먼저 로딩 하고 ENTITY 객체를 변경 /SETTER

        Long tno = 1L;

        Optional<Todo> result = todoRepository.findById(tno);

        Todo todo = result.orElseThrow();

        todo.changeTitle("Updated Title...");
        todo.changeContent("Updated Content...");
        todo.changeComplete(true);

        todoRepository.save(todo);


    }

    @Test
    public void testPaging(){

        //페이지 번호는 0부터
        Pageable pageable = PageRequest.of(0,10, Sort.by("tno").descending());

        Page<Todo> result = todoRepository.findAll(pageable);

        log.info(result.getTotalElements());

        log.info(result.getContent());

    }
/*
    @Test
    public void testSearch1(){

        todoRepository.search1();

    }*/


}
