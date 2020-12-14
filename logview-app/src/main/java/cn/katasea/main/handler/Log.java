package cn.katasea.main.handler;

import cn.katasea.main.service.FileService;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author katasea
 * 2020/12/10 14:24
 */
@Controller
public class Log {
	@Resource
	FileService fileService;
	@RequestMapping(value = {"/",""})
	public String index(HttpServletRequest request) {
		return "index";
	}
}
