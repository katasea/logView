package cn.katasea.main.service;

import cn.katasea.bean.po.FileBean;
import cn.katasea.bean.vo.RequestVO;
import cn.katasea.exception.PayBusinessException;

import java.util.List;
import java.util.Map;

/**
 * @author katasea
 * 2020/12/9 16:39
 */
public interface FileService {
	List<FileBean> getFileList(RequestVO<Map<String,Object>> requestVO) throws PayBusinessException;
	String getFileContent(RequestVO<Map<String,Object>> requestVO) throws Exception;
	void modifyFile(RequestVO<Map<String,Object>> requestVO) throws Exception;
}
