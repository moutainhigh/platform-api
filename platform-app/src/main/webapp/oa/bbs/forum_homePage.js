/**
 * author:zhangfangzhi
 * date:20170620
 */
var zTreeObj;
var jqGrid2;
var currentSelectNode;
var portalForumId ;
$(function(){
	var urlParams = $.xljUtils.getUrlParams();
	portalForumId = urlParams.portalForumId;
	//计算高度
	function resizeHeight(){
		//左侧  头部底部为60px  title类 为50px
		var w_h = $(window).height();
		$(".slide-left .ztree-box").height((w_h-89)+"px");
		$(".right-content").height((w_h-28)+"px");
		$(".con-box-scroll").height((w_h-28)+"px");

	}
	resizeHeight();
	$(window).resize(function() {
		resizeHeight();
	});
	//初始化树
	getCustomTree();
	
	//初始化首页数据
	initHomePageData();
//input添加伪placeholder
	$("#title").inputPlaceholder();

	var menuArray = getOperationAuthorition();
	if($.inArray("collectBtn", menuArray)>-1){
		$('#collectBtn').show();
	}
	if($.inArray("postBtn", menuArray)>-1){
		$('#postBtn').show();
	}
	if($.inArray("delBtn", menuArray)>-1){
		$('#delBtn').show();
	}
    //初始化查询条件
	initSearch();
	
	//绑定按钮事件
	bindButton();
	
	//禁用所有按钮的默认行为
    $('.btn').click(function() {
        return false;
    });
    
    //初始化表格
	initJqGrid2();
	//查询用户信息
    getLoginUserInfo();
	
    //所有ajax请求异常的统一处理函数，处理
    $(document).ajaxError(
        function(event,xhr,options,exc ){
            if(xhr.status == 'undefined'){
                return;
            }
            switch(xhr.status){
	            case 403:
	                pop_tip_open("red","系统拒绝。");
	                break;
	            case 404:
	                pop_tip_open("red","您访问的资源不存在。");
	                break;
	            case 500:
	                pop_tip_open("red","服务器异常。");
	                break;
            }
        }
    );
});

/**
 * 按钮事件
 */
function bindButton() {
	//模糊查询按钮
	$('#searchBtn').click(function () {
		var postDataObj = $('#list2').jqGrid('getGridParam', 'postData');
		var postData = {};
		postData.status = postDataObj.status;
		postData.sortFields = postDataObj.sortFields;
		var value = $('#title').getInputVal();
		var fuzzyArr = [];
		fuzzyArr.push('title');
		postData.title = value;
		postData.fuzzyQueryFields = JSON.stringify(fuzzyArr);
		delete postDataObj[fuzzyArr[0]];
		delete postDataObj.essence;
		$('.senior-box :input').each(function (index,item) {
			delete postDataObj[item.name];
		})
		$("#list2").jqGrid('setGridParam', {postData: postData,page:1}).trigger('reloadGrid');
	});

	//精确查询按钮
	$('#exactSearchBtn').click(function () {
		var postDataObj = $('#list2').jqGrid('getGridParam', 'postData');
		var postData = {};
		var dateArr = [];
		postData.status = postDataObj.status;
		postData.sortFields = postDataObj.sortFields;
		delete  postDataObj.fuzzyQueryFields;
		delete postDataObj.title;
		delete postDataObj.essence;
			$('.senior-box input').each(function (index, item) {
				if ($.trim(item.value) != '') {
					var name = item.name;
					if (name.indexOf('starttime') > -1 || name.indexOf('endtime') > -1) {
						dateArr.push(name);
					}
					postData[item.name] = item.value;
				}
				delete postDataObj[item.name];
			});
			if (dateArr.length > 0) {
				postData.dateFields = JSON.stringify(dateArr);
			}

			delete postDataObj.dateFields;
		$("#list2").jqGrid('setGridParam', {postData: postData,page:1}).trigger('reloadGrid');
	});
}

/************************************************treeBegin******************************************/
/**
 * 树参数设置
 */
