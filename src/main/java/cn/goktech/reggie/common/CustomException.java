package cn.goktech.reggie.common;

/**
 * 自定义业务类异常
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
