@/*
表单中textarea框标签中各个参数的说明:

id : textarea框id
name : textarea框名称
readonly : readonly属性
clickFun : 点击事件的方法名
style : 附加的css属性
labelCol: label宽度
valueCol: textarea的div宽度
rows:  textarea列宽度
@*/
<div class="form-group">
    @if(isNotEmpty(label)){
    <label class="col-sm-${labelCol!3} control-label"

           @if(isNotEmpty(labelStyle)){
           style="${labelStyle}"
           @}

    >${label}
    </label>
    @}

    <div class="col-sm-${valueCol!9}">
            <textarea class="form-control"
                @if(isNotEmpty(id)){
                id="${id}"
                @}

                @if(isNotEmpty(name)){
                name="${name}"
                @}

                rows="${rows!4}"

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
            >@if(isNotEmpty(value)){
${tool.dateType(value)}
            @}
</textarea>
    </div>
</div>
@if(isNotEmpty(underline) && underline == 'true'){
<div class="hr-line-dashed"></div>
@}


