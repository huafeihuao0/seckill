package org.seckill.web;

import org.seckill.dto.Exposure;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Repo;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillRepeatException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/seckill")
public class SeckillController
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    /***
     *  秒杀商品列表
     *      【/seckill/list】
     * */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model)
    {
        List<Repo> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        return "list";
    }

    /***
     *  商品详情
     *      【/seckill/{productId}/detail】
     * */
    @RequestMapping(value = "/{productId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("productId") Long productId,
                         Model model)
    {
        if (productId == null)
        {
            return "redirect:/seckill/list";
        }
        Repo repo = seckillService.getById(productId);
        if (repo == null)
        {
            return "redirect:/seckill/list";
        }
        // 用于传向前端页面的变量seckill
        model.addAttribute("seckill", repo);
        return "detail";
    }

    /**
     * 商品展出
     * 【/seckill/{productId}/exposure】
     */
    @RequestMapping(value = "/{productId}/exposure",
            method = RequestMethod.POST,
            produces = {"application/json;charset=utf-8"})//响应头的内容类型
    @ResponseBody   //返回值是json
    public SeckillResult<Exposure> exposure(@PathVariable("productId") Long productId)
    {

        try
        {
            Exposure exposure = seckillService.exportSeckillUrl(productId);
            return new SeckillResult<Exposure>(true, exposure);
        } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return new SeckillResult<Exposure>(false, e.getMessage());
        }

    }

    /***
     *  秒杀
     * 【/seckill/{productId}/{md5}/execution】
     * */
    @RequestMapping(value = "/{productId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("productId") Long productId,
                                                   @CookieValue(value = "killPhone", required = false) Long userPhone,
                                                   @PathVariable("md5") String md5)
    {
        if (userPhone == null)
        {
            return new SeckillResult<SeckillExecution>(false, "未注册");
        }
        try
        {
            SeckillExecution seckillExecution = seckillService.executeSeckill(productId, userPhone, md5);
            return new SeckillResult<SeckillExecution>(true, seckillExecution);

        } catch (SeckillRepeatException e)
        {
            SeckillExecution seckillExecution = new SeckillExecution(productId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(false, seckillExecution);

        } catch (SeckillCloseException e)
        {
            SeckillExecution seckillExecution = new SeckillExecution(productId, SeckillStateEnum.END);
            return new SeckillResult<SeckillExecution>(false, seckillExecution);

        } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            SeckillExecution seckillExecution = new SeckillExecution(productId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(false, seckillExecution);

        }

    }

    /***
     *  时间
     *      【/seckill/time/now】
     * */
    @RequestMapping(value = "/time/now", method = RequestMethod.GET,
            produces = {"application/json;charset=utf-8"})
    @ResponseBody
    public SeckillResult<Long> time()
    {
        Long now = System.currentTimeMillis();
        return new SeckillResult<Long>(true, now);
    }
}
