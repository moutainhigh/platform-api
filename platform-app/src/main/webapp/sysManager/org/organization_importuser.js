/**
 * 引入用户
 * @author shiyong
 */
var zTreeObj;

//树 搜索名称参数
var lastValue = "", nodeList = [], fontCss = {};

//zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
var setting = {
		view: {
            fontCss: getFontCss
        },  
	edit: {
		enable: false,
		showRemoveBtn: false,
		showRenameBtn: false
	},
	data: {
		simpleData: {
			enable: true
		}
	},
	callback: {
		onDblClick: zTreeOnDblClick,
		onCollapse: function(){
            $.xljUtils.treeResizeFn();
        },
        onExpand: function(){
            $.xljUtils.treeResizeFn();
        }
	}
};

var newrowid = 0;
function zTreeOnDblClick(event, treeId, treeNode) {
	var isexist = false ;
	var obj=$("#listUser").jqGrid("getRowData");
	jQuery(obj).each(function(){
		if(this.id == treeNode.id){
			isexist =  true;
		}
	});
	
	if(isexist){
		pop_tip_open("blue","人员已存在，请重新选择");
		return false;
	}
	if(treeNode.type == 'user'){
		var selectedId = $("#listUser").jqGrid("getGridParam", "selrow");   
	    var ids = jQuery("#listUser").jqGrid('getDataIDs');  
	    //获得当前最大行号（数据编号）  
//	    var rowid = Math.max.apply(Math,ids);  
	    //获得新添加行的行号（数据编号）  
	    newrowid = newrowid+1;  
	    var dataRow = {    
	        id: treeNode.id,  
	        realName:treeNode.name, 
	        loginName:treeNode.loginName,
	        sort:''
	    }; 
	    //将新添加的行插入到第一列  
	    $("#listUser").jqGrid("addRowData", newrowid, dataRow, "first");
		$.xljUtils.addGridScroll();
		$.xljUtils.gridResizeFn();
	}else{
		pop_tip_open("blue","只能选择用户，不能选择组织");
		return false;
	}
    
};

/**
 * 初始化已选择的用户
 */
function initJqGridUser(){
	var ubody = "sys/org/user/queryUserListByPostId";
	var uall = hostUrl+ubody;
	var postId = window.opener.impostId;
    //创建jqGrid组件
    var jqGrid2 = jQuery("#listUser").jqGrid(
        {
        	url: uall,
            ajaxGridOptions: { contentType: 'application/json' },
            mtype : "POST",  
            contentType : "application/json",  
//            postData:{"orgId":"000fbb2eef694532ab9d8orgorg03"},
            postData:{"postId":postId},
            datatype : "json",
            jsonReader : {
                root:"result"
            },
            rownumbers: true,
            colModel : [ //jqGrid每一列的配置信息。包括名字，索引，宽度,对齐方式.....
                         {name : 'id',label : '序号',width : 55,align : "center",hidden : true},
                         {name : 'realName',label : '用户名',width : 410,align : "center"},
                         {name : 'loginName',label : '账号',width : 517,align : "center"}
            ],
            rowNum : -1,//一页显示多少条
            sortname : 'id',//初始化的时候排序的字段
            sortorder : "desc",//排序方式,可选desc,asc
            ondblClickRow:function(rowid,iRow){
            	$("#listUser").jqGrid("delRowData", rowid);
				$.xljUtils.addGridScroll();
				$.xljUtils.gridResizeFn();
            },
            loadError:function(xhr,status,error){
            	pop_tip_open("red","初始化已选择的用户列表请求失败");
            },
            viewrecords : true
        });
}


/*
 * 树搜索方法
 */

function focusKey(e) {
	if (key.hasClass("empty")) {
		key.removeClass("empty");
	}
}
function blurKey(e) {
	if (key.get(0).value === "") {
		key.addClass("empty");
	}
}

