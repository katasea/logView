package cn.katasea.bean.po;

import lombok.Data;

/**
 * @author katasea
 * 2020/12/9 16:56
 */
@Data
public class FileBean {
	private String name;
	private String dir;
	private int type;
	private long totalSpace;
	private long freeSpace;
	private String usedSpace;
	private String modifyTime;

}
