package com.taotao.manage.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "tb_item_desc")
public class ItemDesc extends BasePojo{
    
    @Id//对应tb_item中的id
    @Column(name = "item_id")
    private Long itemId;
    
    @Column(name = "item_desc")
    private String itemDesc;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }
    
    

}
