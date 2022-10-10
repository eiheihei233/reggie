package cn.goktech.reggie.service.impl;

import cn.goktech.reggie.entity.OrderDetail;
import cn.goktech.reggie.mapper.OrderDetailMapper;
import cn.goktech.reggie.service.OrderDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
