package org.zerock.mallapi.service;

import jakarta.transaction.Transactional;
import org.zerock.mallapi.domain.Todo;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.TodoDTO;

/*
    * 프록시 객체가 생성되었다는 것은,
    * @Transactional 어노테이션이 붙은 메서드를 호출할 때,
    * 그 메서드가 단순히 호출되는 것이 아니라, 트랜잭션 관리 등의
    * 추가적인 작업이 수행되는 것을 의미합니다.
*/

@Transactional // 트랜잭션 처리
public interface TodoService {

    TodoDTO get(Long tno);

    Long register(TodoDTO dto);

    void modify(TodoDTO dto);

    void remove(Long tno);

    PageResponseDTO<TodoDTO> getList(PageRequestDTO pageRequestDTO);



    default TodoDTO entityToDTO(Todo todo){

        TodoDTO todoDTO =
                TodoDTO.builder()
                        .tno(todo.getTno())
                        .title(todo.getTitle())
                        .content(todo.getContent())
                        .complete(todo.isComplete())
                        .dueDate(todo.getDueDate())
                        .build();

        return todoDTO;
    }



    default Todo dtoToEntity(TodoDTO todoDTO){

        Todo todo =
                Todo.builder()
                        .tno(todoDTO.getTno())
                        .title(todoDTO.getTitle())
                        .content(todoDTO.getContent())
                        .complete(todoDTO.isComplete())
                        .dueDate(todoDTO.getDueDate())
                        .build();

        return todo;
    }

}
