package org.zerock.mallapi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.zerock.mallapi.dto.CartItemDTO;
import org.zerock.mallapi.dto.CartItemListDTO;
import org.zerock.mallapi.service.CartService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/cart")
public class CartController {

    //@PreAuthorize 나 security의 사용자 정보를 사용할 수 있습니다.

    private final CartService cartService;

    @PreAuthorize("#itemDTO.email == authentication.name")
    @PostMapping("/change")
    public List<CartItemListDTO> changeCart(@RequestBody @P("itemDTO") CartItemDTO itemDTO) {

        log.info(itemDTO);

        if(itemDTO.getQty() <= 0){
            return cartService.remove(itemDTO.getCino());
        }

        return cartService.addOrModify(itemDTO);

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/items")
    public List<CartItemListDTO> getCartItems(Principal principal) {

        String email = principal.getName();

        log.info("현재 사용자 정보 : " + email);

        return cartService.getCartItems(email);

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/{cino}")
    public List<CartItemListDTO> removeFromCart(@PathVariable("cino") Long cino) {
        log.info("cart item no : " + cino);

        return cartService.remove(cino); // 삭제 후 리스트 다시 불러오는 처리
    }

}
