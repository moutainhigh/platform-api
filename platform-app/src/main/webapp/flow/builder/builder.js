require.config({
    paths: {
        jquery: [ 'lib/jquery-1.12.4.min'],
        bootstrap: [hostUrl+'common/bootstrap/js.bootstrap.min.js'],
        jqueryValidate:[hostUrl+'common/validate/jquery.validate.min'],
        dotdotdot:[hostUrl+'common/jquery/jquery.dotdotdot.min'],
        easyDialog:[hostUrl+'common/easyDialog/easydialog'],
        xljUtils:[hostUrl+'common/utils/xljUtils'],
        lodash: ['lib/lodash.min'],
        utils: ['modules/Utils'],
        actions: ['modules/Actions'],
        sidebar: ['modules/Sidebar'],
        toolbar: ['modules/Toolbar'],
        stage: ['modules/Stage'],
        flTemplate: ['fl_template'],
        builder: ['lib/builder.js']
    },
    shim:{
        'jqueryValidate': {
            deps: ['jquery'],
            exports: 'jqueryValidate'
        },
        'dotdotdot':{
            deps: ['jquery'],
            exports: 'dotdotdot'
        },
        'easyDialog':{
            deps: ['dotdotdot'],
            exports: 'easyDialog'
        },
        'xljUtils':{
            deps: ['jqueryValidate','easyDialog'],
            exports: 'xljUtils'
        }
    }
});

require(['jquery', 'lodash', 'stage', 'utils','flTemplate'], function ($, _, stage, utils,flTemplate) {
    console.info('builder is running ...');
    var initial = function (container) {
        if (!mxClient.isBrowserSupported()) {
            mxUtils.error('该浏览器不支持!', 200, false);
        } else {
            stage.renderMainStage(container,true,true);
        }
    };

    /**
     * 监听load事件，绘制工具条，容器等
     * */
    document.addEventListener('load', initial(document.getElementById('_designer')));

    //重置窗口大小
    var resizeStageContainer = function (container) {
        var winHeight = $(window).height();
        $(container).height(winHeight - 62);

        $(window).on('resize', function () {
            var winHeight = $(window).height();
            $(container).height(winHeight - 62);
        });
    };
    //初始化容器大小
    resizeStageContainer($('#_designer'));

    //初始化按钮动作
    flTemplate.initOperation();
    //初始化模板相关数据
    var flDataDef = flTemplate.initFlDatas();
    //渲染已存在的画图
    $.when(flDataDef).done(function(flBaseData){
        var flData = flBaseData.flData;
        //获取url参数
        var urlParams = $.xljUtils.getUrlParams();
        if(flData.graphXml){
            var graph = utils.getGraphEditor();
            //从xml中渲染画布
            utils.renderModelFromXml(graph,flData.graphXml);
            //utils.renderModelFromXml(graph,flData.graphXml);

            //操作类型
            var act = urlParams.act;
            if(act=='view'){
                graph.setEnabled(false);
            }
        }

        setTimeout(function () {
            var acId = urlParams.acId;
            if(acId){
                var oldNewAcIdMapJson = flData.oldNewAcIdMapJson;
                if(oldNewAcIdMapJson&&oldNewAcIdMapJson!=''){
                    var oldNewAcIdMap = JSON.parse(oldNewAcIdMapJson);
                    var selectionCell = getCellById(oldNewAcIdMap[acId]);
                    graph.setSelectionCell(selectionCell);
                }

            }
            var h = $("#_designer").find('svg').height();
            if(h){
                $("#_designer").find('svg').height(h+1000);
            }
        },300);
    });


    //初始化变量列表
    var variableListDef = flTemplate.initVariableListByBusinessObjectId();

    //暴露给iframe子页面调用
    window.getFlDataDef = function () {
      return flDataDef;
    };

    window.setFlDataDef = function (flBaseDataDef) {
        flDataDef = flBaseDataDef;
    };

    //暴露给iframe子页面调用
    window.flDataDefResolveData = function (data) {
        flDataDef = new $.Deferred();
        flDataDef.resolve(data);
        flDataDef.promise();
        utils.closeFrameWnd();
    };

    //暴露给iframe子页面调用
    window.getVariableListDef = function () {
        return variableListDef;
    };

    //暴露给iframe子页面调用
    window.getUUID = utils.getUUID;

    //暴露给iframe子页面调用
    window.getSelectCell = function () {
        var graph = utils.getGraphEditor();
        return graph.getSelectionCell();
    };

    //获取流程模板相关数据
    window.getFlDataForSave = flTemplate.getFlDataForSave;
    //验证流程图正确性
    window.validateFlowChart = flTemplate.validateFlowChart;

    //更新节点属性
    window.updateCellAttr = function (cellId,attrName,attrValue) {
        var model = utils.getGraphEditor().model;
        var cell = model.getCell(cellId);
        model.beginUpdate();
        try {
            var edit = new mxCellAttributeChange(cell, attrName, attrValue);
            model.execute(edit);
        }
        finally {
            model.endUpdate();
        }
    };

    //根据cell id获取单元格
    window.getCellById = function (cellId) {
        var model = utils.getGraphEditor().model;
        var cell = model.getCell(cellId);
        return cell;
    };

    //关闭弹出框
    window.closeFrameWnd = function () {
        utils.closeFrameWnd();
    };

    //引用流程
    window.referenceOtherFl = flTemplate.referenceOtherFl;

    //获取连线中的条件表达式使用的业务表单字段
    window.getConditionFields = flTemplate.getConditionFields;

    window.getGraphEditor = utils.getGraphEditor;

    //窗口关闭时提醒
    window.onbeforeunload = function (event) {
        event.returnValue = '请确保您修改的模板数据已经保存！';

    };
});
