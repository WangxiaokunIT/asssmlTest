@/*
    select标签中各个参数的说明:
    id : select的id
    name : select的名称
    label:label标签的内容
    labelCol:label标签的宽度
    labelStyle:label标签的style
    value:select标签的默认值
    valueCol:select标签的宽度
    valueStyle:select内容的style
    multiple:开启多选
    readonly:开启只读
    disabled:开启禁用
    cascade:级联parent
    placeholder:选择提示
    hiddenId:hiddenInput标签的id
    hiddenValue:hiddenInput标签的值
    tableName:数据查询表名字
    valueColumn:数据查询value字段
    textColumn:数据查询text字段
    whereColumn:where条件字段
    whereValue:where条件值
    underline : 是否带分割线
@*/
<div class="form-group">
    <label class="col-sm-${labelCol!3} control-label"
           @if(isNotEmpty(labelStyle)){
           style="${labelStyle}"
           @}
>${label!}</label>
    <div class="col-sm-${valueCol!9}"
         @if(isNotEmpty(valueStyle)){
         style="${valueStyle}"
         @}
><select class="form-control" data-placeholder="${placeholder!}" tabindex="1"
                @if(isNotEmpty(id)){
                id="${id}"
                @}
                @if(isNotEmpty(name)){
                name="${name}"
                @}
                @if(isNotEmpty(multiple)){
                multiple="multiple" size="10"
                @}
                @if(isNotEmpty(readonly)){
                readonly="${readonly}"
                @}
                @if(isNotEmpty(disabled)){
                disabled="${disabled}"
                @}

></select>
        <script type="application/javascript">
            $(function() {
                $("#${id}").chosen({
                    no_results_text: '没有找到',//没有搜索到匹配项时显示的文字
                    disable_search_threshold:5, //少于 n 项时隐藏搜索框
                    search_contains:true,
                    placeholder_text_single:"请选择"
                });

                function ${id}LoadData(){
                    var postData = {
                        tableName:"${secureUtil.encryp(tableName)}",
                        relationColumn:"${secureUtil.encryp(valueColumn)}",
                        targetColumn:"${secureUtil.encryp(textColumn)}",
                        whereColumn:"${secureUtil.encryp(whereColumn)}",
                        whereValue:"${whereValue!}"
                    }
                    @if(isNotEmpty(cascade)&&isNotEmpty(whereColumn)){

                        $("${cascade}").chosen().change(function(a,b){
                            postData.whereColumn="${secureUtil.encryp(whereColumn)}";
                            postData.whereValue=b && b.selected || $("${cascade}").val();
                            Feng.selectLoadData("${id}",postData);
                            $("#${id}").trigger('change');
                        })
                        $("${cascade}").trigger('change');
                        @if(isNotEmpty(value)){
                            $("#${id}").val("${value}");
                            $("#${id}").trigger("chosen:updated");
                        @}
                    @}else{
                        Feng.selectLoadData("${id}",postData,${value!});
                    @}
                }
                    ${id}LoadData();

            });
        </script>
        @if(isNotEmpty(hidden)){
            <input class="form-control" type="hidden"
                   @if(isNotEmpty(hiddenId)){
                   id="${hiddenId}"
                   @}

                   @if(isNotEmpty(hiddenValue)){
                   value="${tool.dateType(hiddenValue)}"
                   @}
                  >
        @}
    </div>
</div>
@if(isNotEmpty(underline) && underline == 'true'){
    <div class="hr-line-dashed"></div>
@}

