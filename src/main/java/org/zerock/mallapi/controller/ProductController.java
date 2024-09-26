package org.zerock.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.mallapi.dto.PageRequestDTO;
import org.zerock.mallapi.dto.PageResponseDTO;
import org.zerock.mallapi.dto.ProductDTO;
import org.zerock.mallapi.service.ProductService;
import org.zerock.mallapi.util.CustomFileUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final CustomFileUtil fileUtil;
    private final ProductService productService;


    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable("fileName") String fileName){

        return fileUtil.getFile(fileName);

    }

    @PreAuthorize(("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')"))
    @GetMapping("/list")
    public PageResponseDTO<ProductDTO> productList(PageRequestDTO pageRequestDTO){

        return productService.getList(pageRequestDTO);

    }

    @PostMapping("/")
    public Map<String, Long> register(ProductDTO productDTO){

        log.info("컨트롤러" + productDTO);
        //이 과정에서 파일을 업로드를 먼저 시켜야 합니다.

        //멀티 파트로 파일을 리스트로 얻어오고
        List<MultipartFile> files = productDTO.getFiles();
        //fileUtil로 파일을 upload path에 저장하면서 db에 String으로 파일을 저장해줍니다.
        List<String> uploadedFileNames = fileUtil.saveFiles(files);

        productDTO.setUploadedFileNames(uploadedFileNames);

        log.info(uploadedFileNames);

        Long pno = productService.register(productDTO);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return Map.of("RESULT", pno);
    }

    @GetMapping("/{pno}")
    public ProductDTO get(@PathVariable("pno") Long pno){

        return productService.get(pno);

    }

    @PutMapping("/{pno}")
    public Map<String, String> modify(@PathVariable("pno") Long pno, ProductDTO productDTO){

        productDTO.setPno(pno);

        // old product =  DB 저장 상품
        ProductDTO oldProduct = productService.get(pno);

        // file upload / 새로운 파일들
        List<MultipartFile> files = productDTO.getFiles();
        List<String> newUploadFileNames = fileUtil.saveFiles(files);

        // keep files String / db에 있는 이미지 파일들
        List<String> uploadedFileNames = productDTO.getUploadedFileNames();

        if(newUploadFileNames != null && !newUploadFileNames.isEmpty()){
            uploadedFileNames.addAll(newUploadFileNames);
        }

        productService.modify(productDTO);

        // 지워져야 하는 파일들
        List<String> oldFileNames = oldProduct.getUploadedFileNames();

        if(oldFileNames != null && !oldFileNames.isEmpty()){
            List<String> removeFiles =
                    oldFileNames
                    .stream()
                    .filter(fileName -> uploadedFileNames.indexOf(fileName) == -1)
                    .collect(Collectors.toList());

            fileUtil.deleteFiles(removeFiles);
        }// end if

        return Map.of("RESULT", "SUCCESS");

    }

    @DeleteMapping("/{pno}")
    public  Map<String, String> remove(@PathVariable("pno") Long pno){

        List<String> oldFileNames = productService.get(pno).getUploadedFileNames();

        productService.remove(pno);

        fileUtil.deleteFiles(oldFileNames);

        return Map.of("RESULT", "DELETE SUCCESS");
    }


}
