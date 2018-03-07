package com.songxm.qiuqiu.manage.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.github.pagehelper.PageInfo;
import com.songxm.qiuqiu.common.bean.EUDataGridResult;
import com.songxm.qiuqiu.manage.pojo.Item;
import com.songxm.qiuqiu.manage.service.ItemService;
import lombok.extern.slf4j.Slf4j;

@RequestMapping("item")
@Controller
@Slf4j
public class ItemController {

    @Autowired
    private ItemService itemService;

    

    /**
     * 提供REST接口，分页查询商品数据，商品数据是按照更新时间做倒序排序
     * 
     * @param page  当前页
     * @param rows  页面大小
     * @return  返回http状态为200时包含 EUDataGridResult 的数据，否则返回500状态
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody //将EUDataGridResult序列化为json数据返回
    public ResponseEntity<EUDataGridResult> queryListByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "30") Integer rows) {
        try {
        	if(log.isDebugEnabled()){
        		//输出入参参数
        		log.debug("分页查询商品数据,page={},rows={}",page,rows);
        	}
            PageInfo<Item> pageInfo = this.itemService.queryListByPage2(page, rows);
            
            if(log.isDebugEnabled()){
            	//输出结果
            	log.debug("分页查询商品数据，pageInfo = {}",pageInfo);
            }
            return ResponseEntity.ok(new EUDataGridResult(pageInfo.getTotal(), pageInfo.getList()));
        } catch (Exception e) {
           log.error("分页查询商品出错！{}",e);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    /**
     * 保存商品
     * 
     * @param item
     * @param desc
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> saveItem(Item item, @RequestParam("desc") String desc,
            @RequestParam("itemParams") String itemParams) {
        try {

            if (log.isDebugEnabled()) {
                log.debug("开始保存商品数据，传入的参数是： item = {}, desc = {}", item, desc);
            }
            this.itemService.saveItem(item, desc, itemParams);
            if (log.isDebugEnabled()) {
                log.debug("保存商品数据成功， itemId = {}", item.getId());
            }
            // return ResponseEntity.status(HttpStatus.OK).build();
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("保存商品数据出现异常！item = " + item, e);
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

   

}