var setting = {
		view: {
            dblClickExpand: false,  
            showLine: true,  
            selectedMulti: false,
            fontCss: false,
            nameIsHTML: true
        },  
	edit: {  
		enable: true,
		showRemoveBtn:false,
        showRenameBtn:false,
        drag: {  
            autoExpandTrigger: true,  
            prev: null,  
            inner: null,  
            next: null,
            isCopy: false,
            isMove: true
        }
        
    },  
    data: {
    	keep: {
			leaf: false,
			parent: true
		},
        simpleData: {
            enable: true
        }
    },
    callback: {  
        beforeDrag: null, //拖拽前：捕获节点被拖拽之前的事件回调函数，并且根据返回值确定是否允许开启拖拽操作  
        beforeDrop: null, //拖拽中：捕获节点操作结束之前的事件回调函数，并且根据返回值确定是否允许此拖拽操作  
        beforeDragOpen: null, //拖拽到的目标节点是否展开：用于捕获拖拽节点移动到折叠状态的父节点后，即将自动展开该父节点之前的事件回调函数，并且根据返回值确定是否允许自动展开操作  
        onDrag: null, //捕获节点被拖拽的事件回调函数  
        onDrop: null, //捕获节点拖拽操作结束的事件回调函数
		onCollapse: function(){
			$.xljUtils.treeResizeFn();
		},
        onExpand:  function(){
			$.xljUtils.treeResizeFn();
		}, //捕获节点被展开的事件回调函数
        onClick:zTreeOnClick //点击节点事件
    }  
};


//论坛首页树
function getCustomTree() {
    $.ajax({
        type:'POST',
        url:hostUrl + "oa/bbs/forumType/getHomePageTree",
        dataType:'json',
        contentType:'application/json',
        data:JSON.stringify({'showAll':true}),
        success: function(json) {
        	var zNodes = json.result;
            recursionArray(zNodes);
            zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
            zTreeObj.expandAll(true);

			if(portalForumId){
				var selectNode = zTreeObj.getNodeByParam('id',portalForumId);
				if(selectNode){
					zTreeObj.expandNode(selectNode.getParentNode(),true);
					zTreeObj.selectNode(selectNode);
					setTimeout(function(){
						zTreeObj.setting.callback.onClick(null,zTreeObj.setting.treeId,selectNode);
					},1000);

				}

			}

			//加滚动条
			setTimeout(function(){
				$.xljUtils.addTreeScroll('ztree-box');
				$.xljUtils.treeResizeFn();
				
			},300);
        }
    })
}

/**
 * 递归树匹配节点icon
 */
function recursionArray(arr) {
	for(var i in arr) {
		if(arr[i].code == "") {
			arr[i].iconSkin = "diy-group";
		}else {
			arr[i].iconSkin = "diy-program";
	    } 
	}
}

/*
 * 树点击节点事件
 */
function zTreeOnClick(event, treeId, treeNode) {
	if(treeNode.code==""){
		$("#homePageToShow").hide();
		//右侧滚动条
		$.xljUtils.removeGridScroll("con-box-scroll");
		$("#listToShow").show();
		currentSelectNode=treeNode;
		var queryData={"forumId":treeNode.id};
		jqGrid2.jqGrid("setGridParam", { postData: queryData,page:1 }).trigger("reloadGrid");
		//页面加载完毕后更改grid宽高
	    $.xljUtils.resizeNestedGrid();
	}else{
		currentSelectNode=null;
	}
}

/************************************************treeEnd******************************************/

/**
 * 初始化首页数据
 */
