package cn.goktech.reggie.service.impl;

import cn.goktech.reggie.common.CustomException;
import cn.goktech.reggie.common.R;
import cn.goktech.reggie.entity.*;
import cn.goktech.reggie.mapper.OrdersMapper;
import cn.goktech.reggie.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    private final Long userId = 114514L;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Override
    @Transactional
    public R<String> submit(Orders orders) {
        // 获取用户id

        // 查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);

        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);

        if (shoppingCartList == null || shoppingCartList.size() == 0){
            throw new CustomException("购物车为空，不能下单");
        }

        // 查询用户数据
        User user = userService.getById(userId);
        // 查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null){
            throw new CustomException("地址信息有误");
        }
        // 向订单表插入数据

        long id = IdWorker.getId(); // 订单号

        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> orderDetails = shoppingCartList.stream().map((item) ->{
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(id);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet((item.getAmount().multiply(new BigDecimal(item.getNumber()))).intValue());
            return orderDetail;
        }).collect(Collectors.toList());


        orders.setId(id);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(id));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(user.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "":addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail())
        );

        this.save(orders);

        // 向订单明细表插入数据
        orderDetailService.saveBatch(orderDetails);
        // 清空购物车
        shoppingCartService.remove(queryWrapper);
        return null;
    }
}
