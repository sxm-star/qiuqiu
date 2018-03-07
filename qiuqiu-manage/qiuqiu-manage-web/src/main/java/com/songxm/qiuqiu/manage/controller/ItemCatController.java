package com.songxm.qiuqiu.manage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.songxm.qiuqiu.manage.pojo.ItemCat;
import com.songxm.qiuqiu.manage.service.ItemCatService;

@RequestMapping("item/cat")
@Controller
public class ItemCatController {

	@Autowired
	ItemCatService itemCatService;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<ItemCat> queryListItemCatByParentId(
			@RequestParam(value = "id", defaultValue = "0") Long parentId) {
	
			return itemCatService.queryListByParentId(parentId);
			
		
	}

}
