@/*
    时间查询条件标签的参数说明:

    tagName : 标签名称
    id :   查询内容的input标签的id属性
    name : 查询内容的input标签的name属性
    placeholder: 查询内容的input标签的placeholder属性
    value : 查询内容的input标签的value属性
    pattern : 日期的正则表达式(例如:"YYYY-MM-DD")
    isTime : 日期是否带有小时和分钟(true/false)
@*/
<div class="input-group">
    <div class="input-group-btn">
        <button data-toggle="dropdown" class="btn btn-white dropdown-toggle"
                type="button">${tagName!}
        </button>
    </div>
    <input type="text" class="form-control layer-date"
           @if(isNotEmpty(id)){
           id="${id}"
           @}

           @if(isNotEmpty(name)){
           name="${name}"
           @}

           @if(isNotEmpty(value)){
           value="${tool.dateType(value)}"
           @}

           @if(isNotEmpty(placeholder)){
           placeholder="${placeholder}"
           @}

           readonly="readonly"
           />
</div>
<script type="text/javascript">
    $(function () {
        laydate.render({type:'${type!'date'}', elem: '#${id!}',value:'${value!}'
            @if(isNotEmpty(done)){
                ,done: ${done}
            @}
        });
    })

</script>