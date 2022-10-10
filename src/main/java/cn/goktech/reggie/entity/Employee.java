package cn.goktech.reggie.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    @TableField(fill = FieldFill.INSERT) // 在插入时更新字段数值
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) // 在插入和更新的时候填充字段
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT) // 在插入时更新字段数值
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE) // 在插入和更新的时候填充字段
    private Long updateUser;

}
