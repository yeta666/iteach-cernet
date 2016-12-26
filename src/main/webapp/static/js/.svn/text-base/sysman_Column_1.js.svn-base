/**
 * @author
 */
var colData = new Object();
var firstColData = new Array();
var secondColData = new Array();
$(document).ready(
		function() {
			backToTop();// back To Top
			ShowColumn();// 列表显示
			showContent();// 加载栏目
			// 添加页面
			var addHtml = "<p><a class='btn btn-info disabled'>栏目名称：</a> "
					+ "<input id='firstColName' type='text' placeholder='请输入栏目名称' name='firstColName'></p>"
					+ "<p><a class='btn btn-info disabled'>栏目URL：</a> "
					+ "<input id='firstColUrl' type='text' placeholder='请输入栏目url' name='firstColUrl'></p>"
					+ "<p><a class='btn btn-info disabled'>栏目图标：</a> "
					+ "<input id='firstColIcon' type='text' placeholder='请输入栏目图标' name='firstColIcon'></p>"
					+ "<p><a class='btn btn-info disabled'>栏目顺序：</a> "
					+ "<input id='firstColOrder' type='text' placeholder='请输入栏目顺序' name='firstColOrder'></p>";
			$("#addArea").html(addHtml);
			// 修改页面
			var modHtml = "<p><a class='btn btn-info disabled'>栏目名称：</a> "
					+ "<input	id='firstColName' type='text' placeholder='请输入栏目名称' name='firstColName'></p>"
					+ "<p><a class='btn btn-info disabled'>栏目顺序：</a> "
					+ "<input id='firstColOrder' type='text' placeholder='请输入栏目顺序'name='firstColOrder'></p>";
			$("#modArea").html(modHtml);
			$("#addType").val("1");

			// 邦定添加新的一级栏目按钮
			bindAddFunctionBtn();
			bindAddAction();
			// 邦定编辑保存的栏目按钮
			bindModFunctionBtn();
			// 邦定delete
			bindDelColumn();
		});
// 加载数据
function showContent() {
	var url = "../../handler/authority/queryAllCompetence";
	$.ajaxSettings.async = false;
	$("#mainTable").empty();
	var startIndex = 1;
	$.get(url, function(data, status) {
		$.each(data.data.json, function(itemIndex1, item1) {
			colData = data.data.json;
			var columnHtml = "";
			var isFirst = true;
			firstColData.push(item1);
			columnHtml += "<tr><th rowspan='" + item1.subcols.length + "' nowrap><input type='checkbox' id='" + item1.colid
					+ "' value='" + item1.colid + "' name='1Record" + startIndex
					+ "' coltype='1'><a href='#modModal' data-toggle='modal'  id='fir_" + startIndex + "' name='" + item1.colname
					+ "' title='" + item1.colurl + "' icon='" + item1.colicon + "' onclick='changeModFun("
					+ item1.colid + ",0,1,this);'>" + item1.colname + "</a></th>";
			var secondOrder = 1;
			$.each(item1.subcols, function(itemIndex2, item2) {
				if (!isFirst)
					columnHtml += "<tr>";
				isFirst = false;
				columnHtml += "<td nowrap><input type='checkbox' id='" + item2.colid + "' value='" + item1.colid
						+ "' name='2Record" + startIndex + "' coltype='2'><a href='#modModal' data-toggle='modal'  id='sec_" + secondOrder + "' name='"
						+ item2.colname + "' title='" + item2.colurl + "' onclick='changeModFun(" + item2.colid + ","
						+ item1.colid + ",2,this);'>" + item2.colname + "</a></td><td>";
				secondOrder++;
				if (item2.subcols != null) {
					$.each(item2.subcols, function(itemIndex3, item3) {
						var parentIds = item1.colid.toString() + ";" + item2.colid.toString();
						columnHtml += "<label class='checkbox-inline'> <input type='checkbox' id='" + item3.colid
								+ "' name='3Record" + startIndex + "' value='" + item2.colid + "' firstCol='"
								+ item1.colid + "' secondCol='" + item2.colid + "'  coltype='3' ><a href='#modModal' data-toggle='modal' title='" + item3.colurl + "' name='"
								+ item3.colname + "' onclick='changeModFun(\"" + item3.colid + "\",\""
								+ parentIds.toString() + "\",3,this);'>" + item3.colname + "</a></label>";
					});
				}
				columnHtml += "</td></tr>";
			});
			startIndex++;
			$("#mainTable").append(columnHtml);
		});
	});
	return false;
}

