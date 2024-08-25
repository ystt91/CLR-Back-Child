package org.zerock.mallapi.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.dto.PageRequestDTO;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testInsert(){

        for (int i = 0; i < 10; i++) {

            Product product = Product.builder()
                    .pname("Test" + i)
                    .pdesc("Test Desc" + i)
                    .price(10000)
                    .build();

            product.addImageString(UUID.randomUUID()+ "_" + "IMAGE1.jpg");
            product.addImageString(UUID.randomUUID()+ "_" + "IMAGE2.jpg");

            productRepository.save(product);

        }

    }

    //@Transactional 이게 없으면 에러가 납니다. 왜냐하면 데이터 베이스에 두번 접속해야 되니까
    //               쿼리가 ** 두번 ** 날아갑니다.
    @Test
    public void testRead1(){
        Long pno = 1L;

        Optional<Product> result =
                productRepository.findById(pno);

        Product product = result.orElseThrow();

        log.info(product);

        log.info(product.getImageList());
    }

    //testRead1과 다르게 쿼리가 ** 한번만 ** 날아갑니다.
    @Test
    public void testRead2(){
        Long pno = 1L;

        Optional<Product> result =
                productRepository.selectOne(pno);

        Product product = result.orElseThrow();

        log.info(product);

        log.info(product.getImageList());
    }

    @Commit
    @Transactional
    @Test
    public void testDelete(){
        Long pno = 2L;
        productRepository.updateToDelete(true, 2L);
    }

    @Test
    public void testUpdate(){

        Product product = productRepository.selectOne(1L).get();

        product.changePrice(3000);

        product.clearList();

        //clearList() 안 쓰고, 그냥 다른 ArrayList로 바꿔 쓰면 되는거 아닌가?

        // JPA에서 컬렉션을 쓸 때는 JPA가 관리하는 COLLECTION으로 쓰는게 맞다.
        // JPA CONTEXT?라고 해야 하나 거기서 관리하는 객체로만 써야 하기 때문이다.

        // 한번에 같이 변경되는 데이터를 쓸 때는 @ElementCollection을 쓰는게 좋지 않을까 한다.
        product.addImageString(UUID.randomUUID()+ "_" + "PIMAGE1.jpg");
        product.addImageString(UUID.randomUUID()+ "_" + "PIMAGE2.jpg");
        product.addImageString(UUID.randomUUID()+ "_" + "PIMAGE3.jpg");

        productRepository.save(product);

    }

    @Test
    public void testList(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("pno").descending());
        Page<Object[]> result = productRepository.selectList(pageable);

        result.getContent().forEach(arr -> log.info(Arrays.toString(arr)));
    }

    @Test
    public void testSearch(){

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().build();

        productRepository.searchList(pageRequestDTO);

    }
}
