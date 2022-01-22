<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

String fullname = request.getParameter("fullname");
String id = request.getParameter("id");
String appellation = request.getParameter("appellation");
String company = request.getParameter("company");
String owner = request.getParameter("owner");

%>
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

		$("#isCreateTransaction").click(function(){
			if(this.checked){
				$("#create-transaction2").show(200);
			}else{
				$("#create-transaction2").hide(200);
			}
		});

		//为放大镜图标绑定事件，打开搜索市场活动，模态窗口
		$("#openSearchModalBnt").click(function() {
			$("#searchActivityModal").modal("show");
		})

        //为搜索操作模态窗口绑定，搜索市场活动操作
        $("#aname").keydown(function (event) {
            if (event.keyCode==13){
                $.ajax({
                    url:"workbench/clue/getActivityListByName.do",
                    data:{
                        "aname":$.trim($("#aname").val())
                    },
                    type:"get",
                    dataType:"json",
                    success:function (data) {
                        var html = "";
                        $.each(data,function (i,n) {
                            html += '<tr>';
                            html += '<td><input type="radio" name="xz" value="'+n.id+'"/></td>';
                            html += '<td id="'+ n.id +'">'+n.name+'</td>';
                            html += '<td>'+n.startDate+'</td>';
                            html += '<td>'+n.endDate+'</td>';
                            html += '<td>'+n.owner+'</td>';
                            html += '</tr>';
                        })
                        $("#activityBody").html(html);
                    }
                })
                return false;
            }
        })

		//为确定按钮绑定事件，填充市场活动源
		$("#submitActivityBtn").click(function () {
			//取得id
			var $xz = $("input[name=xz]:checked");
			var id = $xz.val();
			var name = $("#"+id).html();

			//将以上两条消息，填写的表中
			$("#activityId").val(id);
			$("#activityName").val(name);

			$("#searchActivityModal").modal("hide");
		})

		//为转换按钮绑定事件，执行线索的转换操作
		$("#convertBtn").click(function () {
			if ($("#isCreateTransaction").prop("checked")){
				//alert("需要创建交易！");
				//加表单中的信息
				//window.location.href = "workbench/clue/convert.do?clueId=${param.id}&money=xxx&expectedDate=xxx&name=xxx&stage=xxx&activityId=xxx";
				//上述表单很麻烦，不好扩充，挂载可能超出上限
				//提交表单
				$("#tranForm").submit();

			}else{
				//alert("不要需要创建交易");
				window.location.href = "workbench/clue/convert.do?clueId=${param.id}&flag=b";
			}
		})
	});
</script>

</head>
<body>
	
	<!-- 搜索市场活动的模态窗口 -->
	<div class="modal fade" id="searchActivityModal" role="dialog" >
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title">搜索市场活动</h4>
				</div>
				<div class="modal-body">
					<div class="btn-group" style="position: relative; top: 18%; left: 8px;">
						<form class="form-inline" role="form">
						  <div class="form-group has-feedback">
						    <input type="text" id="aname" class="form-control" style="width: 300px;" placeholder="请输入市场活动名称，支持模糊查询">
						    <span class="glyphicon glyphicon-search form-control-feedback"></span>
						  </div>
						</form>
					</div>
					<table id="activityTable" class="table table-hover" style="width: 900px; position: relative;top: 10px;">
						<thead>
							<tr style="color: #B3B3B3;">
								<td></td>
								<td>名称</td>
								<td>开始日期</td>
								<td>结束日期</td>
								<td>所有者</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="activityBody">
							<%--<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>
							<tr>
								<td><input type="radio" name="activity"/></td>
								<td>发传单</td>
								<td>2020-10-10</td>
								<td>2020-10-20</td>
								<td>zhangsan</td>
							</tr>--%>
						</tbody>
					</table>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" id="submitActivityBtn">确定</button>
				</div>
			</div>
		</div>
	</div>

	<%--P122 集
		EL表达式为我们提拱了N多个隐含对象
		只有XXXScope系列的隐含对象可以省略
		其他的所有隐含对象一概不能省略（如果省略掉了，会变成从域对象中求值）
		${param.fullname}
		直接用request不行，但是可以用pageContext来调用
		${pageContext.request.contextPath}jsp

		<%在EL表达式中，页面域对象pageContext可以随时随地的转变成别的八个域对象来使用，Java脚本中可以直接使用
		//写入setAttribute
			pageContext.setAttribute("str1","aaa",pageContext.REQUEST_SCOPE);
			pageContext.setAttribute("str1","aaa",pageContext.SESSION_SCOPE);
		//获取
			pageContext.getAttribute("str1",pageContext.SESSION_SCOPE);
		%>

		使用Java脚本表达,但是上面得用request域先取出来，EL不需要
		<h4>转换线索 <small><%=fullname%><%=appellation%>-<%=company%></small></h4>
	--%>

	<div id="title" class="page-header" style="position: relative; left: 20px;">
		<h4>转换线索 <small>${param.fullname}${param.appellation}-${param.company}</small></h4>
	</div>
	<div id="create-customer" style="position: relative; left: 40px; height: 35px;">
		新建客户：${param.company}
	</div>
	<div id="create-contact" style="position: relative; left: 40px; height: 35px;">
		新建联系人：${param.fullname}${param.appellation}
	</div>
	<div id="create-transaction1" style="position: relative; left: 40px; height: 35px; top: 25px;">
		<input type="checkbox" id="isCreateTransaction"/>
		为客户创建交易
	</div>
	<div id="create-transaction2" style="position: relative; left: 40px; top: 20px; width: 80%; background-color: #F7F7F7; display: none;" >
	
		<form id="tranForm" action="workbench/clue/convert.do" method="post">
			<input type="hidden" name="flag" value="a"/>
			<input type="hidden" name="clueId" value="${param.id}"/>

		  <div class="form-group" style="width: 400px; position: relative; left: 20px;">
		    <label for="amountOfMoney">金额</label>
		    <input type="text" class="form-control" id="amountOfMoney" name="money">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="tradeName">交易名称</label>
		    <input type="text" class="form-control" id="tradeName" name="name">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="expectedClosingDate">预计成交日期</label>
		    <input type="text" class="form-control time" id="expectedClosingDate" name="expectedDate">
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="stage">阶段</label>
		    <select id="stage"  class="form-control" name="stage">
		    	<option></option>
		    	<c:forEach items="${stage}" var="s">
					<option value="${s.value}">${s.text}</option>
				</c:forEach>
		    </select>
		  </div>
		  <div class="form-group" style="width: 400px;position: relative; left: 20px;">
		    <label for="activityName">市场活动源&nbsp;&nbsp;<a href="javascript:void(0);" id="openSearchModalBnt" style="text-decoration: none;"><span class="glyphicon glyphicon-search"></span></a></label>
		    <input type="text" class="form-control" id="activityName" placeholder="点击上面搜索" readonly>
			  <input type="hidden" id="activityId" name="activityId"/>
		  </div>
		</form>
		
	</div>
	
	<div id="owner" style="position: relative; left: 40px; height: 35px; top: 50px;">
		记录的所有者：<br>
		<b>${param.owner}</b>
	</div>
	<div id="operation" style="position: relative; left: 40px; height: 35px; top: 100px;">
		<input class="btn btn-primary" type="button" id="convertBtn" value="转换">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input class="btn btn-default" type="button" value="取消">
	</div>
</body>
</html>