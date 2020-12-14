package cn.katasea.bean.po;


import lombok.Data;

import java.io.Serializable;

/**
 * application information record
 *
 * @author katasea
 */
@Data
public class Application implements Serializable{

	private static final long serialVersionUID = -3659005483062971583L;

	private String appId;
	private String appName;
	private String appSecret;
	private String createTime;
	private String createUserId;
	private String updateTime;
	private String appType;
	private String enable;
	private String extra;

}
