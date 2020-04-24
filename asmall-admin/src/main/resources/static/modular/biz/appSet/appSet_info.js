window.app_set_vue = new Vue({
    el: '#app_set_vue',
    data: {
        obj: {
            id: 1,
            userAgreement: '',
            privacyProtocol: '',
            agentProtocol: '',
            vipDescription: '',
            agentDescription: '',

        },
        ids: ['userAgreement', 'privacyProtocol', 'agentProtocol', 'vipDescription','agentDescription']
    },
    methods: {
        /**
         * 初始化 富文本
         */
        initEdit: function (id) {
            CKEDITOR.replace( id, {
                filebrowserImageUploadUrl :"/editor/upload?",
                removePlugins:'elementspath,resize',
                codeSnippet_theme: 'zenburn',
                height:'500',
                toolbar :
                    [
                        //加粗     斜体，     下划线      穿过线      下标字        上标字
                        ['Bold','Italic','Underline','Strike','Subscript','Superscript'],
                        // 数字列表          实体列表            减小缩进    增大缩进
                        ['NumberedList','BulletedList','-','Outdent','Indent'],
                        //左对 齐             居中对齐          右对齐          两端对齐
                        ['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
                        //超链接  取消超链接 锚点
                        ['Link','Unlink','Anchor'],
                        //图片    flash    表格       水平线            表情       特殊字符        分页符
                        ['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'],
                        '/',
                        // 样式       格式      字体    字体大小
                        ['Styles','Format','Font','FontSize'],
                        //文本颜色     背景颜色
                        ['TextColor','BGColor'],
                        //全屏           显示区块
                        ['Maximize', 'ShowBlocks','-']
                    ]
            });

        },

        /**
         * 遍历所有的富文本框
         */
        initCkEdit: function (fun) {
            if(this.ids && this.ids.length>0) {
                for(let id of this.ids) {
                    this.initEdit(id);
                }
            }

            if(typeof fun == 'function') {
                fun();
            }
        },

        /**
         * 保存app设置
         */
        saveAppSet: function () {
            let userAgreement = CKEDITOR.instances.userAgreement.getData().replace(/style\s*?=\s*?([‘"])[\s\S]*?\1/ig, 'style="width:100%;height:auto;"');
            let privacyProtocol = CKEDITOR.instances.privacyProtocol.getData().replace(/style\s*?=\s*?([‘"])[\s\S]*?\1/ig, 'style="width:100%;height:auto;"');
            let agentProtocol = CKEDITOR.instances.agentProtocol.getData().replace(/style\s*?=\s*?([‘"])[\s\S]*?\1/ig, 'style="width:100%;height:auto;"');
            let vipDescription = CKEDITOR.instances.vipDescription.getData().replace(/style\s*?=\s*?([‘"])[\s\S]*?\1/ig, 'style="width:100%;height:auto;"');
            let agentDescription = CKEDITOR.instances.agentDescription.getData().replace(/style\s*?=\s*?([‘"])[\s\S]*?\1/ig, 'style="width:100%;height:auto;"');
            window.vueUtils.ajax( {
                id: this.obj.id,
                userAgreement: userAgreement,
                privacyProtocol: privacyProtocol,
                agentProtocol:agentProtocol,
                vipDescription: vipDescription,
                agentDescription:agentDescription,
            }, "/appSet/update", function () {
                layer.msg('变更成功');
            })
        },

        /**
         * 获取预设app设置
         */
        showAppSet: function () {
            let this_ = this;
            window.vueUtils.ajax(
                null
            , "/appSet/detail/" + this.obj.id, function (data) {
                this_.obj.id = data.id;
                this_.obj.userAgreement = data.userAgreement;
                this_.obj.privacyProtocol = data.privacyProtocol;
                this_.obj.agentProtocol = data.agentProtocol;
                this_.obj.vipDescription = data.vipDescription;
                this_.obj.agentDescription = data.agentDescription;
                CKEDITOR.instances.userAgreement.setData(data.userAgreement);
                CKEDITOR.instances.privacyProtocol.setData(data.privacyProtocol);
                CKEDITOR.instances.agentProtocol.setData(data.agentProtocol);
                CKEDITOR.instances.vipDescription.setData(data.vipDescription);
                CKEDITOR.instances.agentDescription.setData(data.agentDescription);
            })
        }
    },
    mounted: function () { // 页面第一次加载时执行
        let this_ = this;
        this.$nextTick(() => {
            this_.initCkEdit(function () {
                this_.showAppSet();
            });
        });
    },
    updated: function () { // Vue组件页面变化时执行
        this.$nextTick(() => {

        });
    }
});