function validateRoleInfo() {
	var isValid = true;
	// 验证栏目名称是否为空
	if ($('#rolename').val() == '') {
		infoNotice("error", "", "栏目不能为空");
		isValid = false;
	}
	if (getAuthority() == null || getAuthority() == "") {
		infoNotice("error", "", "请选择栏目");
		isValid = false;
	}
	return isValid;
}

function getParams() {
	var params = new Object();
	params.rolename = $('#rolename').val();
	params.roledesc = $('#roledesc').val();
	// 获取选择的栏目权限信息
	params.authority = getAuthority();
	return $.param(params, true);
}

function getAuthority() {
	var params = new Array();
	// 遍历功能
	$('#mainTable tr').each(function() {
		var actionId = new Array();
		var cli = {
			"firstCol" : 0,
			"secondCol" : 0,
			"actionId" : actionId
		};
		var isCheck = false;
		// 遍历该模块下所有操作的多选框
		$(this).find('input[coltype="action"]').each(function() {
			cli.firstCol = $(this).attr("firstCol");
			cli.secondCol = $(this).attr("secondCol");
			if ($(this).attr('checked') != undefined) {
				actionId.push($(this).attr('id').split("+")[1]);
				cli.actionId = actionId;
				isCheck = true;
			}
		});
		if (isCheck)
			params.push($.param(cli, true));
	});
	return params;
}

