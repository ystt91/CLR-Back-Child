package org.zerock.mallapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.domain.ProductImage;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;
import org.zerock.mallapi.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage()-1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );

        Page<Object[]> result = productRepository.selectList(pageable);

        //Object[] = 0 product 1 productImage
        //Object[] = 0 product 1 productImage
        //Object[] = 0 product 1 productImage
        //그럴려면 반복문 처리를 해줘야 한다.

        List<ProductDTO> dtoList = result.get().map(arr->{
            ProductDTO productDTO = null;

            Product product = (Product) arr[0];
            ProductImage productImage = (ProductImage) arr[1];

            productDTO = ProductDTO.builder()
                    .pno(product.getPno())
                    .pname(product.getPname())
                    .price(product.getPrice())
                    .pdsec(product.getPdesc())
                    .build();

            String imageStr = productImage.getFileName();
            productDTO.setUploadedFileNames(List.of(imageStr));

            return productDTO;

        }).collect(Collectors.toList());


        long totalCount = result.getTotalElements();


        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(dtoList)
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();

    }


    @Override
    public Long register(ProductDTO productDTO) {

        log.info("시발2 " + productDTO.toString());

        Product product = dtoToEntity(productDTO);

        log.info("----------------------------------");
        log.info("시발" + product);
        log.info(product.getImageList());

        Long pno = productRepository.save(product).getPno();

        return pno;
    }

    @Override
    public ProductDTO get(Long pno) {

        Optional<Product> result = productRepository.findById(pno);

        log.info(result.toString());

        Product product = result.orElseThrow();

        ProductDTO productDTO = entityToDTO(product);

        return productDTO;
    }

    @Override
    public void modify(ProductDTO productDTO) {

        // 조회
        Optional<Product> result = productRepository.findById(productDTO.getPno());

        Product product = result.orElseThrow();
        // 업데이트
        product.changePrice(productDTO.getPrice());
        product.changeName(product.getPname());
        product.changeDesc(product.getPdesc());
        product.changeDel(productDTO.isDelFlag());

        //**** 제일 중요한 이미지 처리 ****
        // ArrayList를 바꾸면 안된다고 했습니다.

        List<String> uploadFileNames = productDTO.getUploadedFileNames();

        product.clearList();

        if(uploadFileNames != null && uploadFileNames.isEmpty()){
            uploadFileNames.forEach(uploadName->{
                product.addImageString(uploadName);
            });
        };

        // 저장
        productRepository.save(product);
    }

    @Override
    public void remove(Long pno){

        // 원래대로라면 삭제 기능은 없다.
        // delete Flag 값을 변경 하는 것밖에 없다.
        // 고객 정보는 소중하니까

        productRepository.deleteById(pno);
    }

    private ProductDTO entityToDTO(Product product) {

        log.info("productDTO-----------------------" + product.toString());

        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .price(product.getPrice())
                .pdsec(product.getPdesc())
                .delFlag(product.isDelFlag())
                .build();

        List<ProductImage> imageList = product.getImageList();

        if(imageList == null || imageList.isEmpty()) {
           return productDTO;
        }

        List<String> fileNameList = imageList.stream().map
                (productImage ->
                        productImage.getFileName()).collect(Collectors.toList());

        productDTO.setUploadedFileNames(fileNameList);

        log.info("productDTO-----------------------" + productDTO.toString());

        return productDTO;
    }

    private Product dtoToEntity(ProductDTO productDTO) {

        Product product = Product.builder()
                .pno(productDTO.getPno())
                .pname(productDTO.getPname())
                .price(productDTO.getPrice())
                .pdesc(productDTO.getPdsec())
                .delFlag(productDTO.isDelFlag())
                .build();

        List<String> uploadedFileNames = productDTO.getUploadedFileNames();

        if(uploadedFileNames != null && uploadedFileNames.size() == 0) {
            return product;
        }

        uploadedFileNames.forEach(fileName->{
           product.addImageString(fileName);
        });

        return product;

    }
}
