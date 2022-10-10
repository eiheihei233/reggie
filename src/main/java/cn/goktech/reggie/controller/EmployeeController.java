package cn.goktech.reggie.controller;

import cn.goktech.reggie.common.R;
import cn.goktech.reggie.entity.Employee;
import cn.goktech.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 后台管理员的登录操作
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request,@RequestBody Employee employee){
        // 将页面提交的密码进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 根据页面提交的用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        if (emp == null){
            return R.error("登录失败,该用户不存在");
        }
        if (!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }
        if (emp.getStatus() == 0){
            return R.error("账号被禁用");
        }
        //登陆成功 将用户放入session
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        //设置初始密码，进行加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        // 调用方法，保存到数据库
        employeeService.save(employee);

        return R.success("新增员工成功");
    }

    /**
     * 查询所有员工并进行分页
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page = {},pageSize = {},name = {}" ,page,pageSize,name);

        // 基于mybatis-plus 分页
        Page pageInfo = new Page(page,pageSize);

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        // 添加过滤条件
        queryWrapper.like(Strings.isNotEmpty(name),Employee::getName,name);
        // 添加条件排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo,queryWrapper);


        return R.success(pageInfo);
    }
    @PutMapping
    public R<String> update(HttpServletRequest request , @RequestBody Employee employee){

        log.info(employee.toString());
        employeeService.updateById(employee);
        return R.success("更新成功");
    }


    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
    log.info("根据id查询员工信息" );
    Employee employee = employeeService.getById(id);
    if (employee != null){
        return R.success(employee);
    }
        return R.error("没有查询到员工");
    }
}
