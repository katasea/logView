package cn.katasea.main.service;

import cn.katasea.bean.vo.RequestVO;
import cn.katasea.bean.vo.ResponseVO;

/**
 * @author katasea
 * 2019/9/16 10:32
 */
public interface ReflectionService {
	ResponseVO<Object> doHandler(RequestVO requestVO);
}
