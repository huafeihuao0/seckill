package org.seckill.exception;

/***
 *  【秒杀重复异常】
 * */
public class SeckillRepeatException
        extends SeckillException
{
    public SeckillRepeatException(String message)
    {
        super(message);
    }

    public SeckillRepeatException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
