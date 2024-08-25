package org.zerock.mallapi.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductServiceTests {

    @Autowired
    private ProductService productService;

    @Test
    public void testList(){

        //목록 불러오기
        //리스트 불러오는데 이미지까지 가지고 오느냐가 중요한 것이다.
        //쿼리도 잘 날아가는지 그것도 좀 궁금하고

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        PageResponseDTO<ProductDTO> responseDTO = productService.getList(pageRequestDTO);

        log.info(responseDTO.getDtoList());

    }

    @Test
    public void testRegister(){
        ProductDTO productDTO = ProductDTO.builder()
                        .pname("새로운 상품")
                        .pdsec("신규 추가 상품")
                        .price(1000)
                        .build();

        productDTO.setUploadedFileNames(
                List.of(UUID.randomUUID()+ "_" + "Test1.jpg",
                        UUID.randomUUID()+ "_" + "Test2.jpg")
        );

        productService.register(productDTO);

    }

}