function chooseall(startIndex) {
	var name1 = "1Record" + startIndex;
	var name2 = "2Record" + startIndex;
	// 1 class
	var isAllChecked1 = true;
	for ( var i = 0; i < $('input[name="' + name2 + '"]').length; i++) {
		if (!$($('input[name="' + name2 + '"]')[i]).attr("checked")) {
			isAllChecked1 = false;
			break;
		}
	}
	if (isAllChecked1)
		$('input[name="' + name1 + '"]').attr("checked", "true");
	else
		$('input[name="' + name1 + '"]').removeAttr("checked");
}
function chooseall1(startIndex) {
	var name1 = "1Record" + startIndex;
	var name2 = "2Record" + startIndex;
	var name3 = "3Record" + startIndex;
	// 绑定全选/取消全选按钮
	$('input[name="' + name2 + '"]').each(function() {
		if ($('input[name="' + name1 + '"]').attr("checked")) {
			$(this).attr("checked", "true");
		} else
			$(this).removeAttr("checked");
	});
	// 绑定全选/取消全选按钮
	$('input[name="' + name3 + '"]').each(function() {
		if ($('input[name="' + name1 + '"]').attr("checked")) {
			$(this).attr("checked", "true");
		} else
			$(this).removeAttr("checked");
	});
	// chooseall(startIndex);
}
function chooseall2(startIndex, obj) {
	if ($('input[id="' + obj.id + '"]').attr("checked")) {
		$('input[value="' + obj.id + '"]').each(function() {
			$(this).attr("checked", "true");
		});
	} else {
		$('input[value="' + obj.id + '"]').each(function() {
			$(this).removeAttr("checked");
		});
	}
	chooseall(startIndex);
}
function chooseall3(startIndex, parentId) {
	var isAllChecked = true;
	for ( var int = 0; int < $('input[value="' + parentId + '"]').length; int++) {
		if (!$($('input[value="' + parentId + '"]')[int]).attr("checked")) {
			isAllChecked = false;
			break;
		}
	}
	if (!isAllChecked)
		$('input[id="' + parentId + '"]').removeAttr("checked");
	chooseall(startIndex);
}
function changeFun() {
	$("#addArea").empty();
	$("#modArea").empty();
	var type = $("#addType").val();
	if (type == 1) {
		addHtml = "<p><a class='btn btn-info disabled'>栏目名称：</a> "
				+ "<input id='firstColName' type='text' placeholder='请输入栏目名称' name='firstColName'></p>"
				+ "<p><a class='btn btn-info disabled'>栏目URL：</a> "
				+ "<input id='firstColUrl' type='text' placeholder='请输入栏目url' name='firstColUrl'></p>"
				+ "<p><a class='btn btn-info disabled'>栏目图标：</a> "
				+ "<input id='firstColIcon' type='text' placeholder='请输入栏目图标' name='firstColIcon'></p>"
				+ "<p><a class='btn btn-info disabled'>栏目顺序：</a> "
				+ "<input id='firstColOrder' type='text' placeholder='请输入栏目顺序'name='firstColOrder'></p>";
		$("#addArea").html(addHtml);
	} else if (type == 2) {
		addHtml = "<p><a class='btn btn-info disabled'>一级栏目：</a><select id='firstColId'>"
				+ "<option value='0'>一级栏目选择</option></select></p><p><a class='btn btn-info disabled'>栏目名称：</a> "
				+ "<input id='secondColName' type='text' placeholder='请输入二级栏目名称' name='secondColName'></p>"
				+ "<p><a class='btn btn-info disabled'>栏目URL：</a> "
				+ "<input id='secondColUrl' type='text' placeholder='请输入二级栏目url' name='secondColUrl'></p>"
				+ "<p><a class='btn btn-info disabled'>栏目顺序：</a> "
				+ "<input id='secondColOrder' type='text' placeholder='请输入二级栏目顺序'name='secondColOrder'></p>";
		$("#addArea").html(addHtml);
		findColu();
	} else if (type == 3) {
		addHtml = "<p><a class='btn btn-info disabled'>一级栏目：</a><select id='firstColId' onchange='findsecColu()'>"
				+ "<option value='0'>一级栏目选择</option></select></p><p><a class='btn btn-info disabled'>二级栏目：</a>"
				+ "<select id='secondColId'><option value='0'>二级栏目选择</option></select></p>"
				+ "<p><a class='btn btn-info disabled'>操作名称：</a> "
				+ "<input id='actionName' type='text' placeholder='请输入操作名称' name='actionName'></p>"
				+ "<p><a class='btn btn-info disabled'>操作URL：</a> "
				+ "<input id='actionUrl' class='span4' type='text' placeholder='请输入操作URL' name='actionUrl'></p>";
		$("#addArea").html(addHtml);
		findColu();
		findsecColu();
	}
}
function changeModFun(selColId, parColId, type, obj) {
	$("#addAction").remove();
	$("#addArea").empty();
	$("#modArea").empty();
	var name = obj.name;
	var url = obj.title;
	var order = "";
	var colicon = $("a[name='" + name + "']").attr("icon");
	if (type == 1) {
		order = parseInt(obj.id.replace("fir_", ""));
		modHtml = "<p><a class='btn btn-info disabled'>栏目名称：</a> "
				+ "<input id='modType' type='hidden' name='modType' value='" + type + "'/>"
				+ "<input id='firstColId' type='hidden' name='firstColId' value='" + selColId + "'/>"
				+ "<input id='firstColName' type='text' placeholder='请输入栏目名称' name='firstColName'></p>"
				+ "<p><a class='btn btn-info disabled'>栏目URL：</a> "
				+ "<input id='firstColUrl' type='text' placeholder='请输入栏目url' name='firstColUrl'></p>"
				+ "<p><a class='btn btn-info disabled'>栏目图标：</a> "
				+ "<input id='firstColIcon' type='text' placeholder='请输入栏目图标' name='firstColIcon'></p>"
				+ "<p><a class='btn btn-info disabled'>栏目顺序：</a> "
				+ "<input id='firstColOrder' type='text' placeholder='请输入栏目顺序'name='firstColOrder'></p>";
		$("#modArea").html(modHtml);
		$("#firstColName").val(name);
		$("#firstColUrl").val(url);
		$("#firstColIcon").val(colicon);
		$("#firstColOrder").val(order);
	} else if (type == 2) {
		order = parseInt(obj.id.replace("sec_", ""));
		modHtml = "<p><a class='btn btn-info disabled'>一级栏目：</a><select id='firstColId'>"
				+ "<option value='0'>一级栏目选择</option></select></p><p><a class='btn btn-info disabled'>栏目名称：</a> "
				+ "<input id='modType' type='hidden' name='modType' value='" + type + "'/>"
				+ "<input id='secondColId' type='hidden' name='secondColId' value='" + selColId + "'/>"
				+ "<input id='secondColName' type='text' placeholder='请输入二级栏目名称' name='secondColName'></p>"
				+ "<p><a class='btn btn-info disabled'>栏目URL：</a> "
				+ "<input id='secondColUrl' type='text' placeholder='请输入二级栏目url' name='secondColUrl'></p>"
				+ "<p><a class='btn btn-info disabled'>栏目顺序：</a> "
				+ "<input id='secondColOrder' type='text' placeholder='请输入二级栏目顺序'name='secondColOrder'></p>";
		$("#modArea").html(modHtml);
		$("#secondColName").val(name);
		$("#secondColUrl").val(url);
		$("#secondColOrder").val(order);
		findColu();
		$('option[value="' + parColId + '"]').attr("selected", "selected");
	} else if (type == 3) {
		modHtml = "<p><a class='btn btn-info disabled'>一级栏目：</a><select id='firstColId' onchange='findsecColu()'>"
				+ "<option value='0'>一级栏目选择</option></select></p><p><a class='btn btn-info disabled'>二级栏目：</a>"
				+ "<select id='secondColId'><option value='0'>二级栏目选择</option></select></p>"
				+ "<p><a class='btn btn-info disabled'>操作名称：</a> "
				+ "<input id='modType' type='hidden' name='modType' value='" + type + "'/>"
				+ "<input id='actionId' type='hidden' name='actionId' value='" + selColId.split("+")[1] + "'/>"
				+ "<input id='actionName' type='text' placeholder='请输入操作名称' name='actionName'></p>"
				+ "<p><a class='btn btn-info disabled'>操作URL：</a> "
				+ "<input id='actionUrl' class='span4' type='text' placeholder='请输入操作URL' name='actionUrl'></p>";
		var buttonHtml = "<button id='addAction' class='btn btn-info'>添加</button>";
		$("#modArea").html(modHtml);
		$("#buttonGroup").append(buttonHtml);
		$("#actionName").val(name);
		$("#actionUrl").val(url);
		var paIds = parColId.split(";");
		findColu();
		$('option[value="' + parseInt(paIds[0]) + '"]').attr("selected", "selected");
		findsecColu();
		$('option[value="' + parseInt(paIds[1]) + '"]').attr("selected", "selected");
	}
}

