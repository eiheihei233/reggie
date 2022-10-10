package cn.goktech.reggie.controller;

import cn.goktech.reggie.common.R;
import cn.goktech.reggie.entity.ShoppingCart;
import cn.goktech.reggie.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
     public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

        // 设置用户id

        Long userId = 114514L;

        shoppingCart.setUserId(userId);


        Long dishId = shoppingCart.getDishId();
        // 查询当前菜品或套餐是否在购物车中
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId,userId);

        if (dishId != null){
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else{
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        if (cartServiceOne != null){
            // 不为空 有数据 做加操作
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        }else {
            // 为空，没有数据，新增操作
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }


        return R.success(cartServiceOne);
     }
    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){

        // 设置用户id

        Long userId = 114514L;

        shoppingCart.setUserId(userId);

        shoppingCart.setCreateTime(LocalDateTime.now());

        Long dishId = shoppingCart.getDishId();
        // 查询当前菜品或套餐是否在购物车中
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId,userId);

        if (dishId != null){
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else{
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }

        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        Integer number = cartServiceOne.getNumber();

        cartServiceOne.setNumber(number -1);

        shoppingCartService.updateById(cartServiceOne);

        if(cartServiceOne.getNumber() == 0){
            shoppingCartService.removeById(cartServiceOne.getId());
        }

        return R.success(cartServiceOne);
    }
     @GetMapping("/list")
     public R<List<ShoppingCart>> list(){
        Long userId = 114514L;

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(ShoppingCart::getUserId,userId);

        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

         List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

         return R.success(list);
     }
}