function initHomePageData(){
	$.ajax({
		url:hostUrl+"oa/bbs/forum/queryHomepage",
		type:'post',
		dataType:'json',
		data:JSON.stringify({}),
        contentType:'application/json',
		success:function (data) {
			if (data && data.success) {
				var resultData=data.result;
				var finalHtml="";
				for(var i=0;i<resultData.length;i++){
					var reData=resultData[i];
					var forumData=reData.forumList;
					var outerHtml="<div class='item-box'><div class='right-tit'><div class='text'><i></i><span>"+reData.forumTypeName+"</span></div><span class='bg'></span></div>";
					if(forumData && forumData.length>0){
						for(var s=0;s<forumData.length;s++){
							var forumDto=forumData[s];
							var name=forumDto.name==null?0:forumDto.name;
							var forumManager=forumDto.forumManager==null?0:forumDto.forumManager;
							var topicNum=forumDto.topicNum==null?0:forumDto.topicNum;
							var replyNum=forumDto.replyNum==null?0:forumDto.replyNum;
							var title=forumDto.title==null?"":forumDto.title;
							var createTopicDate=forumDto.createTopicDate==null?"":forumDto.createTopicDate;
							var createTopicPerson=forumDto.createTopicPerson==null?"":forumDto.createTopicPerson;
							var forumTilte="<div class='con clearfix'>";
							var forumFirstDiv="<div class='left-con w30'>" +
								"<p class='p-bg'><span class='p-tit'>"+name+"</span><a href=javascript:postTopicHomePage('"+forumDto.id+"','"+forumDto.name+"')>发帖</a></p>" +
								"<p><label>版主:</label><span>"+forumManager+"</span></p>" +
								"</div>";
							var forumSecondDiv="<div class='center-con w30'>" +
								"<p><label>主题:</label><span>"+topicNum+"</span></p>" +
								"<p><label>回复:</label><span>"+replyNum+"</span></p>" +
								"</div>";
							var forumThirdDiv="<div class='right-con w40'>" +
								"<p class='clearfix'><label class='label-des pull-left'>最新发表:</label><span class='p-des pull-left' onclick=toTopicDetail('"+forumDto.topicId+"')>"+title+"</span></p>" +
								"<p><label>发帖人用户名（"+createTopicPerson+"）</label><label>发表于</label><span>"+createTopicDate+"</span></p>" +
								"</div>";
							outerHtml+=forumTilte;
							outerHtml+=forumFirstDiv;
							outerHtml+=forumSecondDiv;
							outerHtml+=forumThirdDiv;
							outerHtml+="</div>";
						}
					}
					outerHtml+="</div>";
					finalHtml+=outerHtml;
				}
				$(".con-box").append(finalHtml);
				//右侧滚动条
				$.xljUtils.addGridScroll("con-box-scroll");
			}else{
				pop_tip_open("red","查询失败！");
			}
		}
	});
}

/**
 * 初始化表格
 */
function initJqGrid2(){
    jqGrid2 = jQuery("#list2").jqGrid(
        {
        	url: hostUrl+"/oa/bbs/topic/page",
            ajaxGridOptions: { contentType: 'application/json' },
            mtype : "POST",  
            contentType : "application/json",  
            postData:{"parentId":"-1",'sortFields': JSON.stringify({'stick': 'desc', 'createDate': 'desc'})},
            datatype : "json", 
            multiboxonly:true,
            multiselect:true,
            autowidth:true,
            rownumbers: true,
            jsonReader : {
				root: function (obj) {
					var result = obj.rows;
					var arr = [];
					for(var row in result){
						var data ={};
						var rowData = result[row];
						data.id = rowData.id;
						var essence="";
						if(rowData.essence){
							essence = "<span class='bbsIcon elite'></span>";
						}
						var stick="";
						if(rowData.stick){
							stick=  "<span class='bbsIcon up'></span>";
						}
						var hot="";
						if(rowData.clickNum>200){
							hot=  "<span class='bbsIcon hot'></span>";
						}
						var closed="";
						if(rowData.closed){
							closed="<span class='bbsIcon ok'></span>";
						}
						data.title =stick+essence+hot+closed+rowData.title;
						data.forum = rowData.forum;
						data.createPersonName = rowData.createPersonName;
						data.clickNum =  rowData.clickNum;
						data.replyNum =  rowData.replyNum;
						data.updateDate =  rowData.updateDate;
						data.lastReplyUser =  rowData.lastReplyUser;
						arr.push(data);
					}
					return arr;
				},
                repeatitems: false
            },
            colModel : [ 
                 {name : 'id',label : 'id',hidden:true,align : "center"},
                 {
                 	label: '主题',
                     name: 'title',
                     width: 200,
                     editable: false
                 },
                 {
                 	 label: '版块',
                     name: 'forum',
                     width: 75,
                     editable: false
                 },
                 {
 					 label : '作者',
                     name: 'createPersonName',
                     width: 75,
                     editable: false
                 },
                 {
 					 label : '点击',
                     name: 'clickNum',
                     width: 75,
                     editable: false
                 },
                 {
 					 label : '回复',
                     name: 'replyNum',
                     width: 75,
                     editable: false
                 },
                 {
 					 label : '更新时间',
                     name: 'updateDate',
                     width: 75,
                     editable: false
                 },
                 {
 					 label : '最后回复',
                     name: 'lastReplyUser',
                     width: 75,
                     editable: false
                 }
             ],
            rowNum : 20,//一页显示多少条
            rowList : [ 20, 50, 100, 200 ],//可供用户选择一页显示多少条
            pager : '#pager2',//表格页脚的占位符(一般是div)的id
            sortname : 'id',//初始化的时候排序的字段
            sortorder : "desc",
            viewrecords : true,
            ondblClickRow:function(rowid){
				window.open('topic/topic_detail.html?id='+rowid);
            },
            gridComplete: function () {
                $.xljUtils.addGridScroll();
                $.xljUtils.gridResizeFn();
            },
        }).navGrid('#pager2', { add: false, edit: false, del: false,search:false,refresh:false });
}

