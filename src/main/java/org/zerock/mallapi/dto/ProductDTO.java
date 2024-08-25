package org.zerock.mallapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Long pno;

    private String pname;

    private int price;

    private String pdsec;

    private boolean delFlag;

    //초기화 시키는 이유 : null 체크

    // 브라우저에서 보내는 값
    @Builder.Default
    private List<MultipartFile> files = new ArrayList<>();

    // DB에 있는 값
    @Builder.Default
    private List<String> uploadedFileNames = new ArrayList<>();

}
