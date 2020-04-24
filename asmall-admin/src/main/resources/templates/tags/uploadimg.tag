@/*
    图片参数的说明:

@*/
<div class="form-group">
    <label class="col-sm-3 control-label head-scu-label">${label}</label>
    <div class="col-sm-4">
        <div id="${id}PreId">
            <div><img width="100px" height="100px" src="${value!}"></div>
        </div>
    </div>
    <div class="col-sm-2">
        <div class="head-scu-btn upload-btn" id="${id}BtnId">
            <i class="fa fa-upload"></i>&nbsp;上传
        </div>
    </div>
    <input type="hidden" id="${id}" value="${value!''}"/>
</div>
@if(isNotEmpty(underline) && underline == 'true'){
    <div class="hr-line-dashed"></div>
@}

