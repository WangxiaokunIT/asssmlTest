window.modal_reason_vue = new Vue({
    el: "#modal_reason_vue",
    data: {
        id: '',
        state: '',
        content: '',
        readonly: false
    },
    methods: {
        /**
         * 保存不通过原因
         */
        saveAjax: function() {
            let this_ = this;
            if(!this.content) {
                parent.layer.msg('请填写不通过原因');
                return;
            }
            if(window.project_info_vue) {
                window.project_info_vue.updateState(3, this.content);
            } else if(window.cash_out_vue) {
                window.cash_out_vue.updateState({
                    id: this.id,
                    state: this.state,
                    remarks: this.content
                }, function () {
                    this_.close();
                });
            }
            else if(window.order_back_vue)
            {
                window.order_back_vue.updateState({
                    id: this.id,
                    state: this.state,
                    refuseRemark: this.content
                }, function () {
                    this_.close();
                });
            }

        },

        /**
         * 关闭
         */
        close: function () {
            console.log('---关闭---')
            this.id = '';
            this.state = '';
            this.content = '';
            $('#reasonModal').modal('hide');
        }
    },
    mounted: function () { // 页面第一次加载时执行
        this.$nextTick(() => {

        });
    }
});