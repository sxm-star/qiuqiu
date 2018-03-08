package com.songxm.qiuqiu.common.bean;
import java.io.Serializable;
import lombok.Data;

/**
 * @author sxm
 * @category easyui 树状控件返回值封装
 */
@SuppressWarnings("serial")
@Data
public class EasyUITreeNode implements Serializable {

	private long id;
	private String text;
	private String state;
}
