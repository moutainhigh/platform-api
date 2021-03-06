var selectType="agent";// 选中类型 starter/ agent / approver
var instanceGrid;
//打开窗口参数定义
var specs = '';
var sysAppList, busiObjectList;
var appId="-1", busiObjectId="-1";
$(function() {
	 queryAppSystemList();
});

function resizeInstanceGridList(){
	$('#instanceGridList').jqGrid('setGridHeight',$(window).height()-$(".xj-form-header").height()-$(".flow-tab").height()-$(".mst").height()-$("#lanuchForm").height()-110, true);
}

function initQueryGridList(){
	instanceGrid = $("#instanceGridList");
	var paramData1 = {queryType : "ALLTYPE",appId:appId,busiObjectId:customFormId};
	var urlText1 = hostUrl+"flow/instance/personalQueryList"; // ;
	var colModel1 = [ {name:'id',  hidden:true}, 
	                  {name:'groupId',  hidden:true},  
	                  {name:'postId',  hidden:true}, 
	                  {name:'participantId',  hidden:true},
	                  {name:'currentApproverIds',  hidden:true},
	                  {name:'currentUserId',  hidden:true},
	                  {name:'startUserId',  hidden:true},
	                  
	                  {name:'businessId',  hidden:true},
	                  {name:'flCode',  hidden:true},
	                  {name:'name',  label:'流程标题',  width:"200", align:"left", formatter: instanceFormatter},
	                  {name:'currentApprovers',  label:'当前审批人', width:"100",  align:"center"},
	                  {name:'startUserName',  label:'发起人', width:"100",  align:"center" },
	                  {name:'startDate',  label:'发起时间', width:"175",   align:"center" },
	                  {name:'flowBusinessCompanyName',   label:'发起公司',  width:"100",  align:"center" },
	                  {name:'status',   label:'流程状态',   width:"75", align:"center" , formatter: statusformatter},
	                  {name:'hourSum',   label:'历时', width:"50",  align:"center" }];
	initSingleGrid(instanceGrid, paramData1, urlText1, colModel1);
}

function initSingleGrid(itemGrid, postParam, urlText, colModel){
	itemGrid.jqGrid( {// 创建jqGrid组件
        url : urlText, 
        postData : postParam,
        datatype : "json", 
        ajaxGridOptions: { contentType: 'application/json;charset=utf-8' },
        mtype : "post", 
        jsonReader: { repeatitems: false },
        colModel : colModel,    
        sortname : 'id',// 默认的排序列
//        autowidth: true,
        sortorder : "desc",// 排序方式,可选desc,asc
        viewrecords : true, // 定义是否要显示总记录数
        pager: '#instanceGridPager',// 定义翻页用的导航栏，必须是有效的html元素
        rowNum: 20,// 在grid上显示记录条数，这个参数是要被传递到后台
        rowList:  [20, 50, 100, 200], // 可供用户选择一页显示多少条
       	multiselect:true,// 定义是否可以多选
       	gridComplete: function() {// 当表格所有数据都加载完成
       		$('.ui-state-default.ui-jqgrid-hdiv').css({'overflow':'hidden','margin-top':'8px'});
			$('#instanceGridList').jqGrid('setGridWidth',$("#participateDiv").width()-3);
			resizeInstanceGridList();
	        $.xljUtils.addGridScroll();
	        $.xljUtils.gridResizeFn();
	    },
        loadError: function(xhr, status, error){
        	
        }
    });
}

// 格式化流程标题的显示
function instanceFormatter(cellvalue, options, rowObject) {
	var currentUserId = rowObject.currentUserId;
	var currentApproverIds = rowObject.currentApproverIds;
	var urlText = "";
	var typeText = "view";
	var instanceId = rowObject.id;
	var flCode = rowObject.flCode;
	var pcUrl = rowObject.pcUrl;
	var businessId = rowObject.businessId;
	var startUserId = rowObject.startUserId;
	if(currentApproverIds && currentApproverIds.indexOf(currentUserId)>=0 && "1"== rowObject.status){// 如果当前用户是当前审批用户
		typeText = "approve";
	}else if(startUserId == currentUserId){//
		var typeText = "start_view";
	}
	return "<a id=\""+options.rowId+"_a\" href=\"javaScript:void(0);\" style=\"font-weight: bold;color:#3c8dbc;\" " +
			" onclick=\"gotoInstanceView(\'"+typeText+"\',\'"+instanceId+"\', \'"+flCode+"\', \'"+pcUrl+"\', \'"+businessId+"\');\">"+cellvalue+"</a>";        
}

