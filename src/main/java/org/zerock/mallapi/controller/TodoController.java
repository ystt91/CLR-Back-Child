package org.zerock.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.TodoDTO;
import org.zerock.mallapi.service.TodoService;

import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/{tno}")
    public TodoDTO GET(@PathVariable("tno") Long tno) {

        return todoService.get(tno);

    }

    @GetMapping("/list")
    public PageResponseDTO<TodoDTO> list(PageRequestDTO pageRequestDTO) {

        log.info("list............" + pageRequestDTO);

        return todoService.getList(pageRequestDTO);

    }

    @PostMapping("/")
    public Map<String, Long> register(@RequestBody TodoDTO todoDTO ){

        // 왜 Map을 했을까?
        // JSON 반환을 위해

        log.info("todoDTO : " + todoDTO);

        Long tno = todoService.register(todoDTO);

        return Map.of("TNO", tno);

    }

    @PutMapping("/{tno}")
    public Map<String, String> modify(@PathVariable("tno") Long tno,
                                    @RequestBody TodoDTO todoDTO ){

        todoDTO.setTno(tno);
        todoService.modify(todoDTO);

        return Map.of("RESULT", "SUCCESS");

    }

    @DeleteMapping("{tno}")
    public Map<String, String> remove(@PathVariable("tno") Long tno){

        todoService.remove(tno);

        return Map.of("RESULT", "SUCCESS");

    }
}
