// /**
//  * @Title：搜索框提示插件||输入框提示插件
//  * @Version：1.0
//  * @Auth：wj
//  * @Date: 2020/07/01
//  */
// layui.define(['jquery', 'table'], function (exports) {
//     "use strict";
//     var $ = layui.jquery,
//         table = layui.table;
//
//     var inputTips = function () {
//         this.v = '1.0.1';
//     };
//
//     /**
//      * inputTips搜索框提示插件||输入框提示插件初始化
//      */
//     inputTips.prototype.render = function (opt) {
//         opt.urlBak = opt.url;
//         //设置默认初始化参数
//         opt.type = opt.type || 'tips'; //默认tips，传入tips||tipsTable
//         opt.elem = '#inputTipsTable_' + opt.id;
//         opt.height = opt.height || '229';
//         opt.cellMinWidth = opt.cellMinWidth || '80'; //最小列宽
//         opt.page = opt.page || true;
//         opt.limits = opt.limits || [3];
//         opt.loading = opt.loading || true;
//         opt.limit = opt.limit || 100;
//         opt.size = opt.size || 'sm'; //小尺寸的表格
//
//         //输入框节点
//         var inputObj = $("#" + opt.id);
//         //初始化输入框提示容器
//         inputObj.after('<div id="tipsItem" style="background-color: #fff;display: none;z-index:1;position: absolute;width:100%;overflow:auto;max-height:' + opt.height + 'px;"></div>');
//
//         //输入框提示容器移出事件：鼠标移出隐藏输入提示框
//         inputObj.parent().mouseleave(function () {
//             inputObj.next().hide().html("");
//         });
//
//         if (opt.type === "tipsTable") {
//             //如果type为sugTable，则初始化下拉表格
//             /* 输入框鼠标松开事件 */
//             inputObj.mouseup(function () {
//                 opt.obj = this;
//                 getTipsTable(opt);
//             });
//             /* 输入框键盘抬起事件 */
//             inputObj.keyup(function () {
//                 opt.obj = this;
//                 getTipsTable(opt);
//             });
//         } else if (opt.type === "tips") {
//             //如果type为tips，则初始化下拉框
//             inputObj.next().css("border", "solid #e6e6e6 0.5px");
//             /* 输入框鼠标松开事件 */
//             inputObj.mouseup(function () {
//                 opt.obj = this;
//                 getTips(opt);
//             });
//             /* 输入框键盘抬起事件 */
//             inputObj.keyup(function () {
//                 opt.obj = this;
//                 getTips(opt);
//             });
//         }
//     };
//
//     //搜索框提示插件||输入框提示插件--sugTable-下拉表格
//     function getTipsTable(opt) {
//         //如果输入信息为"",则隐藏输入提示框,不再执行下边代码
//         var keyword = $.trim($(opt.obj).val());
//         //下拉表格初始化table容器
//         var html = '<table id=inputTipsTable_"' + opt.id + '" lay-filter=inputTipsTable_"' + opt.id + '"></table>';
//         $("#" + opt.obj.getAttribute("id")).next().show().html(html);
//         //下拉表格初始化
//         opt.url = opt.urlBak + keyword;
//         table.render(opt);
//         //设置下拉表格样式
//         $(opt.elem).next().css("margin-top", "0").css("background-color", "#ffffff");
//         //监听下拉表格行单击事件（单击||双击事件为：row||rowDouble）设置单击或双击选中对应的行
//         table.on('rowDouble(' + "inputTipsTable_" + opt.id + ')', function (obj) {
//             for (var param in opt.params) {
//                 //$("#" + param.name).val(obj.data[param.field])
//                 //此处修改---由原来的根据id设置value值变更为根据name设置value值
//                 $("*[name='"+param.name+"']").val(obj.data[param.field])
//             }
//             $("#" + opt.id).next().hide().html("");
//         });
//     }
//
//     //搜索框提示插件||输入框提示插件--tips-下拉框
//     function getTips(opt) {
//         var keyword = $.trim($(opt.obj).val());
//         //tips下拉框异步加载数据并渲染下拉框
//         $.ajax({
//             type: "get",
//             url: opt.urlBak + keyword,
//             success: function (data) {
//                 var html = "";
//                 layui.each(data.data, function(index, item) {
//                     if (index >= opt.limit) return;
//                     html += '<div class="item" style="padding: 3px 10px;cursor: pointer;" onmouseenter="getFocus(this)" onclick="getCon(this)">'+item+'</div>';
//                 });
//                 if (html !== "") {
//                     $(opt.obj).next().show().html(html);
//                 } else {
//                     $(opt.obj).next().hide().html("");
//                 }
//             }
//         });
//     }
//     //上下键选择和鼠标选择事件改变颜色
//     window.getFocus = function (obj) {
//         $(".item").css("background", "");
//         $(obj).css("background", "#e6e6e6");
//     };
//     //点击选中事件，获取选中内容并回显到输入框
//     window.getCon = function (obj) {
//         var value = $(obj).text();
//         $("#" + $(".item").parent().prev().attr("id")).val(value);
//         $("#" + $(".item").parent().prev().attr("id")).next().hide().html("");
//     };
//
//     //自动完成渲染
//     inputTips = new inputTips();
//     //暴露方法
//     exports("inputTips", inputTips);
// });