/**
 * 查询初始化
 */
function initSearch(){
	$("#forumSelf").xljSingleSelector({
		title:'版块选择',//选择器标题，默认是'选择组织机构'
		selectorType:'eeee',//选择器类型，默认是组织机构选择器:org表示组织机构选择器； person表示人员选择器；post表示岗位选择器； role表示角色选择器；menu表示菜单选择器
		treeUrl:hostUrl + 'oa/bbs/forumType/getHomePageTree'+'?time='+Math.random(),// 生成zTree树的请求url,不指定使用默认对应类型的url
		treeParam:{},//生成zTree树的请求参数，json对象
		saveCallback:function (selectData,ele) {
			if (selectData != null) {
				$("#forumName").val(selectData.name);
				$("#forumId").val(selectData.id);
			}
		},
		selectNodeType:{
			"dataType":"1",
			"msg":"请选择版块！"
		},
		formatTreeJson:formatZTreeData,
		treeSettings:{data:{
			simpleData: {
				enable: true
			}
		}}
	});
	$(".fa-times").unbind('click').on('click',function () {//清除内容
		$("#forumName").val("");
		$("#forumId").val("");
	});
	
	$(".datetimepicker1").datetimepicker({ 
		  language: 'zh-CN', //语言
		  format: 'yyyy-mm-dd',//显示格式
		  minView: "month",//设置只显示到月份
		  initialDate: new Date(),//初始化当前日期
		  autoclose: true,//选中自动关闭
		  todayBtn: true//显示今日按钮
	});
}

/**
 * 查询数据
 */
function searchData(){
//	var forumTopic={};
//	forumTopic.forumId=null;
//	forumTopic.title=null;
//	forumTopic.content=null;
//	forumTopic.createPersonName=null;
//	forumTopic.startDate1=null;
//	forumTopic.startDate2=null;
//	forumTopic.updateDate1=null;
//	forumTopic.updateDate2=null;
//	
//	if($("#forumId").val()){
//		forumTopic.forumId=$("#forumId").val();
//	}
//	if($("#createPerson").val()){
//		forumTopic.createPersonName=$("#createPerson").val();
//	}
//	if($("#content").val()){
//		forumTopic.content=$("#content").val();
//	}
//	if($("#title").val()){
//		forumTopic.title=$("#title").val();
//	}
//	if($("#startDate1").val()){
//		forumTopic.startDate1=$("#startDate1").val();
//	}
//	if($("#startDate2").val()){
//		forumTopic.startDate2=$("#startDate2").val();
//	}
//	if($("#updateDate1").val()){
//		forumTopic.updateDate1=$("#updateDate1").val();
//	}
//	if($("#updateDate2").val()){
//		forumTopic.updateDate2=$("#updateDate2").val();
//	}
//	$("#list2").jqGrid('setGridParam',{postData:forumTopic}).trigger('reloadGrid'); 
	var postDataObj = $('#list2').jqGrid('getGridParam', 'postData');
	var postData = {};
	var dateArr = [];
	postData.status = postDataObj.status;
	postData.sortFields = postDataObj.sortFields;
	delete  postDataObj.fuzzyQueryFields;
	delete postDataObj.title;
	delete postDataObj.essence;
		$('.senior-box input').each(function (index, item) {
			if ($.trim(item.value) != '') {
				var name = item.name;
				if (name.indexOf('starttime') > -1 || name.indexOf('endtime') > -1) {
					dateArr.push(name);
				}
				postData[item.name] = item.value;
			}
			delete postDataObj[item.name];
		});
		if (dateArr.length > 0) {
			postData.dateFields = JSON.stringify(dateArr);
		}
		delete postDataObj.dateFields;
	$("#list2").jqGrid('setGridParam', {postData: postData,page:1}).trigger('reloadGrid');
}

/**
 * 递归树匹配节点icon
 */
