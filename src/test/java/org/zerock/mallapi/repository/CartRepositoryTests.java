package org.zerock.mallapi.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.mallapi.domain.Cart;
import org.zerock.mallapi.domain.CartItem;
import org.zerock.mallapi.domain.Member;
import org.zerock.mallapi.domain.Product;
import org.zerock.mallapi.dto.CartItemListDTO;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
public class CartRepositoryTests {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    @Commit
    @Test
    public void testInsertByProduct(){

        String email = "user1@aaa.com";

        Long pno = 6L;

        int qty = 3;

        //이메일과 상품 번호로 장바구니 아이템 확인
        // 없으면 추가 / 있으면 수량 변경

         CartItem cartItem = cartItemRepository.getCartItemDTOByEmailAndPno(email, pno);

         // 사용자의 장바구니가 있으면
            // 담겨있는 상품이므로 수량만 변경
                // qty를 2로 바꾸면 update sql이 실행되고 아래 코드가 적용될거임
         if( cartItem != null ){
             cartItem.changeQty(qty);
             cartItemRepository.save(cartItem);
             return;
         }

         // 사용자의 장바구니 자체가 없으면
            // 사용자의 장바구니에 장바구니 아이템 만들어서 저장

        Optional<Cart> result = cartRepository.getCartOfMember(email);

         Cart cart = null;

         if(result.isEmpty()){

             Member member = Member.builder().email(email).build();
             Cart tempCart = Cart.builder().owner(member).build();

             cart = cartRepository.save(tempCart);

         }else { // 장바구니는 있으나 해당 상품의 장바구니 아이템은 없는 경우
            cart = result.get();
         }

         Product product = Product.builder().pno(pno).build();
         cartItem = CartItem.builder().cart(cart).product(product).qty(qty).build();

        cartItemRepository.save(cartItem);

    }

    @Test
    public void testListOfMember(){

        String email = "user1@aaa.com";

        List<CartItemListDTO> cartItemListDTOList = cartItemRepository.getItemsOfCartDTOByEmail(email);

        for (CartItemListDTO dto : cartItemListDTOList) {
            log.info(dto);
            //역순으로 쿼리를 작성 했으므로 log가 찍힐 것임
        }

    }

    @Transactional
    @Commit
    @Test
    public void testUpdateByCino(){

        Long cino = 1L;
        int qty = 4;

        Optional<CartItem> result = cartItemRepository.findById(cino);

        CartItem cartItem = result.orElseThrow();

        cartItem.changeQty(qty);

        cartItemRepository.save(cartItem);
    }

    //삭제 한 후에
    @Test
    public void testDeleteThenList(){

        Long cino = 1L;

        Long cno = cartItemRepository.getCartFromItem(cino);

        cartItemRepository.deleteById(cino);

        List<CartItemListDTO> cartItemList = cartItemRepository.getItemOfCartDTOByCart(cno);

        for(CartItemListDTO dto : cartItemList){
            log.info(dto);
        }

    }

}
