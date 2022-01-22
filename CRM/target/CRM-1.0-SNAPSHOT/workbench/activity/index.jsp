<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


<script type="text/javascript">

	$(function(){
	    //为创建按钮提供绑定事件，打开添加操作的模态窗口--------------------
        $(".time").datetimepicker({
            minView: "month",
            language:  'zh-CN',
            format: 'yyyy-mm-dd',
            autoclose: true,
            todayBtn: true,
            pickerPosition: "bottom-left"
        });

		//绑定操作添加模态窗口------------------------------------------
        $("#addBtn").click(function () {
			/*绑定操作模态窗口*/
			/*需要操作的模态窗口的jQuery对象，调用modal方法，为改方法传递参数 show：打开窗口   hide：关闭窗口*/
			//$("#createActivityModal").modal("show");

			//走后台区用户信息列表
			$.ajax({
				url:"workbench/activity/getUserList.do",
				data:{

				},
				type:"get",
				dataType:"json",
				success:function (data) {
					var html = "<option></option>";
					//遍历
					$.each(data,function (i,n) {
						html+="<option value='"+ n.id +"'>"+n.name+"</option>";
					})
					$("#create-marketActivityOwner").html(html);
					$("#create-marketActivityOwner").val("${user.id}");
					//下拉框处理完毕，展现模态窗口
					$("#createActivityModal").modal("show");
				}
			})
		})

		//执行保存添加操作---------------------------------------
        $("#saveBtn").click(function () {
            $.ajax({
                url:"workbench/activity/save.do",
                data:{
                    "owner": $.trim($("#create-marketActivityOwner").val()),
                    "name":$.trim($("#create-marketActivityName").val()),
                    "startDate":$.trim($("#create-startTime").val()),
                    "endDate":$.trim($("#create-endTime").val()),
                    "cost":$.trim($("#create-cost").val()),
                    "description":$.trim($("#create-describe").val())
                },
                type: "get",
                dataType: "json",
                success: function (data) {
					if (data.success){
						//添加成功
						//局部刷新活动信息列表
						/*
							使用reset方法，重置表单，但是jQuery对象没有这个方法
							所以只能把jQuery对象转换成原生js对象

							jQuery对象转换为dom对象：
								jQuery对象[下标]
							dom对象转换为jQuery对象：
								$(dom)

						*/
						$("#activityAddForm")[0].reset();
						//alert("添加成功!");
						//关闭添加操作的模态窗口
						$("#createActivityModal").modal("hide");
						//刷新页面
						pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

					}else{
						alert("添加市场活动失败！");
					}
                }
            })
        })

		//页面加载后查询活动并刷新页面-----------------------------
		pageList(1,6);

        $("#searchBtn").click(function () {
        	//保存输入框的数据到隐藏域
			$("#hidden-name").val($.trim($("#search-name").val()));
			$("#hidden-owner").val($.trim($("#search-owner").val()));
			$("#hidden-startDate").val($.trim($("#search-startDate").val()));
			$("#hidden-endDate").val($.trim($("#search-endDate").val()));
			pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

		})

		//为全选的复选框绑定事件，触发全选---------------------------------------
		$("#qx").click(function () {
			$("input[name=xz]").prop("checked",this.checked);
		})

		//执行动态全选--------------------------------------
		$("#activityBody").on("click",$("input[name=xz]"),function () {
			/*	动态生成的不能直接使用$("input[name=xz]")来选择
			要以on方法的形式来触发事件，
			$(需要绑定元素的有效的外层元素).on(绑定事件的方式，需要绑定的元素的jQuery对象，回调函数)	*/
			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length);
		})

        //为删除按钮绑定事件，执行市场活动删除操作----------------------------------------
        $("#deleteBtn").click(function () {

            var $xz = $("input[name=xz]:checked");
            if ($xz.length==0){
                alert("您未选择任何内容！请选择内容后删除！");
            }else{
            	if (confirm("确定删除所选择的内容吗？")){
					//url:workbench/activity/delete.do?id=xxx&id=xxx&id=xxx
					//拼接参数
					var param = "";
					//将$xz中的每一个dom对象遍历出来，取其value值，相当于取得了需要删除的记录的id
					for(var i=0;i<$xz.length;i++){
						//param += "id="+$xz[i].value();   //使用传统dom取值，下面是封装为jQuery取值
						param += "id="+$($xz[i]).val();
						if (i<$xz.length-1){
							param += "&";
						}
					}
					//alert(param);
					$.ajax({
						url:"workbench/activity/delete.do",
						data:param,
						type:"get",
						dataType:"json",
						success:function (data) {
							/*返回一个{"success":true/false}*/
							if (data.success){
								//alert("删除成功！");
								pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

							}else{
								alert("删除失败！");
							}
						}
					})
				}

            }
        })

		//为修改按钮绑定事件，打开修改操作的模态窗口----------------------------------------
		$("#editBtn").click(function () {
			var $xz = $("input[name=xz]:checked");
			if ($xz.length==0){
				alert("请选择需要修改的记录!");
			}else if ($xz.length > 1){
				alert("只能选择一条记录进行修改!");
			} else{
				//因为只有一条记录所以可以使用val直接取值
				var id = $xz.val();

				$.ajax({
					url:"workbench/activity/getUserListAndActivity.do",
					data:{
						"id":id
					},
					type:"get",
					dataType:"json",
					success:function (data) {
						/*
							data
								用户列表			市场活动对象
								{"uList":[{用户1},{2},{3}],"activity":{市场活动}}
						*/
						var html = "<option></option>";
						//遍历
						$.each(data.uList,function (i,n) {
							html+="<option value='"+ n.id +"'>"+n.name+"</option>";
						})
						$("#edit-marketActivityOwner").html(html);

						//处理单条activity
						$("#edit-id").val(data.activity.id);
						$("#edit-marketActivityOwner").val(data.activity.owner);
						$("#edit-marketActivityName").val(data.activity.name);
						$("#edit-startTime").val(data.activity.startDate);
						$("#edit-endTime").val(data.activity.endDate);
						$("#edit-cost").val(data.activity.cost);
						$("#edit-describe").val(data.activity.description);
						//下拉框处理完毕，展现模态窗口
						$("#editActivityModal").modal("show");

					}
				})
			}
		})

        //为更新事件绑定，市场修改活动操作
        $("#updateBtn").click(function () {
            $.ajax({
                url:"workbench/activity/update.do",
                data:{
                    "id": $.trim($("#edit-id").val()),
                    "owner": $.trim($("#edit-marketActivityOwner").val()),
                    "name":$.trim($("#edit-marketActivityName").val()),
                    "startDate":$.trim($("#edit-startTime").val()),
                    "endDate":$.trim($("#edit-endTime").val()),
                    "cost":$.trim($("#edit-cost").val()),
                    "description":$.trim($("#edit-describe").val())
                },
                type: "post",
                dataType: "json",
                success: function (data) {
                    if (data.success){
                        //添加成功
                        //局部刷新活动信息列表
                        /*
                            使用reset方法，重置表单，但是jQuery对象没有这个方法
                            所以只能把jQuery对象转换成原生js对象

                            jQuery对象转换为dom对象：
                                jQuery对象[下标]
                            dom对象转换为jQuery对象：
                                $(dom)

                        */
                        //$("#activityAddForm")[0].reset();
                        //alert("添加成功!");
                        //关闭添加操作的模态窗口
                        $("#editActivityModal").modal("hide");
                        //刷新页面
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

					}else{
                        alert("修改市场活动失败！");
                    }
                }
            })
        })



	});

	//查询产品活动信息，分页显示，局部刷新----------------------
	function pageList(pageNo,pageSize) {
	    //将全选框的复选框干掉
        $("#qx").prop("checked",false);

	    //查询前，将隐藏域中保存的值取出来，重新赋值给搜索框
		$("#search-name").val($.trim($("#hidden-name").val()));
		$("#search-owner").val($.trim($("#hidden-owner").val()));
		$("#search-startDate").val($.trim($("#hidden-startDate").val()));
		$("#search-endDate").val($.trim($("#hidden-endDate").val()));

		$.ajax({
			url:"workbench/activity/pageList.do",
			data:{
				"pageNo" : pageNo,
				"pageSize" : pageSize,
				"name" : $.trim($("#search-name").val()),
				"owner" : $.trim($("#search-owner").val()),
				"startDate" : $.trim($("#search-startDate").val()),
				"endDate" : $.trim($("#search-endDate").val())
			},
			type:"get",
			dataType:"json",
			success: function (data) {
				/*
					需要市场活动信息列表，总记录数total
					{"total":100,"dataList":[{活动1},{2},{3}]}
				*/
				var html = "";
				//每一个N都是一个市场活动对象
				$.each(data.dataList,function (i,n) {
					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" value="'+ n.id +'"/></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.do?id='+n.id+'\';">'+ n.name +'</a></td>';
					html += '<td>'+ n.owner +'</td>';
					html += '<td>'+ n.startDate +'</td>';
					html += '<td>'+ n.endDate +'</td>';
					html += '</tr>';
				})

				$("#activityBody").html(html);
				var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;

				//数据处理完毕之后展现分页信息
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}



		})
		//alert("展现市场活动列表！");
	}
	
</script>
</head>
<body>

	<input type="hidden" id="hidden-name">
	<input type="hidden" id="hidden-owner">
	<input type="hidden" id="hidden-startDate">
	<input type="hidden" id="hidden-endDate">

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activityAddForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								  <option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
                    <%--data-dismiss="modal" 表示关闭模态窗口--%>
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">

						<input type="hidden" id="edit-id"/>
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">

								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startTime" readonly>
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endTime" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<%--关于文本域textarea：
										（1）一定是要以标签对的形式来呈现，正常状态下标签对要紧紧的挨着
										（2）textarea虽然是以标签对的形式来呈现的，但是他也是属于表单元素范畴
											我们所有的对于textarea的取值和赋值操作，应该统一使用val()方法（而不是html()方法）
								--%>
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="search-startDate" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="search-endDate">
				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
				<%--<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b>50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>--%>
			</div>
			
		</div>
		
	</div>
</body>
</html>