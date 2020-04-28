// 删除按钮文件赋值给删除文件模态框
function values(fileCode){
    $('#model-fileCode').val(fileCode);
}

// 下载文件函数
var DownLoadFile = function (options) {
    var config = $.extend(true, { method: 'get' }, options);
    var $iframe = $('<iframe id="down-file-iframe" />');
    var $form = $('<form target="down-file-iframe" method="' + config.method + '" />');
    $form.attr('action', config.url);
    for (var key in config.data) {
        $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
    }
    $iframe.append($form);
    $(document.body).append($iframe);
    $form[0].submit();
    $iframe.remove();
}

// 重置文件名的div的html内容
var resetFileDivHtml = function (fileCode, ioniconName, fileName){
	$("#file-name-div-"+fileCode).html("<ion-icon name="+ioniconName+" id='file-ionicon-"+fileCode+"'></ion-icon>&nbsp;&nbsp;<span id='file-name-span-"+fileCode+"'>"+fileName+"</span>");
}

var resetFileName = function(fileCode, ioniconName, oldFileName, currentPath) {
	var newFileName = $("#file-rename-input-"+fileCode).val();
	
	if(newFileName == '' || newFileName.trim() == '') {
		toastr.error("文件名不允许为空！");
		return;
	}

	newFileName = newFileName.trim();
	
	if(newFileName.indexOf(" ") != -1) {
		toastr.error("文件名中不允许包含空格！");
		return;
	}
	
	if(newFileName == oldFileName) {
		resetFileDivHtml(fileCode, ioniconName, oldFileName);
		return;
	}
	
	var isSuccessUpdate = false;
    $.ajax({
        url : "./file/update",
        async : false,
        type : "POST",
        dataType : "json",
        contentType : "application/json", 
        data: JSON.stringify({'oldPath': currentPath, 'oldFileName': oldFileName, 'newFileName': newFileName}),
        success : function(response) {
            if(response.code == 0) {
	        	toastr.success("更新成功！");
	        	isSuccessUpdate = true;
            } else {
	        	toastr.error("更新失败！失败原因：" + response.msg);
            }
        },
        error: function() {
        	toastr.error("系统异常！");
     	},
     	complete: function() {
     		window.location.reload();
//     		resetFileDivHtml(fileCode, ioniconName, isSuccessUpdate ? newFileName : oldFileName);
        } 
    });
    
}

// 删除文件
function deleteFile(fileCode){
    $.ajax({
        url : "./file/delete",
        async : false,
        type : "POST",
        dataType : "json",
        contentType : "application/json", 
        data: JSON.stringify({'code': $('#model-fileCode').val()}),
        success : function(response) {
            if(response.code == 0) {
	        	toastr.success("删除成功！code="+ response.data.fileCode);
            } else {
	        	toastr.error("删除失败！失败原因：" + response.msg);
            }
        },
        error: function(){
        	toastr.error("系统异常！");
     	},
     	complete: function(){
            $('#model-fileCode').val("");
        }
    });
    
}

// toastr 提示组件初始化
$(function () {
	toastr.options = {
			"closeButton": false, // 是否显示关闭按钮
			"debug": false, // 是否使用debug模式
			"positionClass": "toast-top-center",// 弹出窗的位置
			"showDuration": "300",// 显示的动画时间
			"hideDuration": "1000",// 消失的动画时间
			"timeOut": "2000", // 展现时间
			"extendedTimeOut": "1000",// 加长展示时间
			"showEasing": "swing",// 显示时的动画缓冲方式
			"hideEasing": "linear",// 消失时的动画缓冲方式
			"showMethod": "fadeIn",// 显示时的动画方式
			"hideMethod": "fadeOut", // 消失时的动画方式
			"progressBar": true // 是否显示进度条
	};
	
	// 使用容量加载
	$('#usedSize').ionRangeSlider({
	    type      : 'single',
	    step      : 1,
	    postfix   : 'GB',
	    from      : 35,
	    hideMinMax: true,
	    hideFromTo: true
	});
	
});

