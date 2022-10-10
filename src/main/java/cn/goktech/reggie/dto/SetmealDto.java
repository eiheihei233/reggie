package cn.goktech.reggie.dto;


import cn.goktech.reggie.entity.Setmeal;
import cn.goktech.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
