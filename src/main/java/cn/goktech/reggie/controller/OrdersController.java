package cn.goktech.reggie.controller;

import cn.goktech.reggie.common.R;
import cn.goktech.reggie.entity.Orders;
import cn.goktech.reggie.service.OrdersService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    private Long userId = 114514L;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return R.success("下单成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String number){

        Page pageInfo = new Page(page,pageSize);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(number != null,Orders::getNumber,number);

        ordersService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }
}
