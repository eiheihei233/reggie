package cn.goktech.reggie.service;

import cn.goktech.reggie.common.R;
import cn.goktech.reggie.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;


public interface OrdersService extends IService<Orders> {

    public R<String> submit(Orders orders);
}
