@/*
    图片参数的说明:

@*/
<div class="form-group" style="margin-right: 0">
    <label class="col-sm-${labelCol!3} control-label"
           @if(isNotEmpty(labelStyle)){
           style="${labelStyle}"
           @}
    >${label!}
        @if(isNotEmpty(required)){
        <span style="color:red">*</span>
        @}
    </label>
    <div class="col-sm-${valueCol!9}"
         @if(isNotEmpty(valueStyle)){
         style="${valueStyle}"
         @}
    >

        <div  style="text-align: center">
            <div id="upload-file-preview-div-${id}"  style="display: block;margin:10px auto;border:1px solid #e7eaec;width:${width!};height:${height!} ">
                @if(isNotEmpty(value)){
                    @if(type == 'img'){
                    <img src="${value}" width="${width!}" height="${height!}" />
                    @}else if(type == 'video'){
                    <video controls width="${width!}" height="${height!}" >
                        <source src="${value}" type="video/mp4"/>您的浏览器不支持Video标签。</video>
                    @}
                @}else{
                <p style="text-align: left">${placeholder!}</p>
                @}
            </div>
            @if(isEmpty(readonly)){
            <button type="button" class="btn btn-success" onclick="uploadFileFun('${id}')" id="upload-file-btn-${id}">
                <i class="fa fa-upload"></i>&nbsp;上传
            </button>
            <button type="button" class="btn btn-danger"  onclick="deleteUploadFileFun('${id}')" id="delete-file-btn-${id}">
                <i class="fa fa-close"></i>&nbsp;删除
            </button>
            @}

        </div>
        <input type="file" name="file" style="display: none"

            @if(type=='img'){
            accept="jpg,jpeg,JPG,JPEG,png,PNG,GIF,gif"
            @}

            @if(type=='video'){
            accept="mp4,MP4"
            @}

            id="select-file-btn-${id}" onchange="uploadFileFun('${id}',${maxsize},'${type}','${width!}','${height!}')"/>

        <script type="application/javascript">

            $('#upload-file-btn-${id}').click(function(){
                $('#select-file-btn-${id}').click();
            });

            @if(isNotEmpty(value)){
                $("#${id}").val('${value}');
            @}
            var fileUploadPlaceholder${id} = "<p style=\"text-align: left\">${placeholder!}</p>";
            var uploadFileFun;
            var deleteUploadFileFun;

            if(!uploadFileFun) {
                uploadFileFun = function (id,maxsize,type,width,height) {
                    Feng.log(id);
                    let uploadFile = document.getElementById("select-file-btn-" + id).files[0];

                    if (!uploadFile) {
                        return;
                    }

                    if(maxsize && maxsize != -1){
                        if (uploadFile.size > (maxsize * 1024 * 1024)) {
                            Feng.error("文件不能超过20M");
                            return;
                        }
                    }

                    let formData = new FormData();
                    formData.append("file", uploadFile);
                    $.ajax({
                        url: "/mgr/upload",
                        type: 'POST',
                        data: formData,
                        async: false,
                        cache: false,
                        contentType: false,
                        processData: false,
                        success: function (data) {
                            $("#" + id).val(data);
                            let content="";
                            if(type == 'img'){
                                content = "<img  title=\""+uploadFile.name+"\" src=\""+data+"\" "+
                                    "width=\"+width+\" height=\""+height+"\" >";
                            }else if(type == 'video'){
                                content = "<video title=\""+uploadFile.name+"\" controls "+
                                    "width=\""+width+"\" height=\""+height+"\" >"+
                                    "<source src=\""+data+"\" type=\"video/mp4\">您的浏览器不支持Video标签。</video>";
                            }

                            $("#upload-file-preview-div-" + id).html(content);

                            //Feng.info("上传成功!");
                        },
                        error: function (response) {
                            Feng.error("上传失败!");
                            console.log(response);
                        }
                    });
                }
            }
            if(!deleteUploadFileFun) {
                deleteUploadFileFun = function (id) {
                    document.getElementById("select-file-btn-" + id).value = null;
                    $("#"+id).val("");
                    $("#upload-file-preview-div-" + id).html(fileUploadPlaceholder${id});
                }
            }

        </script>

        <input type="hidden"
            @if(isNotEmpty(id)){
            id="${id}"
            @}

            @if(isNotEmpty(value)){
            value="${value}"
            @}
        />

    </div>
</div>
@if(isNotEmpty(underline) && underline == 'true'){
<div class="hr-line-dashed"></div>
@}