function formatZTreeData(arr) {
	var zNodes = [];
	
	for (var i = 0; i < arr.length; i++) {
		var iconStyle='diy-group';
		if(arr[i].code == "") {
			iconStyle = "diy-group";
		}else {
			iconStyle = "diy-program";
	    } 
		zNodes.push({id:arr[i].id, pId:arr[i].pId, name:arr[i].name,dataType:arr[i].dataType,iconSkin:iconStyle});
	}
	return zNodes;
};

/**
 * 收藏
 */
function toAddFavorite(){
	var ids=$('#list2').jqGrid('getGridParam','selarrrow');
	if(!ids||ids.length==0) {
		pop_tip_open("blue","请选择要收藏的记录！");
		return;
	}
	ids = ids.join(",");
	if(ids&&ids!='') {
		$.ajax({
			url:hostUrl+"oa/bbs/topic/addFavorite/"+ids,
			type:'post',
			dataType:'json',
	        contentType:'application/json',
			success:function (resultData ) {
				if (resultData && resultData.success) {
					pop_tip_open("green","收藏成功！");
				}else{
					pop_tip_open("red","收藏失败！");
				}
			}
		});
	}
}

/**
 * 发帖
 */
function postTopic(){
	if(currentSelectNode){
		window.open("topic/topic_edit.html?oper=add&forumId="+currentSelectNode.id+"&forumName="+encodeURI(currentSelectNode.name, "UTF-8"));
	}else{
		window.open("topic/topic_edit.html?oper=add");
	}
}

/**
 * 首页发帖
 */
function postTopicHomePage(id,name){
	window.open("topic/topic_edit.html?oper=add&forumId="+id+"&forumName="+encodeURI(name, "UTF-8"));
}

/**
 * 跳转帖子详情
 * @param id
 */
function toTopicDetail(id){
	window.open("topic/topic_detail.html?id="+id);
}

/**
 * 查询登录用户信息
 */
function getLoginUserInfo(){
	$.ajax({
		type:'get',
		url:hostUrl+'/oa/bbs/topic/getUserInfo?time='+Math.random(),
		success: function(data) {
			if(data.success){
				var userBeanInfo=data.result;
				if(userBeanInfo && userBeanInfo.type=='2'){
					$("#delBtn").show();
				}
			}
		}
	});
}

/**
 * 删除
 */
function toDelete(){
    var ids = $('#list2').jqGrid('getGridParam','selarrrow');
    var delCount=ids.length;
    ids = ids.join(",");
	if(ids&&ids!='') {
		pop_text_open("blue", "确认要删除这"+delCount+"条数据吗？",function(){
            $.ajax({
                url:hostUrl+"oa/bbs/topic/deletePseudoBatch/"+ids,
                type:'DELETE',
                dataType:'JSON',
                contentType:'application/json',
                data:JSON.stringify({}),
                success:function (xhr,textStatus ) {
                    if (xhr){
                        if(xhr.success) {
                            $.xljUtils.tip("green","数据删除成功！");
                            $('#list2').jqGrid().trigger("reloadGrid");
                        }else{
                            if(xhr.code=="50000"){
                                $.xljUtils.tip("red",xhr.msg);
                                return;
                            }
                            $.xljUtils.tip("red","数据删除失败！");
                        }
                    }else{
                        $.xljUtils.tip("red","服务异常,请联系管理员！");
                    }
                },
                error: function(xhr, textStatus, errorThrown) {
                    console.log(xhr);
                    $.xljUtils.tip("red","服务异常,请联系管理员！");
                }
            });
        },true);
    }else{
        $.xljUtils.tip("blue","请选择要删除的数据！");
    }
}

/**
 * 刷新页面
 */
function reloadList(){
	jQuery("#list2").trigger("reloadGrid");
}
/**
 * 获取按钮权限
 */
function getOperationAuthorition() {
	var menuList;
	$.ajax({
		type: 'GET',
		url: hostUrl + 'sys/authentication/getUserAuthenticationOperation?t_='+new Date().getTime()+'&appCode=OA&menuCode=ltsy',
		dataType: 'json',
		//contentType: 'application/json',
		async: false,
		//data: JSON.stringify(postdata),
		success: function (data) {
			if (data.success) {
				menuList =  data.result;

			} else {
				$.xljUtils.tip('red', '获取按钮权限失败！');
			}
		},
		error: function (xhr, textStatus, errorThrown) {
			$.xljUtils.tip('red', '获取按钮权限失败！');
		}
	});
	return menuList;
}