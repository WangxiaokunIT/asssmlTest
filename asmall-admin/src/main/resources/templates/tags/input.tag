@/*
表单中input框标签中各个参数的说明:

labelCol: label标签的宽度
labelStyle: label标签的css属性
labelName : label标签的内容

id : input标签id
name : input标签的name
type : input标签的type,默认为text
value: input标签的value
readonly : input标签的readonly属性
clickFun : input标签点击事件的方法名
style : input标签附加的css属性
disabled: input标签的disabled属性
valueCol:  input标签宽度

@*/
<div id="${id!}-group" class="form-group">
    @if(isNotEmpty(label)){
    <label class="col-sm-${labelCol!3} control-label"

           @if(isNotEmpty(labelStyle)){
           style="${labelStyle}"
           @}

           @if(isNotEmpty(labelId)){
           id="${labelId}"
           @}

          >${label}
        @if(isNotEmpty(required)){
            <span style="color:red">*</span>
        @}
    </label>
    @}

    <div class="col-sm-${valueCol!9}">
        @if(isNotEmpty(addonValue)){
        <div class="input-group">
        @}
            <input class="form-control"

                   @if(isNotEmpty(valueStyle)){
                   style="${valueStyle}"
                   @}

                   @if(isNotEmpty(id)){
                   id="${id}"
                   @}

                   @if(isNotEmpty(name)){
                   name="${name}"
                   @}

                   @if(isNotEmpty(min)){
                   min="${min}"
                   @}

                   @if(isNotEmpty(max)){
                   max="${max}"
                   @}

                   @if(isNotEmpty(step)){
                   step="${step}"
                   @}

                   @if(isNotEmpty(value)){
                   value="${tool.dateType(value)}"
                   @}

                   @if(isNotEmpty(type)){
                   type="${type}"
                   @}else{
                   type="text"
                   @}

                   @if(isNotEmpty(readonly)){
                   readonly="${readonly}"
                   @}

                   @if(isNotEmpty(clickFun)){
                   onclick="${clickFun}"
                   @}

                   @if(isNotEmpty(style)){
                   style="${style}"
                   @}

                   @if(isNotEmpty(disabled)){
                   disabled="${disabled}"
                   @}

                   @if(isNotEmpty(placeholder)){
                   placeholder="${placeholder}"
                   @}

                   @if(isNotEmpty(required)){
                   required="${required}"
                   @}
                   @if(isNotEmpty(onkeyup)){
                   onkeyup="${onkeyup}"
                   @}
            >
            @if(isNotEmpty(addonValue)){
                <span class="input-group-addon">${addonValue}</span>
                </div>
            @}

            @if(isNotEmpty(hidden)){
            <input type="hidden" id="${hidden}" value="${hiddenValue!}">
            @}

            @if(isNotEmpty(selectFlag)){
            <div id="${selectDivId}" style="display: none; position: absolute;z-index: 200;">
                <ul id="${selectTreeId}" class="ztree tree-box" style="${selectStyle!}"></ul>
            </div>
            @}
    </div>
</div>
@if(isNotEmpty(underline) && underline == 'true'){
<div class="hr-line-dashed"></div>
@}


