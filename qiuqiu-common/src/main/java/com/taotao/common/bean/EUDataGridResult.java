/**
 * 
 */
package com.taotao.common.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author sxm
 *
 */
@SuppressWarnings("serial")
public class EUDataGridResult implements Serializable {

	public long total;
	public List<?> rows;
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public List<?> getRows() {
		return rows;
	}
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	public EUDataGridResult(long total, List<?> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}
	
	
}
