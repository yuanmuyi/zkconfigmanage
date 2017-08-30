package com.yy.zk.controller;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.qos.logback.classic.Logger;

/**     
 * @Description:  
 * @author yang.yuan    
 * @date 2017年8月30日 下午2:27:02  
 * @reviewer  
 */
@Controller
public class MainCntroller {
	
	@RequestMapping("logtest")
	@ResponseBody
    public String logbackLevel() throws Exception {
        Logger logger = (Logger) LoggerFactory.getLogger("root");
        String levelStr = logger.getLevel().levelStr;
        return levelStr;
    }
}
