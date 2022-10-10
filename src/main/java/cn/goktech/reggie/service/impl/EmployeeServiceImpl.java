package cn.goktech.reggie.service.impl;

import cn.goktech.reggie.entity.Employee;
import cn.goktech.reggie.mapper.EmployeeMapper;
import cn.goktech.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {


}
