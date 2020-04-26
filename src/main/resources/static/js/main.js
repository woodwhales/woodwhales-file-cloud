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
	        	toastr.success("删除成功！");
            } else {
	        	toastr.warning("删除失败！失败原因：" + response.msg);
            }
        },
        error: function(){
        	toastr.error("系统异常！");
     	},
     	complete: function(){
            $('#model-fileCode').val("");
        }, 
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
	var code = $(this).data('code');
	var name = $(this).data('name');
	var path = $(this).data('path');
	
	var filePath = $(this).data('filepath');
	var isDirectory = $(this).data('directory');
	var opreation = $(this).data('opreation');
	var toParent = $(this).data('toparent');
	var currentpath = $(this).data('currentpath');
	
	// 文件夹预览
	if((isDirectory && opreation == 'preview')) {
		window.location.href='./?path='+encodeURI(filePath)
	}
	
	// 返回上级目录
	if(opreation == 'changeDirToParent' && toParent) {
		window.location.href='./?path='+encodeURI(currentpath)+'&toParent='+toParent
	}
	
	// 分享
	if(opreation == 'share') {
		$.ajax({
			url : "./file/share",
			async : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json", 
			data: JSON.stringify({'code': code}), 
			success : function(response) {
				if(response.code == 0) {
					toastr.success("分享成功！" + response.data.shareCode); 
				} else {
					toastr.warning("分享失败！失败原因：" + response.msg); 
				}
            },
			error: function(){
	            toastr.error("系统异常！");
	        }
		});
    }
	
	if(opreation == 'download') {
		window.location.href='./file/download?path='+encodeURI(path)+'&fileName='+encodeURI(name)
    }
	
	if(opreation == 'info') {
		$.ajax({
			url : './file/detail',
			async : false,
			type : "POST",
			dataType : "json",
			contentType : "application/json", 
			data: JSON.stringify({'code': code, 'path': path, 'fileName': name}), 
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
					toastr.warning("分享失败！失败原因：" + response.msg); 
				}
            },
			error: function(){
	            toastr.error("系统异常！");
	        }
		});
	}
	
});