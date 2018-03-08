Date.prototype.format = function(format){ 
    var o =  { 
    "M+" : this.getMonth()+1, // month
    "d+" : this.getDate(), // day
    "h+" : this.getHours(), // hour
    "m+" : this.getMinutes(), // minute
    "s+" : this.getSeconds(), // second
    "q+" : Math.floor((this.getMonth()+3)/3), // quarter
    "S" : this.getMilliseconds() // millisecond
    };
    if(/(y+)/.test(format)){ 
    	format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
    }
    for(var k in o)  { 
	    if(new RegExp("("+ k +")").test(format)){ 
	    	format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
	    } 
    } 
    return format; 
};

var TT = QIUQIU = {
	// 编辑器参数
	kingEditorParams : {
		filePostName  : "uploadFile",  //上传文件名
		uploadJson : '/rest/pic/upload',//上传路径
		dir : "image"//上传文件类型
	},
	// 格式化时间
	formatDateTime : function(val,row){
		var now = new Date(val);
    	return now.format("yyyy-MM-dd hh:mm:ss");
	},
	// 格式化连接
	formatUrl : function(val,row){
		if(val){
			return "<a href='"+val+"' target='_blank'>查看</a>";			
		}
		return "";
	},
	// 格式化价格
	formatPrice : function(val,row){
		return (val/100).toFixed(2);
	},
	// 格式化商品的状态
	formatItemStatus : function formatStatus(val,row){
        if (val == 1){
            return '正常';
        } else if(val == 2){
        	return '<span style="color:red;">下架</span>';
        } else {
        	return '未知';
        }
    },
    
    init : function(data){
    	this.initPicUpload(data);
    	this.initItemCat(data);
    },
    // 初始化图片上传组件
    initPicUpload : function(data){
    	// 寻找页面中class为picFileUpload的标签，对找到的所有对象进行遍历
    	$(".picFileUpload").each(function(i,e){
    		// 循环的对象转换为jQuery对象
    		var _ele = $(e);
    		//找到同辈的div标签，class为pics
    		_ele.siblings("div.pics").remove();
    		//     “\”的作用是进行换行
    		_ele.after('\
    			<div class="pics">\
        			<ul></ul>\
        		</div>');
    		// 回显图片
        	if(data && data.pics){
        		//对pics数据进行切分，获取图片url数组
        		var imgs = data.pics.split(",");
        		//遍历数组
        		for(var i in imgs){
        			if($.trim(imgs[i]).length > 0){
        				//图片的回显
        				_ele.siblings(".pics").find("ul").append("<li><a href='"+imgs[i]+"' target='_blank'><img src='"+imgs[i]+"' width='80' height='50' /></a></li>");
        			}
        		}
        	}
        	//先解绑再绑定，做一个click的清理
        	$(e).unbind('click').click(function(){
        		//找到图片上传按钮的父元素的“form”标签
        		var form = $(this).parentsUntil("form").parent("form");
        		//加载富文本编辑器的图片上传组件
        		KindEditor.editor(TT.kingEditorParams).loadPlugin('multiimage',function(){
        			var editor = this;
        			//打开上传窗窗口
        			editor.plugin.multiImageDialog({
						clickFn : function(urlList) {
							var imgArray = [];
							//多张图片上传，进行遍历
							KindEditor.each(urlList, function(i, data) {
								//把上传成功的图片url存放imgArray数组中
								imgArray.push(data.url);
								//把图片进行回显
								form.find(".pics ul").append("<li><a href='"+data.url+"' target='_blank'><img src='"+data.url+"' width='80' height='50' /></a></li>");
							});
							//把数组中的图片url做一个拼接，拼接为字符串，里面用，分隔
							form.find("[name=image]").val(imgArray.join(","));
							//关闭上传组件
							editor.hideDialog();
						}
					});
        		});
        	});
    	});
    },
    
    // 初始化选择类目组件
    initItemCat : function(data){
    	// 寻找页面中class为selectItemCat的标签，对找到的所有对象进行遍历
    	$(".selectItemCat").each(function(i,e){
    		// 循环的对象转换为jQuery对象
    		var _ele = $(e);
    		//商品类目的回显
    		if(data && data.cid){
    			_ele.after("<span style='margin-left:10px;'>"+data.cid+"</span>");
    		}else{
    			_ele.after("<span style='margin-left:10px;'></span>");
    		}
    		// 解绑click，再绑定click
    		_ele.unbind('click').click(function(){
    			// 添加一个div标签，在div上添加了一个ul标签
    			$("<div>").css({padding:"5px"}).html("<ul>")
    			.window({
    				width:'500',
    			    height:"450",
    			    modal:true,
    			    closed:true,
    			    iconCls:'icon-save',
    			    title:'选择类目',
    			    // 窗口打开之后，执行以下逻辑
    			    onOpen : function(){
    			    	var _win = this;
    			    	// 在当前打开的窗口，查找ul标签
    			    	$("ul",_win).tree({
    			    		url:'/rest/item/cat',
    			    		method:'GET',
    			    		animate:true,
    			    		onClick : function(node){
    			    			//判断当前点击的节点是否是叶子节点
    			    			if($(this).tree("isLeaf",node.target)){
    			    				// 填写到cid中
    			    				_ele.parent().find("[name=cid]").val(node.id);
    			    				_ele.next().text(node.text).attr("cid",node.id);
    			    				$(_win).window('close');
    			    				if(data && data.fun){
    			    					//回调函数，this就是按钮，node就是叶子节点
    			    					data.fun.call(this,node);
    			    				}
    			    			}
    			    		}
    			    	});
    			    },
    			    onClose : function(){
    			    	$(this).window("destroy");
    			    }
    			    // 打开一个窗口
    			}).window('open');
    		});
    	});
    },
    
    createEditor : function(select){
    	return KindEditor.create(select, TT.kingEditorParams);
    },
    
    /**
	 * 创建一个窗口，关闭窗口后销毁该窗口对象。<br/>
	 * 
	 * 默认：<br/> width : 80% <br/> height : 80% <br/> title : (空字符串) <br/>
	 * 
	 * 参数：<br/> width : <br/> height : <br/> title : <br/> url : 必填参数 <br/>
	 * onLoad : function 加载完窗口内容后执行<br/>
	 * 
	 * 
	 */
    createWindow : function(params){
    	$("<div>").css({padding:"5px"}).window({
    		width : params.width?params.width:"80%",
    		height : params.height?params.height:"80%",
    		modal:true,
    		title : params.title?params.title:" ",
    		href : params.url,
		    onClose : function(){
		    	$(this).window("destroy");
		    },
		    onLoad : function(){
		    	if(params.onLoad){
		    		params.onLoad.call(this);
		    	}
		    }
    	}).window("open");
    },
    
    closeCurrentWindow : function(){
    	$(".panel-tool-close").click();
    },
    
    changeItemParam : function(node,formId){
    	//发起get的请求
    	$.getJSON("/rest/item/param/" + node.id,function(data){
    			//如果data有数据则执行逻辑
			  if(data){
				 //显示商品规格参数所在的元素
				 $("#"+formId+" .params").show();
				 //转换成json对象
				 var paramData = JSON.parse(data.paramData);
				 var html = "<ul>";
				 //遍历paramData
				 for(var i in paramData){
					 var pd = paramData[i];
					 html+="<li><table>";
					 html+="<tr><td colspan=\"2\" class=\"group\">"+pd.group+"</td></tr>";
					 
					 for(var j in pd.params){
						 var ps = pd.params[j];
						 html+="<tr><td class=\"param\"><span>"+ps+"</span>: </td><td><input autocomplete=\"off\" type=\"text\"/></td></tr>";
					 }
					 
					 html+="</li></table>";
				 }
				 html+= "</ul>";
				 $("#"+formId+" .params td").eq(1).html(html);
			  }else{
				 $("#"+formId+" .params").hide();
				 $("#"+formId+" .params td").eq(1).empty();
			  }
		  });
    },
    getSelectionsIds : function (select){
    	var list = $(select);
    	var sels = list.datagrid("getSelections");
    	var ids = [];
    	for(var i in sels){
    		ids.push(sels[i].id);
    	}
    	ids = ids.join(",");
    	return ids;
    },
    
    /**
	 * 初始化单图片上传组件 <br/> 选择器为：.onePicUpload <br/> 上传完成后会设置input内容以及在input后面追加<img>
	 */
    initOnePicUpload : function(){
    	$(".onePicUpload").click(function(){
			var _self = $(this);
			KindEditor.editor(TT.kingEditorParams).loadPlugin('image', function() {
				this.plugin.imageDialog({
					showRemote : false,
					clickFn : function(url, title, width, height, border, align) {
						var input = _self.siblings("input");
						input.parent().find("img").remove();
						input.val(url);
						input.after("<a href='"+url+"' target='_blank'><img src='"+url+"' width='80' height='50'/></a>");
						this.hideDialog();
					}
				});
			});
		});
    }
};
