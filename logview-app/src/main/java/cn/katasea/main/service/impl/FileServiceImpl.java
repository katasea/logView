package cn.katasea.main.service.impl;

import cn.katasea.bean.po.FileBean;
import cn.katasea.bean.vo.RequestVO;
import cn.katasea.exception.PayBusinessException;
import cn.katasea.main.service.FileService;
import cn.katasea.main.service.util.EncodeUtils;
import cn.katasea.util.CommonUtil;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author katasea
 * 2020/12/9 16:39
 */
@Service
public class FileServiceImpl implements FileService {
	@Override
	public List<FileBean> getFileList(RequestVO<Map<String,Object>> requestVO) throws PayBusinessException {
		Map<String,Object> params = requestVO.getBizObj();
		List<FileBean> childResult = new ArrayList<>();
		if(params!=null && params.keySet().size()!=0) {
			String parentDir = String.valueOf(params.get("parentDir"));
			File[] childrends = null;
			if(CommonUtil.isEmpty(parentDir)) {
				childrends = File.listRoots();
			}else {
				File parent = new File(parentDir);
				if(parent.isFile()) {
					throw new PayBusinessException("当前路径是一个文件，无法获取子目录");
				}
				childrends = parent.listFiles(); // 如果是目录，获取该目录下的内容集合
			}
			if(childrends!=null && childrends.length!=0) {
				for (int i = 0; i < childrends.length; i++) { // 循环遍历这个集合内容
					FileBean fileBean = new FileBean();
					File temp = childrends[i];
					fileBean.setDir(temp.getPath().replace("\\","/"));
					fileBean.setName(temp.getName());
					if(CommonUtil.isEmpty(fileBean.getName())) {
						fileBean.setName(temp.toString());
					}
					fileBean.setTotalSpace(temp.getTotalSpace()/1024/1024);
					fileBean.setFreeSpace(temp.getUsableSpace()/1024/1024);

					if (temp.isDirectory()) {    //判断元素是不是一个目录
						fileBean.setType(0);
						fileBean.setUsedSpace(String.valueOf(fileBean.getTotalSpace()-fileBean.getFreeSpace())+"MB");
					}else {
						if(temp.length()/1024/1024 == 0) {

							fileBean.setUsedSpace(String.valueOf(temp.length()/1024)+"KB");
						}else {
							fileBean.setUsedSpace(String.valueOf(temp.length()/1024/1024)+"MB");
						}
						fileBean.setType(1);
					}
					fileBean.setModifyTime(String.valueOf(temp.lastModified()));
					childResult.add(fileBean);
				}
			}else {
				throw new PayBusinessException("未找到子目录信息，请确认路径是否正确！");
			}
		}else {
			throw new PayBusinessException("未获取相关入参，入参为空！");
		}
		return childResult;
	}

	@Override
	public String getFileContent(RequestVO<Map<String, Object>> requestVO) throws Exception {
		Map<String,Object> params = requestVO.getBizObj();
		String fileUrl = String.valueOf(params.get("filePath"));
		String encodeType = null;
		File file = new File(fileUrl);
		InputStream is = null;
		BufferedReader br = null;
		try {
			encodeType = EncodeUtils.getEncode(fileUrl, true);
			is = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(is,encodeType));
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ((line = br.readLine()) != null){
				sb.append(line+"\r\n");
			}
			return new String(sb.toString().getBytes());
		}catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}finally {
			if(null != br) {
				br.close();
			}
			if(null != is) {
				is.close();
			}
		}
	}
}
