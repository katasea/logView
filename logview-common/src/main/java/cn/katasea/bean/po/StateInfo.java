package cn.katasea.bean.po;

import cn.katasea.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * 状态信息描述
 * 用于AJAX返回
 *
 * @author CFQ
 */
public class StateInfo implements Serializable {
	Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     *
     */
    private static final long serialVersionUID = 1054440207147560676L;
    /**
     * 处理结果
     */
    private boolean flag = true;
    /**
     * 处理信息
     */
    private String msg;

	/**
	 * 数据
	 */
	private Object data;


	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(Class c,String msg,Exception e) {
        if(e!= null) {
            logger.error(e.getMessage(),e);
	        e.printStackTrace();
        }else {
	        logger.error(msg);
        }
        if(CommonUtil.isEmpty(msg)) {
        	msg = "空指针异常！请查看日志排查！";
        }
	    this.msg = msg;
    }
}