// 查找一级栏目
function findColu() {
	$.get("../../handler/column/firLevelCol", function(data, status) {
		var optionHtml = "";
		$.each(data.data.firLevelCol, function(index, item) {
			optionHtml += "<option value='" + item.fumoId + "'>" + item.fumoName + "</option>";
		});
		$("#firstColId").html(optionHtml);
	});
}

// 查找二级栏目
function findsecColu() {
	var id = $("#firstColId").val();
	$.post("../../handler/column/secLevelCol", {
		"firId" : id
	}, function(data) {
		var optionHtml = "";
		if (data.ret) {
			$.each(data.data.subCol, function(index, item) {
				optionHtml += "<option value='" + item.fumoId + "'>"
						+ item.fumoName + "</option>";
			});
		} else {
			optionHtml += "<option value=0>二级栏目获取失败</option>";
		}
		$("#secondColId").html(optionHtml);
	});
}

// 邦定添加模块按钮
function bindAddFunctionBtn() {
	// 添加一级栏目
	$("#addFunctionBtn").click(function() {
		var type = $("#addType").val();
		if (validateData(type)) {
			// 装载参数
			var colAddParam = buildAddParam(type);
			$.post("../../handler/column/saveNewCol", colAddParam, function(data) {
				if (data.ret) {
					$("#addModal").modal('hide');
					$("#infomsg").html("<div class='alert alert-success'>新增成功</div>");
					showContent();
				} else {
					$("#addMsg").html("<div class='alert alert-error'>" + data.errmsg + "</div>");
				}
			});
		}
	});
}
// 邦定添加action按钮
function bindAddAction() {
	// 添加action
	var colAddParam = buildAddParam(3);
	$("#addAction").click(function() {
		if (validateData(3) && colAddParam.parentid > 0) {
			// 装载参数
			$.post("../../handler/column/saveNewCol", colAddParam, function(data) {
				if (data.ret) {
					$("#modModal").modal('hide');
					$("#infomsg").html("<div class='alert alert-success'>新增成功</div>");
					showContent();
				} else {
					$("#addMsg").html("<div class='alert alert-error'>请检查url是否重复添加，具体错误信息" + data.errmsg + "</div>");
				}
			});
		} else
			$("#addMsg").html("<div class='alert alert-error'>内容填写错误，请核对！</div>");
	});
}

