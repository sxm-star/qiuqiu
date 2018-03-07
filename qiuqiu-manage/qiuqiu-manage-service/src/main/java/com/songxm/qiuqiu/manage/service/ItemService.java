package com.songxm.qiuqiu.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.songxm.qiuqiu.manage.pojo.Item;
import com.songxm.qiuqiu.manage.pojo.ItemDesc;
import com.songxm.qiuqiu.manage.pojo.ItemParamItem;

@Service
public class ItemService extends BaseService<Item> {

    @Autowired
    private ItemDescService itemDescService;

    @Autowired
    private ItemParamItemService itemParamItemService;

    /**
     * 保存商品数据
     * 
     * @param item
     * @param desc
     * @param itemParams
     */
    public void saveItem(Item item, String desc, String itemParams) {
        // 保存商品数据
        item.setId(null);   //从安全角度考虑强制为null
        item.setStatus(1);
        super.save(item);

        // 保存描述数据
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(item.getId());
        itemDesc.setItemDesc(desc);
        this.itemDescService.save(itemDesc);

        // 保存商品规格数据
        ItemParamItem itemParamItem = new ItemParamItem();
        itemParamItem.setId(null);
        itemParamItem.setItemId(item.getId());
        itemParamItem.setParamData(itemParams);
        this.itemParamItemService.save(itemParamItem);
    }

    /**
     * 分页查询
     * 
     * @param page
     * @param rows
     * @return
     */
    public PageInfo<Item> queryListByPage(Integer page, Integer rows) {
        PageHelper.startPage(page, rows);
        Example example = new Example(Item.class);
        example.setOrderByClause("updated DESC");
        example.createCriteria().andEqualTo("status", 1);
        List<Item> list = super.getMapper().selectByExample(example);

        // 有问题的
        // List<Item> list = super.queryAll();

        return new PageInfo<Item>(list);
    }

    /**
     * 分页查询2
     * 
     * @param page
     * @param rows
     * @return
     * @throws Exception
     */
    public PageInfo<Item> queryListByPage2(Integer page, Integer rows) throws Exception {
        Item item = new Item();
        item.setStatus(1);

        return super.queryListByPageAndOrder(item, page, rows, "updated DESC");
    }

    /**
     * 更新商品
     * 
     * @param item
     * @param desc
     */
//    public void updateItem(Item item, String desc) {
//        // 商品数据保存
//        this.updateByIdSelective(item);
//
//        // 商品描述保存
//        ItemDesc itemDesc = this.itemDescService.queryByID(item.getId());
//        itemDesc.setItemDesc(desc);
//        this.itemDescService.updateByIdSelective(itemDesc);
//
//    }
}
