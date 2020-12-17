package cn.katasea.main.handler;

import cn.katasea.main.service.FileService;
import cn.katasea.main.service.util.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author katasea
 * 2020/12/10 14:24
 */
@Controller
public class Log {
	@Resource
	FileService fileService;
	@RequestMapping(value = {"/",""})
	public String indexForward(HttpServletRequest request) {
		return "redirect:/index";
	}
	@RequestMapping("/index")
	public String index(HttpServletRequest request) {
		return "index";
	}

	@RequestMapping("/view")
	public String view(HttpServletRequest request,@RequestParam String path) {
		return "detail";
	}

	@GetMapping(value = "/file/download")
	public void batchDownLoad(
			HttpServletResponse response,
			@RequestParam String path) {
//		if(arr.length > 1) {
//			FileUtil.downloadZip("download.zip",arr,response);
//		} else {
			String fileName = path.substring(path.lastIndexOf("/")+1);
			if(fileName.length() > 0) {
				FileUtil.downLoadFile(response,path,fileName);
			}
//		}

	}

}