// 邦定编辑保存按钮
function bindModFunctionBtn() {
	// 编辑保存栏目
	$("#modFunctionBtn").click(function() {
		var type = $("#modType").val();
		if (validateData(type)) {
			var colEditParam = buildEditParam(type);
			$.post("../../handler/column/editCols", colEditParam, function(data) {
				if (data.ret) {
					$("#modModal").modal('hide');
					$("#infomsg").html("<div class='alert alert-success'>编辑成功</div>");
					showContent();
				} else {
					$("#addMsg").html("<div class='alert alert-error'>请检查url是否重复添加，具体错误信息"+data.errmsg+"</div>");
				}
			});
		}
	});
}

// 页面检验输入参数是否合法
function validateData(type) {
	if (type == 1) {
		var colName = $("#firstColName").val();
		var colUrl = $("#firstColUrl").val();
		var colOrder = $("#firstColOrder").val();
		if (colName == null || colName == "") {
			$("#addMsg").html("<div class='alert alert-error'>请填写栏目名称</div>");
			return false;
		}
		if (colUrl == null || colUrl == "") {
			$("#addMsg").html("<div class='alert alert-error'>请填写栏目URL</div>");
			return false;
		}
		if (colOrder == null || colOrder == "") {
			$("#addMsg").html("<div class='alert alert-error'>请填写栏目顺序</div>");
			return false;
		}
		if (isNaN(colOrder)) {
			$("#addMsg").html("<div class='alert alert-error'>栏目顺序必须为数字</div>");
			return false;
		}
		$.each(firstColData, function(index, item) {
			if (item.colname == colName) {
				$("#addMsg").html("<div class='alert alert-error'>填写的栏目名称已存在</div>");
				return false;
			}
		});
	} else if (type == 2) {
		var secondColName = $("#secondColName").val();
		var secondColUrl = $("#secondColUrl").val();
		var secondColOrder = $("#secondColOrder").val();
		if (secondColName == null || secondColName == "") {
			$("#addMsg").html("<div class='alert alert-error'>请填写二级栏目名称</div>");
			return false;
		}
		if (secondColUrl == null || secondColUrl == "") {
			$("#addMsg").html("<div class='alert alert-error'>请填写二级栏目URL</div>");
			return false;
		}
		if (secondColOrder == null || secondColOrder == "") {
			$("#addMsg").html("<div class='alert alert-error'>请填写二级栏目顺序</div>");
			return false;
		}
		if (isNaN(secondColOrder)) {
			$("#addMsg").html("<div class='alert alert-error'>二级栏目顺序必须为数字</div>");
			return false;
		}
		var id = $("#firstColId").val();
		$.each(colData, function(itemIndex1, item1) {
			if (item1.colid == id) {
				$.each(item1.subcols, function(itemIndex2, item2) {
					if (item2.colname == secondColName) {
						$("#addMsg").html("<div class='alert alert-error'>填写的二级栏目名称以存在</div>");
						return false;
					}
				});
			}
		});
	} else if (type == 3) {
		var actionName = $("#actionName").val();
		var actionUrl = $("#actionUrl").val();
		if (actionName == null || actionName == "") {
			$("#addMsg").html("<div class='alert alert-error'>请填写您要添加的操作</div>");
			return false;
		}
		if (actionUrl == null || actionUrl == "") {
			$("#addMsg").html("<div class='alert alert-error'>请填写您要添加的URL</div>");
			return false;
		}
	} else {
		$("#addMsg").html("<div class='alert alert-error'>请选择下拉框试试</div>");
		return false;
	}
	return true;
}

