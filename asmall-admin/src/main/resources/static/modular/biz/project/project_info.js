window.project_info_vue = new Vue({
    el: '#project_info_vue',
    data: {
        user: {},
        disabled: true, // 是否禁用编辑
        obj: {
            number: '',
            supplierId: '1',
            unit: '月'
        },
        copyObj: {}, // 备份
        supplier: { // 供应商信息
        },
        search_article: ['way_of_restitution'],
        select_way_of_restitution: [],
        selectNames: {},
        select_state: {}, // 状态转换
        loading: null,
        projectId: '',
        list_examine: [], // 审核记录
        list_joinin: [], // 加盟信息
        page_type: 1, // 1申请人操作  2审核人操作
        search_c: "name, phone, identityNo, companyAddress, legalName",
        search_a: "searchContent",
        supplierDetail: {},
        quota: 0, // 限制额度
    },
    methods: {
        /**
         * 验证
         */
        validate: function(fun) {

            let c = CKEDITOR.instances.description.getData();
            if(!this.obj.number) {
                parent.layer.msg('未生成项目编号，请等待');
                return;
            }
            if(!c) {
                parent.layer.msg('请填写【加盟积分奖励介绍 / 加盟权益详情】内容');
                return;
            }
            this.obj.supplierId = this.supplier.id;
            if(!this.obj.supplierId) {
                parent.layer.msg('请选择供应商');
                return;
            }
            if(!this.obj.name) {
                parent.layer.msg('请填写项目名称');
                return;
            }
            if(!this.obj.startTime) {
                parent.layer.msg('请选择招募开始时间');
                return;
            }
            if(!this.obj.endTime) {
                parent.layer.msg('请选择招募结束时间');
                return;
            }
            if(this.obj.endTime < this.startTime) {
                parent.layer.msg('开始时间不能比结束时间大');
                return;
            }
            if( !this.obj.minMoney) {
                parent.layer.msg('请填写最低加盟金额');
                return;
            }

            if(!/^\d+$/.test(this.obj.maxMoney)) {
                parent.layer.msg('请填写正确的最低加盟金额');
                return;
            }

           if( this.obj.minMoney%1000 != 0 || this.obj.minMoney == 0 ) {
                parent.layer.msg('最低加盟金额只能是1000的倍数');
                return;
            }

            if(!this.obj.maxMoney) {
                parent.layer.msg('请填写最高加盟金额');
                return;
            }

            if(this.obj.maxMoney > this.quota) {
                parent.layer.msg('不能超过您的最高额度<br>您的最高额度为 ' + this.quota + ' 元' );
                return;
            }

            if( !/^\d+$/.test(this.obj.maxMoney)) {
                parent.layer.msg('请填写正确的最高加盟金额');
                return;
            }

            if( this.obj.maxMoney%1000 != 0 || this.obj.maxMoney == 0 ) {
                parent.layer.msg('最高加盟金额只能是1000的倍数');
                return;
            }

            if(parseInt(this.obj.minMoney) > parseInt(this.obj.maxMoney)) {
                parent.layer.msg('最低加盟金额不能大约最高加盟金额');
                return;
            }

            if(this.obj.maxMoney) {

            }

            if(!this.obj.repaymentMethod) {
                parent.layer.msg('请选择归还方式');
                return;
            }
            if(!this.obj.recruitmentCycle) {
                parent.layer.msg('请填写招募周期');
                return;
            }
            if(!this.obj.unit) {
                parent.layer.msg('请选择招募周期的单位');
                return;
            }
            if(!this.obj.equityRate) {
                parent.layer.msg('请填写权益率');
                return;
            }


            this.obj.details = c;
            if(typeof fun === 'function') {
                fun();
            }
        },

        /**
         * 保存
         */
        saveProject: function() {
            let this_ = this;
            this.validate(function () {
                let formData = new FormData();
                let path = '/project/add';
                if(this_.projectId) {
                    path = '/project/update';
                }
                this_.obj.state = 1;
                this_.obj.startRecordTime = '';
                this_.obj.endRecordTime = '';
                Object.keys(this_.obj).forEach((key) => {
                    formData.append(key, this_.obj[key]);
                });
                this_.loading = layer.load(2, {
                    shade: [0.9,'#bfbfbf'] //0.1透明度的白色背景
                });

                $.ajax({
                    url: path,
                    type: 'POST',
                    cache: false,
                    data: formData,
                    processData: false,
                    contentType: false
                }).done(function(res) {
                    parent.layer.alert('申请发布招募成功', {
                        skin: 'layui-layer-molv' //样式类名
                    }, function(index){
                        parent.Project.search();
                        layer.close(this_.loading);
                        parent.layer.close(index);
                        parent.layer.close(window.parent.Project.layerIndex);
                    });
                }).fail(function(res) {
                    layer.close(this.loading);
                });
            })
        },

        /**
         * 关闭窗口
         */
        closeWindow: function() {
            parent.layer.close(window.parent.Project.layerIndex);
        },

        /**
         * 加载下拉框
         * @param fun 回调方法（可选）
         */
        loadSelect: function (fun) {
            let this_ = this;
            window.vueUtils.ajax({
                codes: this.search_article.join(',')
            }, "/dict/selectByCodes", function (data) {
                for (let obj of data) {
                    this_.selectNames[obj.pidCode + '-' + obj.code] = obj.name;
                    if (obj.pidCode === "way_of_restitution" ) {
                        this_.select_way_of_restitution.push(obj);
                    }
                }
                if (typeof fun === "function") {
                    fun();
                }
            })
        },

        /**
         * 获取项目编号
         */
        getMaxNumber: function() {
            let this_ = this;
            if(!this_.obj.number) {
                window.vueUtils.ajax( null, "/project/getMaxNumber", function (data) {
                    this_.obj.number = data;
                })
            }
        },

        /**
         * 初始化 富文本
         */
        initEdit: function () {
            CKEDITOR.replace( 'description', {
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
         * 初始化自动补全
         */
        initSuggest: function () {
            let this_ = this;
            $("#testNoBtn").bsSuggest({
                url: "/supplier/searchSupplier?search=",
                effectiveFields: this.search_a.split(","), // 有效显示于列表中的字段，非有效字段都会过滤，默认全部有效。
                searchFields: this.search_c.split(","), // 有效搜索字段，从前端搜索过滤数据时使用，但不一定显示在列表中。effectiveFields 配置字段也会用于搜索过滤
                effectiveFieldsAlias:{detail: "供应商信息"},// 有效字段的别名对象，用于 header 的显示
                getDataMethod: 'url',
                showBtn: false,
                idField: "id",
                keyField: "name",
                allowNoKeyword: false
            }).on('onDataRequestSuccess', function (e, result) {
                // console.log('onDataRequestSuccess: ', result);

                // 将检索出的所有列表保存
                for(let o of result.value) {
                    this_.supplierDetail[o.id] = o;
                }
            }).on('onSetSelectValue', function (e, keyword) {
                // console.log('onSetSelectValue: ', keyword);
                // 获取用户选择的保存
                this_.supplier = this_.supplierDetail[keyword.id];
                this_.quota = this_.supplier.loanLimit - (this_.supplier.usedLoanAmount || 0);
                this_.obj.supplierId = this_.supplier.id;
            }).on('onUnsetSelectValue', function (e) {
                console.log("onUnsetSelectValue");
            });
        },

        /**
         * 时间组件初始化
         */
        initDateTime: function () {
            let this_ = this;
            laydate.render({
                elem: '#startTime',
                type: 'date',
                done: function (value, date, endDate) {
                    $("#startTime").val(value);
                    this_.obj.startTime = value;
                }
            });
            laydate.render({
                elem: '#endTime',
                type: 'date',
                done: function (value, date, endDate) {
                    $("#endTime").val(value);
                    this_.obj.endTime = value;
                }
            });
        },

        /**
         * 初始化数据
         */
        initData: function (fun) {
            let projectId = $('#projectId').val();
            let this_ = this;
            if(projectId) {
                this.projectId = projectId;
                this.page_type = $('#page_type').val();
                window.vueUtils.ajax( null, "/project/detail/" + projectId, function (data) {
                    this_.obj = data;
                    this_.copyObj = data;
                    this_.updateData();
                    if(typeof fun === 'function') {
                        fun();
                    }
                })
            }else {
                this_.getMaxNumber();
            }

        },

        /**
         * 更新部分组件数据
         */
        updateData: function() {
            $('#startTime').val(this.obj.startTime.substring(0, 10));
            $('#endTime').val(this.obj.endTime.substring(0, 10));
            CKEDITOR.instances.description.setData(this.obj.details);
            this.showExamine();
        },

        /**
         * 更新编辑状态
         * @param b
         */
        updateDisabled: function () {
            this.disabled = !this.disabled;
            if(this.disabled) {
                this.saveProject();
            }
        },

        /**
         * 获取审核记录列表
         */
        showExamine: function (fun) {
            let this_ = this;
            window.vueUtils.ajax( null, "/examine/showExamineByProjectId/" + this.projectId, function (data) {
                this_.list_examine = data;
                if(typeof fun === 'function') {
                    fun();
                }
            })
        },

        /**
         * 审核状态转换
         * @param s
         * @returns {string}
         */
        updateStateName: function (s) {
            return this.select_state[s];
        },

        /**
         * 更新状态
         */
        updateState: function (s, c) {
            let this_ = this;
            this_.loading = layer.load(2, {
                shade: [0.9,'#bfbfbf'] //0.1透明度的白色背景
            });

            let pro = this.obj;
            pro.state = s;
            pro.content = c || '';
            pro.startRecordTime = '';
            pro.endRecordTime = '';
            window.vueUtils.ajax(
                pro
                , "/project/update/", function (data) {
                parent.layer.alert('招募状态变更成功', {
                    skin: 'layui-layer-molv' //样式类名
                }, function(index){
                    parent.Project.search();
                    layer.close(this_.loading);
                    parent.layer.close(index);
                    parent.layer.close(window.parent.Project.layerIndex);
                });
            })
        },

        /**
         * 获取父页面的状态转换对应的名称
         */
        initStateName: function(fun) {
            if(parent.window.project_vue) {
                for(let obj of parent.window.project_vue.select_state) {
                    this.select_state[obj.code] = obj.name;
                }
            }
            if(typeof fun === 'function') {
                fun();
            }
        },

        /**
         * 按钮点击触发事件
         * @param f
         */
        btnClick: function (s) {
            let this_ = this;
            if(s == 2) {
                Feng.confirm("是否确认通过此条招募信息？ ", function () {
                    this_.updateState(s);
                });
            } else if(s == 3) {
                $('#reasonModal').modal('show');
            } else {
                Feng.confirm("是否确认变更为【"+ this.select_state[s]+"】？ ", function () {
                    this_.updateState(s);
                });
            }
        },

        /**
         * 获取供应商信息
         */
        initSupplier: function () {
            let this_ = this;
            window.vueUtils.ajax( null, "/supplier/detail/" + this.obj.supplierId, function (data) {
                this_.supplier = data;
                this_.quota = this_.supplier.loanLimit - (this_.supplier.usedLoanAmount || 0);
            })
        },

        /**
         * 获取加盟信息
         */
        initJoinin: function () {
            let this_ = this;
            window.vueUtils.ajax( null, "/joinin/detail_project/" + this.obj.id, function (data) {
                console.log(data)
                this_.list_joinin = data;
            })
        },

        /**
         * 测试AntVue
         */
        testAntVue: function (aa) {
            alert(aa);
            console.log("----------------------AntVue--------------------------")
            console.log(this)
        }
    },
    mounted: function () { // 页面第一次加载时执行
        let this_ = this;
        this.$nextTick(() => {
            this_.loadSelect(function () {
                this_.initStateName(function () {
                    this_.initSuggest();
                    this_.initEdit();
                    this_.initDateTime();
                    this_.initData(function () {
                        this_.initSupplier();
                        this_.initJoinin();
                    });
                });
            });
        });
    },
    updated: function () { // Vue组件页面变化时执行
        this.$nextTick(() => {

        });
    }
});