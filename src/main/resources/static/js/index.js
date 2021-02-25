$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	$("#publishModal").modal("hide");
	//获取标题和内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	//ajax（异步请求发送数据）
	$.post(
		CONTEXT_PATH + "/discuss/add",
		{"title":title,"content":content},
		//处理返回结果
		function (data) {
			//将JSON字符串转为js对象
			data = $.parseJSON(data);
			//将返回结果显示在提示框
			$("#hintModal").text(data.msg);
			//显示提示框，2s后自动关闭
			$("#hintModal").modal("show");
			setTimeout(function(){
				$("#hintModal").modal("hide");
				//如果发布成功，则刷新页面
				if(data.code == 0) {
					window.location.reload();
				}
			}, 2000);

		}
	);

}