/**
 * @Title：yutons_sug搜索框提示插件||输入框提示插件
 * @Version：1.0.1
 * @Auth：yutons
 * @Date: 2019/04/08
 * @Time: 10:00
 */
layui.define(['jquery', 'table', 'form'], function (exports) {
    "use strict";
    var $ = layui.jquery,
        table = layui.table,
        form = layui.form;

    var yutons_sug = function () {
        this.v = '1.0.1';
    };
    /**
     * yutons_sug搜索框提示插件||输入框提示插件初始化
     */
    yutons_sug.prototype.render = function (opt) {
        opt.urlBak = opt.url;
        opt.where = opt.where || {}; //条件
        opt.search = opt.search || 'search'; //查询关键词
        if(opt.where[opt.search]) {
            opt.where[opt.search] = "";
        }
        //设置默认初始化参数
        opt.type = opt.type || 'sug'; //默认sug，传入sug||sugTable
        opt.elem = "#yutons_sug_" + opt.id;
        opt.height = opt.height || '300';
        opt.cellMinWidth = opt.cellMinWidth || '80'; //最小列宽
        opt.page = opt.page || true;
        opt.limits = opt.limits || [100];
        opt.loading = opt.loading || true;
        opt.limit = opt.limit || 100;
        opt.size = opt.size || 'sm'; //小尺寸的表格

        //初始化输入框提示容器
        $("#" + opt.id).parent().addClass('layui-form-select');
        $("#" + opt.id).after('<i class="layui-edge"></i>');
        $("#" + opt.id).after('<dl id="sugItem" class="layui-anim layui-anim-upbit"></dl>');

        $("#" + opt.id).siblings().last().click(function () {
            if($("#" + opt.id).parent().hasClass('layui-form-selected')) {
                $("#" + opt.id).parent().removeClass("layui-form-selected");
                $("#" + opt.id).next().hide().html("");
            } else {
                opt.obj = this;
                if (opt.type == "sugTable") {
                    getSugTable(opt);
                } else {
                    getSug(opt);
                }
            }
        });

        //输入框失去焦点事件:隐藏输入提示框
        if (opt.type == "sug") {
            $("#" + opt.id).blur(function () {
                $("#" + opt.id).parent().removeClass("layui-form-selected");
                $("#" + opt.id).next().hide().html("");
            });
        }

        $("#" + opt.id).click(function () {
            if($("#" + opt.id).parent().hasClass('layui-form-selected')) {
                $("#" + opt.id).parent().removeClass("layui-form-selected");
                $("#" + opt.id).next().hide().html("");
            } else {
                opt.obj = this;
                if (opt.type == "sugTable") {
                    getSugTable(opt);
                } else {
                    getSug(opt);
                }
            }
        });
        /* 输入框键盘抬起事件 */
        $("#" + opt.id).keyup(function (e) {
            opt.obj = this;
            if (opt.type == "sugTable") {
                getSugTable(opt);
            } else {
                getSug(opt);
            }
        })
    }

    //搜索框提示插件||输入框提示插件--sugTable-下拉表格
    function getSugTable(opt) {
        //如果输入信息为"",则隐藏输入提示框,不再执行下边代码
        var keyword = $.trim($(opt.obj).val());
        //下拉表格初始化table容器
        var html = '<table id="yutons_sug_' + opt.obj.getAttribute("id") + '" lay-filter="yutons_sug_' + opt.obj.getAttribute(
            "id") +
            '"></table>';
        $("#" + opt.obj.getAttribute("id")).next().show().html(html);

        //下拉表格初始化
        opt.url = opt.urlBak + keyword;
        table.render(opt);
        //设置下拉表格样式
        $(opt.elem).next().css("margin-top", "0").css("background-color", "#ffffff");
        //监听下拉表格行单击事件（单击||双击事件为：row||rowDouble）设置单击或双击选中对应的行
        table.on('row(' + "yutons_sug_" + opt.id + ')', function (obj) {
            layui.each(opt.params, function(index,param) {
                //$("#" + param.name).val(obj.data[param.field])
                //此处修改---由原来的根据id设置value值变更为根据name设置value值
                console.log(param.field  +  "  " + param.name);
                $("*[name='"+param.name+"']").val(obj.data[param.field])
            });
            $("#" + opt.id).next().hide().html("");
        });
    }

    //搜索框提示插件||输入框提示插件--sug-下拉框
    function getSug(opt) {
        sessionStorage.setItem("inputId", opt.id)
        if (opt.idField != undefined && opt.idField != null) {
            sessionStorage.setItem("idField", opt.idField);
        }
        var keyword = $.trim($(opt.obj).val());
        opt.where[opt.search] = keyword;
        //sug下拉框异步加载数据并渲染下拉框
        $.ajax({
            type: "get",
            url: opt.urlBak,
            data: opt.where,
            success: function (data) {
                var html = "";
                layui.each(data.data, function(index, item) {
                    if (index >= opt.limit) {
                        return;
                    }
                    html += '<dd class="item" onmouseenter="getFocus(this)" onmousedown="getCon(this);">'+item+'</dd>';
                });
                if(html === "") {
                    html += '<dd class="item">--无数据--</dd>';
                }
                $("#" + opt.id).parent().addClass("layui-form-selected");
                $("#" + sessionStorage.getItem("inputId")).next().show().html(html);
            }
        });
    }

    //搜索框提示插件||输入框提示插件--sug-下拉框上下键移动事件和回车事件
    $(document).keydown(function (e) {
        e = e || window.event;
        var keycode = e.which ? e.which : e.keyCode;
        if (keycode == 38) {
            //上键事件
            if ($.trim($("#" + sessionStorage.getItem("inputId")).next().html()) == "") {
                return;
            }
            movePrev(sessionStorage.getItem("inputId"));
        } else if (keycode == 40) {
            //下键事件
            if ($.trim($("#" + sessionStorage.getItem("inputId")).next().html()) == "") {
                return;
            }
            $("#" + sessionStorage.getItem("inputId")).blur();
            if ($(".item").hasClass("addbg")) {
                moveNext();
            } else {
                $(".item").removeClass('addbg').css("background", "").eq(0).addClass('addbg').css("background", "#e6e6e6");
            }
        } else if (keycode == 13) {
            //回车事件


            dojob();
        }
    });
    //上键事件
    var movePrev = function (id) {
        $("#" + id).blur();
        var index = $(".addbg").prevAll().length;
        if (index == 0) {
            $(".item").removeClass('addbg').css("background", "").eq($(".item").length - 1).addClass('addbg').css(
                "background", "#e6e6e6");
        } else {
            $(".item").removeClass('addbg').css("background", "").eq(index - 1).addClass('addbg').css("background", "#e6e6e6");
        }
    }
    //下键事件
    var moveNext = function () {
        var index = $(".addbg").prevAll().length;
        if (index == $(".item").length) {
            $(".item").removeClass('addbg').css("background", "").eq(0).addClass('addbg').css("background", "#e6e6e6");
        } else {
            $(".item").removeClass('addbg').css("background", "").eq(index + 1).addClass('addbg').css("background", "#e6e6e6");
        }
    }
    //回车事件
    var dojob = function () {

        //如果未定义idField，则不添加idField字段
        var idField = sessionStorage.getItem("idField");
        if (idField != undefined && idField != null) {
            $("#" + sessionStorage.getItem("idField")).val($(".addbg").attr("name"))
        }

        var value = $(".addbg").text();
        $("#" + sessionStorage.getItem("inputId")).blur();
        $("#" + sessionStorage.getItem("inputId")).val(value);
        $("#" + sessionStorage.getItem("inputId")).next().hide().html("");
    }

    //上下键选择和鼠标选择事件改变颜色
    window.getFocus = function (obj) {
        $(".item").css("background", "");
        $(obj).css("background", "#e6e6e6");
    }

    //点击选中事件，获取选中内容并回显到输入框
    window.getCon = function (obj) {
        var value = $(obj).text();
        //如果未定义idField，则不添加idField字段
        var idField = sessionStorage.getItem("idField");
        if (idField != undefined && idField != null) {
            $("#" + sessionStorage.getItem("idField")).val($(".item").attr("name"))
        }
        $("#" + $(".item").parent().prev().attr("id")).val(value);
    }

    //自动完成渲染
    yutons_sug = new yutons_sug();
    //暴露方法
    exports("yutons_sug", yutons_sug);
});