function clickRadio(e) {
	/*lastValue = "";
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	var value = $.trim(key.get(0).value);
	//如果搜索框内无内容，不进行搜索，展开所有节点
	if(value == ""){
		zTree.expandAll(true);
		$.xljUtils.treeResizeFn();
	}else{
		searchNode();
	}*/
	var searchKeys = ['loginName', 'name'];
	$.xljUtils._searchTreeBtnEvent(key,zTreeObj, searchKeys);
//	$.xljUtils._searchTreeBtnEvent(key,zTreeObj);
}
function searchNode(e) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	var keyType = "name";
	var keyTypeLoginName ="loginName";
	
	var value = $.trim(key.get(0).value);
	if (lastValue === value) return;
	lastValue = value;
	if (value === "") return;
	updateNodes(false);

	nodeList = zTree.getNodesByParamFuzzy(keyType, value);
	var nodeListLoginName = zTree.getNodesByParamFuzzy(keyTypeLoginName, value);
	nodeList = nodeList.concat(nodeListLoginName);
	for(var i=0;i<nodeList.length;i++){
		var node=nodeList[i];
		var parentNode=node.getParentNode();
		if(parentNode && !parentNode.open){
			zTree.expandNode(parentNode,true,false,false,true);
		}
	}

	updateNodes(true);

}
function updateNodes(highlight) {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	for( var i=0, l=nodeList.length; i<l; i++) {
		nodeList[i].highlight = highlight;
		zTree.updateNode(nodeList[i]);
	}
}
function getFontCss(treeId, treeNode) {
	/*return (!!treeNode.highlight) ? {color:"#A60000", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};*/
	return (treeNode.highlight&&treeNode.highlight=='true') ?
            {'font-family': 'Verdana, Arial, Helvetica, AppleGothic, sans-serif','font-style':'italic', "font-weight":"bold"} :
                {color:"#333", "font-weight":"normal",'font-style':'normal'} | (treeNode.status&&treeNode.status=='0') ?
                {'font-family': 'Verdana, Arial, Helvetica, AppleGothic, sans-serif','font-style':'normal', 'color':'#CD0000'} :
                {color:"#333", "font-weight":"normal",'font-style':'normal'};
}
function filter(node) {
	return !node.isParent && node.isFirstNode;
}

function showLog(str) {
	if (!log) log = $("#log");
	log.append("<li class='"+className+"'>"+str+"</li>");
	if(log.children("li").length > 8) {
		log.get(0).removeChild(log.children("li")[0]);
	}
}
function getTime() {
	var now= new Date(),
	h=now.getHours(),
	m=now.getMinutes(),
	s=now.getSeconds(),
	ms=now.getMilliseconds();
	return (h+":"+m+":"+s+ " " +ms);
}

function setTrigger() {
	var zTree = $.fn.zTree.getZTreeObj("treeDemo");
	zTree.setting.edit.drag.autoExpandTrigger = $("#callbackTrigger").attr("checked");
}



/**
 * 递归设置树的图片样式
 */