// 监听文件属性加载模态框
$(function () { 
    $('#modal-info').on('hide.bs.modal', function () {
    	$(this).removeData("bs.modal");
	})
});

// 操作按钮点击事件监听
$('.opreate-btn').click(function() {
	
	var opreation = $(this).data('opreation');
	
	// 返回上级目录
	if(opreation == 'changeDirToParent') {
		var toParent = $(this).data('toparent');
		var currentPath = $(this).data('currentpath');
		window.location.href='./?path='+encodeURI(currentPath)+'&toParent='+toParent
		return;
	}
	
	var aprentObj = $(this).parents("tr");
	var fileCode = aprentObj.data('code');
	
	// 文件夹预览
	if($(this).children("ion-icon").length == 1 && aprentObj.data('isdirectory') && opreation == 'preview') {
		if($('#dir-'+fileCode).data('dir') != 'true') {
			return;
		}
		
		var filePath = aprentObj.data('filepath');
		window.location.href='./?path='+encodeURI(filePath);
		return;
	}
	
	// 下载文件
	if(opreation == 'download') {
		var path = aprentObj.data('path');
		var fileName = aprentObj.data('name');
		window.location.href='./file/download?path='+encodeURI(path)+'&fileName='+encodeURI(fileName)
		return;
    }
	
	// 分享文件
	if(opreation == 'share') {
		$.ajax({
			url : "./file/share",
			async : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json", 
			data: JSON.stringify({'code': fileCode}), 
			success : function(response) {
				if(response.code == 0) {
					toastr.success("分享成功！" + response.data.shareCode); 
				} else {
					toastr.error("分享失败！失败原因：" + response.msg); 
				}
            },
			error: function(){
	            toastr.error("系统异常！");
	        }
		});
		return;
    }
	
	// 查看文件详情
	if(opreation == 'info') {
		var currentPath = aprentObj.data('path');
		var fileName = aprentObj.data('name');
		
		$.ajax({
			url : './file/detail',
			async : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json", 
			data: JSON.stringify({'code': fileCode, 'path': currentPath, 'fileName': fileName}), 
			success : function(response) {
				if(response.code == 0) {
					$("#modalFileCode").text(response.data.fileCode)
					$("#modalName").text(response.data.name)
					$("#modalFileSize").text(response.data.fileSize)
					$("#modalFormatedFileSize").text(response.data.formatedFileSize)
					$("#modalFilePath").text(response.data.filePath)
					$("#modalUpdateTime").text(response.data.updateTime)
					$("#modalCreator").text(response.data.creator)
					$("#modal-info").modal();
				} else {
					toastr.error("分享失败！失败原因：" + response.msg); 
				}
            },
			error: function(){
	            toastr.error("系统异常！");
	        }
		});
		return;
	}
	
	// 更新文件名
	if(opreation == 'edit') {
		$('#dir-'+fileCode).attr('data-dir', false);
		
		var ioniconName = aprentObj.data('ionicon');
		var currentPath = aprentObj.data('path');
		var filePath = aprentObj.data('filepath');
		var fileName = aprentObj.data('name');
		
		var fileNameDivId = "#file-name-div-"+fileCode;
		var fileIoniconNameId = "#file-ionicon-"+fileCode;
		var oldHtml = $(fileNameDivId).html().replace("\"","\'");
		
		var input = $("<div class='input-group input-group-sm'><input type='text' value='" + fileName + "' id='file-rename-input-"+fileCode+"' class='form-control' />" +
						"<span class='input-group-append'>" +
						"<span class='input-group-text bg-teal' onclick=resetFileName('"+fileCode+"','"+ioniconName+ "','"+fileName+"','"+ currentPath +"');>" +
						"<i class='fas fa-check'></i>" +
						"</span>" +
						"<span class='input-group-text bg-pink' onclick=resetFileDivHtml('"+fileCode+"','"+ioniconName+ "','"+fileName+"');>" +
						"<i class='fa fa-times'><i>" +
						"</span>" +
						"</span>" +
						"<div>");
		$(fileNameDivId).html(input);
	}
});