function gotoInstanceView(typeText, instanceId, flCode, pcUrl, businessId){
	var urlText = "";
	if(typeText == "approve"){// 需要获取对应的taskId
		var postdata = {instanceId: instanceId};
		$.ajax({ // 发送更新的ajax请求
	        type: "post",  dataType: "JSON",
	        url: hostUrl+"flow/instanceTask/queryTaskIdByInstanceId", // ?time="+
																		// Math.random(),
	        data: JSON.stringify(postdata),
	        contentType: 'application/json;charset=utf-8', // 设置请求头信息
	        success: function (returnData) {
	        	if(returnData.success){
	        		var result = returnData.result;
	        		var taskId = "";
	        		var taskType = "";
	        		var approveType = "";
	        		var msgId = "";
	        		if(result && result.length>0 ){
	        			var itemData = result[0];
	        			taskId = itemData.id;	
	        			taskType = itemData.type;
	        			approveType = itemData.approveType;
	        			msgId = itemData.msgId;
	        		}
	        		urlText = encodeURI(hostUrl+"flow/runtime/approve/flow.html?msgId="
	        				+ msgId + "&taskId=" + taskId + "&instanceId=" + instanceId
        		   			+ "&time=" + Math.random());
	        		openWin(urlText);
	        	}
	        },
	        error: function (data) {
	        	pop_tip_open('red', "查询当前用户任务ID失败!");
	        }
	    });// end-for $.ajax({
	}else if(typeText == "start_view"){//
		urlText = encodeURI( hostUrl+"/flow/runtime/approve/flow.html"
		+ "?instanceId=" + instanceId
		+ "&time=" + new Date().getTime() );
		openWin(urlText);
	}else{
		urlText = encodeURI(hostUrl+"flow/runtime/approve/flow.html?instanceId=" + instanceId
		+ "&time=" + Math.random());
		openWin(urlText);
	}
	
}

/**
 * 在新窗口全屏打开外外部链接
 * 
 * @param url  需要打开的URL
 */
function openWin(url) {
	if (url.indexOf('casUrlLogin') > -1) {
		//老平台cas单点不需要encodeURI链接
		window.open(url, '', specs);
	} else {
		window.open(encodeURI(url), '', specs);
	}
}

/*
 * function timeformatter(cellvalue, options, rowObject) { var status =
 * rowObject.status; var startDate = (new Date(rowObject.startDate)).getTime();
 * //得到毫秒数 var timeText = ""; if(status == "1" || status == "4" || status ==
 * "9"){ var serverTime = (new Date(rowObject.serverDate)).getTime(); //得到毫秒数
 * timeText = calculateTimeOffset(serverTime, startDate)+"小时"; }else { var
 * endDate; if(!rowObject.endDate){//没有结束时间 endDate = (new
 * Date(rowObject.serverDate)).getTime(); //得到毫秒数 }else{ endDate = (new
 * Date(rowObject.endDate)).getTime(); //得到毫秒数 } timeText =
 * calculateTimeOffset(endDate, startDate)+"小时"; }
 * ////console.info("status="+status+"; timeText="+timeText); return timeText; }
 */

function calculateTimeOffset(newTime, oldTime){
    var hourSum = (newTime-oldTime)/1000/60/60;
    var hourText = hourSum+"";
    var index = hourText.indexOf(".");
    return hourText.substr(0,index+2);
}

/**
 * 流程状态的格式化数据
 * 
 * @param cellvalue:
 *            该字段的值
 * @param options:
 * @param rowObject
 * @returns {String} 返回业务系统的名称
 */
function statusformatter(cellvalue, options, rowObject) {
    if(cellvalue == "1"){
    	return "运行中";
    }else if(cellvalue == "2"){
    	return "正常完成";
    }else if(cellvalue == "3"){
    	return "撤回";
    }else if(cellvalue == "4"){
    	return "打回";
    }else if(cellvalue == "7"){
    	return "作废";
    }else if(cellvalue == "9"){
    	return "挂起";
    }
}