function recursionArray(arr) {
	//所属的分类 diy-group 目录 diy-company 集团和公司;diy-program 项目和分期;diy-department 部门;
    for(var i in arr) {
    	if(/['"#$%&\^*]/.test(arr[i].name)){
    		arr[i].name=$.xljUtils.htmlDecode(arr[i].name);
    	}
    	if(arr[i].type == "zb" || arr[i].type == "company") {
            arr[i].iconSkin = "diy-company";
            if(arr[i].children && arr[i].children.length > 0) {
                recursionArray(arr[i].children);
            }
        }else if(arr[i].type == "dept" ) {
            arr[i].iconSkin = "diy-department";
            if(arr[i].children && arr[i].children.length > 0) {
                recursionArray(arr[i].children);
            }
        }else if(arr[i].type == "group" ) {
            arr[i].iconSkin = "diy-program";
            if(arr[i].children && arr[i].children.length > 0) {
                recursionArray(arr[i].children);
            }
        }else if(arr[i].type == "branch" ) {
            arr[i].iconSkin = "diy-program";
            if(arr[i].children && arr[i].children.length > 0) {
                recursionArray(arr[i].children);
            }
        }else if(arr[i].type == "cata" ) {
        	arr[i].iconSkin = "diy-group";
            if(arr[i].children && arr[i].children.length > 0) {
                recursionArray(arr[i].children);
            }
        }else if(arr[i].type == "user" ) {
        	arr[i].iconSkin = "diy-member";
        } 
    }
};
var key;
/**
 * 获取人员树
 */
function getUserTree() {
    // var urlBody = "sys/org/user/getUserTree";
    var urlBody = "sys/org/roleUser/selectUserOrgTree";
    var urlAll = hostUrl + urlBody;
    var jsonData={
    		rootDelFlag:0,
    		orgDelFlag:0,
    		userDelFlag:0,
    		rootStatus:1,
    		userStatus:1,
    		orgStatus:1
    };
    $.ajax({
        type:'POST',
        url:urlAll,
        dataType:'json',
        contentType:'application/json',
        data:JSON.stringify(jsonData),
        success: function(json) {
            var zNodes = json.result;
            recursionArray(zNodes);
            zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            var pid = window.opener.imtreeId;
            var node = zTreeObj.getNodeByParam("id",pid);
            zTreeObj.selectNode(node,true);//指定选中ID的节点  
            zTreeObj.expandNode(node, true, false);//指定选中ID节点展开  
            key = $("#key");
			/*key.bind("focus", focusKey)
			.bind("blur", blurKey)
			.bind("propertychange", searchNode)
			.bind("input", searchNode);*/
            var searchKeys = ['loginName', 'name'];
            $.xljUtils._searchTreeInputEvent(key,zTreeObj,searchKeys);
			setTimeout(function(){
				$.xljUtils.addTreeScroll('ztree-box');
				$.xljUtils.treeResizeFn();
			},300);
        },error:function(XMLHttpRequest, textStatus, errorThrown){
        	pop_tip_open("red","获取人员树请求失败");
        }
    })
}

/**
 * 保存引入用户，用户和岗位的关系
 */
function saveUserPost() {
    var uBody = "sys/org/postUser/saveBatch";
    var uAll = hostUrl + uBody;
    var obj=$("#listUser").jqGrid("getRowData");

    var ids = "";
    var uuid = "";
    jQuery(obj).each(function(){
        var uuidBody = "generator/getGuuid"+"?time="+Math.random();
        var uuidAll = hostUrl + uuidBody;
    	ids += this.id + ",";
    	$.ajax({
            type:'GET',
            url:uuidAll,
            dataType:'json',
            async:false,
            contentType:'application/json',
//            data:'{}',
            success: function(json) {
//                $('#id').val(json.result);
            	uuid += json.result+",";
            },error:function(XMLHttpRequest, textStatus, errorThrown){
            	pop_tip_open("red","获取ID请求失败");
            }
        });

    });
    if(ids == ""){
    	pop_tip_open("blue","请选择人员");
    	return false;
    }
    if (ids.length > 0 ) ids = ids.substring(0, ids.length-1);
    if (uuid.length > 0 ) uuid = uuid.substring(0, uuid.length-1);
    var savedata={
    		postIds:window.opener.impostId,
    		userIds:ids,
    		uuids:uuid,
    		type:"users"
    };
    
    $.ajax({
        type:'POST',
        url:uAll,
        async: false,
        dataType:'json',
        contentType:'application/json',
        data:JSON.stringify(savedata),
        success: function(json) {
            if(json.success == true){
            	var queryDataPostUser={
            			"postId":window.opener.impostId
            			};
            	window.opener.jqGridPostUser.jqGrid("setGridParam", { postData: queryDataPostUser }).trigger("reloadGrid");
            	window.close();
            }else{
            	pop_tip_open("red",json.msg);
            }
        },error:function(XMLHttpRequest, textStatus, errorThrown){
        	pop_tip_open("red","保存引入用户请求失败");
        }
    })
}


$(function(){
	//计算高度
	resizeHeight();
	function resizeHeight(){
		//左侧  头部底部为60px  title类 为50px
		var w_h = $(window).height();
		$(".slide-left .ztree-box").height((w_h-160)+"px");
		//右侧table
		$(".con-table .mytable").height(w_h-90+"px");
	}
//grid 自适应宽度
	$(window).resize(function(){
		resizeHeight();
		$("#listRole").setGridWidth($('.mytable').width());
	});

	//初始化initJqGridUser
	initJqGridUser();
	//初始化角色树
	getUserTree();
	//页面加载完毕后更改grid宽高
	$.xljUtils.resizeNestedGrid();
});


