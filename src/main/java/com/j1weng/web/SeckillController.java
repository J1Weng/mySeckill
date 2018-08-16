package com.j1weng.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.j1weng.dto.Exposer;
import com.j1weng.dto.SeckillExecution;
import com.j1weng.dto.SeckillResult;
import com.j1weng.entity.Seckill;
import com.j1weng.enums.SeckillStateEnum;
import com.j1weng.exception.RepeatKillException;
import com.j1weng.exception.SeckillCloseException;
import com.j1weng.exception.SeckillException;
import com.j1weng.service.SeckillService;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
	@Autowired
	private SeckillService seckillService;
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(Model model) {
		List<Seckill> list=seckillService.getSeckillList();
		model.addAttribute("list",list);
		return "list";
	}
	@RequestMapping(value="/{seckillId}/detail",method=RequestMethod.GET)
	public String detail(@PathVariable("seckillId") Long seckillId,Model model) {
		if(seckillId==null) {
			return "redirect:/seckill/list";
		}
		Seckill seckill=seckillService.getById(seckillId);
		if(seckill==null) {
			return "forward:/seckill/list";
		}
		model.addAttribute("seckill",seckill);
		return "detail";
	}
	@RequestMapping(value="/{seckillId}/exposer",
				method=RequestMethod.GET,
				produces= {"application/json;charset=UTF-8"})
	@ResponseBody
	public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId){
		SeckillResult<Exposer> result;
		
		try {
			Exposer exposer=seckillService.expostSeckillUrl(seckillId);
			result=new SeckillResult<Exposer>(true,exposer);
		}catch(Exception e) {
			e.printStackTrace();
			result=new SeckillResult<Exposer>(false,e.getMessage());
		}
		return result;
	}
    @RequestMapping(value = "/{seckillId}/{md5}/execution",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
	public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,
												@PathVariable("md5") String md5,
												@CookieValue(value="userPhone",required=false) Long userPhone){
		if(userPhone==null) {
			return new SeckillResult<SeckillExecution>(false,"未注册");
		}
		SeckillResult<SeckillExecution> result;
		try {
			
			
			//不以存储过程的方式来秒杀
			//SeckillExecution execution=seckillService.executeSeckill(seckillId, userPhone, md5);
			
			//以存储过程的方式来执行秒杀
			SeckillExecution execution=seckillService.executeSeckillProcedure(seckillId, userPhone, md5);
						return new SeckillResult<SeckillExecution>(true,execution);
			}catch(RepeatKillException e1) {
				SeckillExecution execution=new SeckillExecution(seckillId,SeckillStateEnum.REPEAT_KILL);
				return new SeckillResult<SeckillExecution>(true,execution);
			}catch(SeckillCloseException e2){
				SeckillExecution execution=new SeckillExecution(seckillId,SeckillStateEnum.END);
				return new SeckillResult<SeckillExecution>(true,execution);
			}catch(Exception e) {
				SeckillExecution execution=new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
	            return new SeckillResult<SeckillExecution>(true,execution);
			}
	}
  //获取系统时间
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time()
    {
        Date now=new Date();
        return new SeckillResult<Long>(true,now.getTime());
        
    }
}