// 组装要编辑的参数
function buildAddParam(type) {
	var colAddParam = {
		"type" : type,
		"parentid" : 0,
		"colname" : "",
		"colurl" : "",
		"colicon" : "",
		"colorder" : 0
	};// 新建的参数
	if (type == 1) {
		colAddParam.colname = $("#firstColName").val();
		colAddParam.colurl = $("#firstColUrl").val();
		colAddParam.colicon = $("#firstColIcon").val();
		colAddParam.colorder = $("#firstColOrder").val();
	} else if (type == 2) {
		colAddParam.parentid = $("#firstColId").val();
		colAddParam.colname = $("#secondColName").val();
		colAddParam.colurl = $("#secondColUrl").val();
		colAddParam.colorder = $("#secondColOrder").val();
	} else if (type == 3) {
		colAddParam.parentid = $("#secondColId").val();
		colAddParam.colname = $("#actionName").val();
		colAddParam.colurl = $("#actionUrl").val();
	}
	return colAddParam;
}

// 组装编辑保存时的参数
function buildEditParam(type) {
	var colEditParam = {
		"type" : type,
		"parentid" : 0,
		"colid" : 0,
		"colname" : "",
		"colurl" : "",
		"colicon" : "",
		"colorder" : 0
	};
	if (type == 1) {
		colEditParam.colid = $("#firstColId").val();
		colEditParam.colname = $("#firstColName").val();
		colEditParam.colurl = $("#firstColUrl").val();
		colAddParam.colicon = $("#firstColIcon").val();
		colEditParam.colorder = $("#firstColOrder").val();
	} else if (type == 2) {
		colEditParam.parentid = $("#firstColId").val();
		colEditParam.colid = $("#secondColId").val();
		colEditParam.colname = $("#secondColName").val();
		colEditParam.colurl = $("#secondColUrl").val();
		colEditParam.colorder = $("#secondColOrder").val();
	} else if (type == 3) {
		colEditParam.parentid = $("#secondColId").val();
		colEditParam.colid = $("#actionId").val();
		colEditParam.colname = $("#actionName").val();
		colEditParam.colurl = $("#actionUrl").val();
	}
	return colEditParam;
}


//邦定删除按钮
function bindDelColumn() {
	$("#delButton").click(function() {
		var params = new Object();
		params.data = getDelParam();
		alert(params.data);
		var delParams = $.param(params, true);
		var url = "../../handler/column/delCols";
		$.post(url, delParams, function(data) {
			if(data.ret){
				$("#infomsg").html("<div class='alert alert-success'>删除成功</div>");
				showContent();
			}else{
				$("#infomsg").html("<div class='alert alert-error'>删除失败</div>");
			}
		}, "json");
	});
}
function getDelParam() {
	var params = new Array();
	// 遍历功能
	$('#mainTable tr').each(function() {
		// 遍历该模块下所有操作的多选框
		$(this).find('input').each(function() {
			if ($(this).attr('checked') != undefined) {
				var cli = {
					"colType" : 0,
					"firstCol" : 0,
					"secondCol" : 0,
					"colId" : 0
				};
				var type = $(this).attr("coltype");
				if (type == 1) {
					cli.colType = 1;
					cli.firstCol = $(this).attr("id");
					cli.colId = $(this).attr("id");
				} else if (type == 2) {
					cli.colType = 2;
					cli.colId = $(this).attr("id");
					cli.secondCol = $(this).attr("id");
				} else if (type == 3) {
					cli.colType = 3;
					cli.firstCol = $(this).attr("firstCol");
					cli.secondCol = $(this).attr("secondCol");
					cli.colId = $(this).attr('id').split("+")[1];
				}
				params.push($.param(cli, true));
			}
		});
	});
	return params;
}