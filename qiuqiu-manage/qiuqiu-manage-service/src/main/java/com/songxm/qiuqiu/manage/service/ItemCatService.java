package com.songxm.qiuqiu.manage.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.songxm.qiuqiu.manage.pojo.ItemCat;

@Service
public class ItemCatService extends BaseService<ItemCat> {

    /**
     * 根据parentId查询商品类目
     * 
     * @param parentId
     * @return
     */
    public List<ItemCat> queryListByParentId(Long parentId) {
        // 构造，根据parentID查询的条件
        ItemCat itemCat = new ItemCat();
        itemCat.setParentId(parentId);
        return super.queryListByWhere(itemCat);
    }

   

}
