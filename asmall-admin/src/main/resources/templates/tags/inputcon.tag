@/*
名称查询条件标签的参数说明:

tagName : 标签名称
id :   查询内容的input标签的id属性
name : 查询内容的input标签的name属性
placeholder: 查询内容的input标签的placeholder属性
value : 查询内容的input标签的value属性
readonly : readonly="readonly"
@*/
<div class="input-group">
    <div class="input-group-btn">
        <button data-toggle="dropdown" class="btn btn-white dropdown-toggle" type="button">${tagName!}</button>
    </div>
    <input type="text" class="form-control"
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

           @if(isNotEmpty(readonly)){
           readonly="${readonly}"
           @}

    />
</div>