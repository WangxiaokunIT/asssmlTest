@/*
select查询条件标签中各个参数的说明:
id : select的id
name : select的名称
tagName: select标题
placeholder: 默认提示字符
readonly: 是否只读
disabled: 是否禁用
tableName:数据查询表名字
valueColumn:数据查询value字段
textColumn:数据查询text字段
whereColumn:where条件字段
whereValue:where条件值
underline : 是否带分割线
@*/
<div class="input-group">
    <div class="input-group-btn">
        <button data-toggle="dropdown" class="btn btn-white dropdown-toggle" type="button">
            ${tagName!}
        </button>
    </div>
    <select class="form-control" data-placeholder="${placeholder!'请选择'}" tabindex="1"
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
            $("#${id!}").chosen({
                no_results_text: '没有找到',//没有搜索到匹配项时显示的文字
                disable_search_threshold:5, //少于 n 项时隐藏搜索框
                search_contains:true,
                placeholder_text_single:"请选择"
            });

            function ${id!}LoadData(){
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
                        Feng.selectLoadData("${id!}",postData);
                        $("#${id}").trigger('change');
                    })
                    $("${cascade}").trigger('change');
                    $("#${id!}").val("${value}");
                    $("#${id!}").trigger("chosen:updated");
                @}else{
                    Feng.selectLoadData("${id}",postData,${value!});
                @}
            }
            ${id}LoadData();

        });
    </script>
</div>