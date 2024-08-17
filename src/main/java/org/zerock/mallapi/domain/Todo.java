package org.zerock.mallapi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Table(name="tbl_todo")
public class Todo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tno;

    @Column(length = 500, nullable = false)
    private String title;
    private String content;
    private boolean complete;
    private LocalDate dueDate;

    /* 테이블 설계를 먼저 하는 경우 */
    /* jpa는 객체 지향 설계 프레임워크 인데 테이블 설계를 먼저 해야 하나?*/
    /* 테이블 설계 자동 */



    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeContent(String content) {
        this.content = content;
    }

    public void changeComplete(boolean complete) {
        this.complete = complete;
    }

    public void changeDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
