/**
 * 
 */
package com.songxm.qiuqiu.common.bean;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * @author sxm
 *
 */
@SuppressWarnings("serial")
@Data
public class EUDataGridResult implements Serializable {

	public long total;
	public List<?> rows;
	public EUDataGridResult(long total, List<?> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}
}