function objectformatter(cellvalue, options, rowObject) {
    var systemName = "";
    $.each(busiObjectList, function (index, item) {// 遍历mapList的数组数据
        if (item.id == cellvalue) {
            systemName = item.name;
        }
    });// $.each(appList
    return systemName;
}

function emptyBusiObject(){
	$("#busiObjectId").val("");
	$("#busiObjectName").val("");
}

/**
 * 查询按钮的点击事件
 */
function doSearchAction(){
	var busiObjectId = customFormId;
	if(!busiObjectId || busiObjectId=="" || busiObjectId=="null"){
		busiObjectId = "-1";
	}
	var paramData = { appId:appId, busiObjectId:busiObjectId, name:$("#name").val() };
	paramData.timeType = $("#timeType").val();
	paramData.queryType = $("#queryType").val();
	instanceGrid.jqGrid('setGridParam', { datatype: 'json', postData: paramData,page:1 }).trigger("reloadGrid");
}

function queryTypeChange(){
	$("#setHaveDoneBtn").hide();
	$("#deleteBtn").hide();
	var queryType = $("#queryType").val();
	if("TO_DO" == queryType || "TO_READ" == queryType){
		$("#setHaveDoneBtn").show();
	}
	if("MY_START" == queryType){
		$("#deleteBtn").show();
	}
}

function setHaveDone(){// 设置为已办的处理方法
	var queryType = $("#queryType").val();
	if("TO_DO" == queryType || "TO_READ" == queryType){
		var selectedIds = instanceGrid.jqGrid('getGridParam','selarrrow');
		// 调用设置已办的接口
		var postdata = {
			queryType: queryType,
			selectedIds: selectedIds.join(",")
		}
	    $.ajax({ // 发送更新的ajax请求
	        type: "post",  dataType: "json",  async: false,
	        url: hostUrl+"flow/instanceOperateLog/batchSetOperateLogHaveDone", // ?time="+
																				// Math.random(),
	        data: JSON.stringify(postdata),
	        contentType: 'application/json;charset=utf-8', // 设置请求头信息
	        success: function (data) {
	        	doSearchAction();
	        },
	        error: function (data) {
	            if (data.msg) {
	                pop_tip_open('red', data.msg);
	            } else {
	                pop_tip_open('red', "置为已办操作失败");
	            }
	        }
	    });// end-for $.ajax({
		
	}else{
		pop_tip_open('blue', "查询分类为'待办'或'待阅'才能进行'置为已办'的操作!");
	}
	
}

function deleteOperateLog(){
	var queryType = $("#queryType").val();
	if("MY_START" == queryType){
		var selectedIds = instanceGrid.jqGrid('getGridParam','selarrrow');
		// 调用设置已办的接口
		var postdata = {
			queryType: queryType,
			selectedIds: selectedIds.join(",")
		}
	    $.ajax({ // 发送更新的ajax请求
	        type: "post",  dataType: "json",  async: false,
	        url: hostUrl+"flow/instanceOperateLog/batchDeleteOperateLog", // ?time="+
																			// Math.random(),
	        data: JSON.stringify(postdata),
	        contentType: 'application/json;charset=utf-8', // 设置请求头信息
	        success: function (data) {
	        	doSearchAction();
	        },
	        error: function (data) {
	            if (data.msg) {
	                pop_tip_open('red', data.msg);
	            } else {
	                pop_tip_open('red', "删除操作失败");
	            }
	        }
	    });// end-for $.ajax({
		
	}else{
		pop_tip_open('blue', "查询分类为'我的发起'才能进行'删除'的操作!");
	}
}

/**
 * 查询所有业务系统的静态数据
 */
function queryAppSystemList() {
    var postdata = {
        appDelFlag: "0",
        appStatus: "1",
        code:'OA'
    }
    $.ajax({ // 发送更新的ajax请求
        type: "post",
        url: hostUrl+"sys/res/appSystem/queryList", // ?time="+ Math.random(),
        dataType: "json",
        async: false,
        data: JSON.stringify(postdata),
        contentType: 'application/json;charset=utf-8', // 设置请求头信息
        success: function (data) {
        	sysAppList = data.result;
        	if(sysAppList && sysAppList.length>0){
        		appId=sysAppList[0].id;
        	}
        },
        error: function (data) {
            if (data.msg) {
                pop_tip_open('red', data.msg);
            } else {
                pop_tip_open('red', "查询业务系统的列表数据失败！");
            }
        }
    });// end-for $.ajax({
}
