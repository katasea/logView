package cn.katasea.main.handler;

import cn.katasea.bean.po.StateInfo;
import cn.katasea.bean.vo.ResponseVO;
import cn.katasea.exception.PayBusinessException;
import cn.katasea.main.service.FileService;
import cn.katasea.main.service.util.CommandUtil;
import cn.katasea.main.service.util.FileUtil;
import cn.katasea.main.service.util.RSAUtil;
import cn.katasea.main.service.util.RSAUtils4Mall;
import cn.katasea.myenum.HandlerType;
import cn.katasea.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Base64;
import java.util.Map;

/**
 * @author katasea
 * 2020/12/10 14:24
 */
@Controller
@Slf4j
public class Log {

	@Value("${logView.userId:logview}")
	private String realUserId;
	@Value("${logView.password:katasea@github}")
	private String realPassword;

	@Resource
	FileService fileService;

	@RequestMapping(value = {"/",""})
	public String indexForward(HttpServletRequest request) {
		return "redirect:/login";
	}

	@RequestMapping("/index")
	public String index(HttpServletRequest request) {
		return "index";
	}

	@RequestMapping("/view")
	public String view(HttpServletRequest request,@RequestParam String path) {
		return "detail";
	}

	@GetMapping("/login")
	public String loginPage(HttpServletRequest request) {
		return "login";
	}
	/**
	 * 视情况开放
	 *
	 */
//	@PostMapping(value = "/privateKey")
//	@ResponseBody
//	public ResponseVO<String> getPrivateKey() throws Exception {
//		RSAUtils4Mall rsaUtils = new RSAUtils4Mall();
//		rsaUtils.getKeys();
//		ResponseVO<String> responseVO = new ResponseVO<>();
//		responseVO.setBizObj(Base64.getEncoder().encodeToString(rsaUtils.getPrivateKey().getEncoded()));
//		return responseVO;
//	}

	/**
	 * 视情况开发
	 *
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/publicKey")
	@ResponseBody
	public ResponseVO<String> getPublicKey() throws Exception {
		RSAUtils4Mall rsaUtils = new RSAUtils4Mall();
		rsaUtils.getKeys();
		ResponseVO<String> responseVO = new ResponseVO<>();
		responseVO.setBizObj(Base64.getEncoder().encodeToString(rsaUtils.getPublicKey().getEncoded()));
		return responseVO;
	}

	@PostMapping("/logout")
	@ResponseBody
	public ResponseVO<String> logout(HttpServletRequest request) {
		request.getSession().removeAttribute("isLogin");
		return new ResponseVO<>();
	}
	/**
	 * 登录
	 */
	@PostMapping("/login")
	@ResponseBody
	public ResponseVO<Map<String, Object>> doLogin(HttpServletRequest request, String userId, String password) {
		ResponseVO<Map<String, Object>> responseVO = new ResponseVO<>();
		try {
			RSAUtils4Mall rsaUtils = new RSAUtils4Mall();
			rsaUtils.getKeys();
			//解密
			userId = rsaUtils.decodeByPrivateKey(userId.trim());
			password = rsaUtils.decodeByPrivateKey(password.trim());
			System.out.println(userId+password);
			if(CommonUtil.isNotEmpty(userId) && userId.equals(realUserId)) {
				if(CommonUtil.isNotEmpty(password) && password.equals(realPassword)) {
					request.getSession().setAttribute("isLogin","1");
					return responseVO;
				}
			}
			throw new PayBusinessException("用户名密码错误，请重新输入！");

		} catch (Exception e) {
			log.error("登录错误！错误信息：{}",e.getMessage());
			responseVO.setRetInfo(HandlerType.OUT_SYSTEM_ERROR);
			responseVO.setRetMsg(e.getMessage());
		}
		return responseVO;
	}
	@PostMapping("/execute")
	@ResponseBody
	public ResponseVO<String> execute(HttpServletRequest request, String code) {
		ResponseVO<String> responseVO = new ResponseVO<>();
		try {
			code = URLDecoder.decode(code,"utf-8");
			log.info("开始执行命令{}",code);
			String result = CommandUtil.executeWindows(code);
			log.info("命令执行完毕[{}]返回：{}",code,result);
			responseVO.setBizObj(result);
		} catch (Exception e) {
			log.error("错误信息：{}",e.getMessage());
			responseVO.setRetInfo(HandlerType.OUT_SYSTEM_ERROR);
			responseVO.setRetMsg(e.getMessage());
		}
		return responseVO;
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

	@PostMapping(value = "/file/upload")
	@ResponseBody
	public ResponseVO<String> fileUpload(@RequestParam("file") MultipartFile file,
	                            @RequestParam(value = "fileUrl", required = true) String fileUrl){
		ResponseVO<String> responseVO = new ResponseVO<>();
		try {
			File temp = new File(fileUrl);
			if(!temp.isDirectory()) {
				fileUrl = fileUrl.substring(0,fileUrl.lastIndexOf("/"));
			}
			File newFile=new File(fileUrl+File.separator+file.getOriginalFilename());
			log.info("开始上传文件：{}",fileUrl+File.separator+file.getOriginalFilename());
			file.transferTo(newFile);

		}catch (Exception e) {
			responseVO.setRetInfo(HandlerType.SYSTEM_ERROR);
			responseVO.setRetMsg("上传文件失败！"+e.getMessage());
		}
		return responseVO;
	}



}
