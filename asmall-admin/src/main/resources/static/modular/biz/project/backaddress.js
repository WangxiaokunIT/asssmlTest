window.modal_backaddress_vue = new Vue({
    el: "#modal_backaddress_vue",
    data: {
        id: '',
        state: '',
        backSeller: '',
        backSellerPhone: '',
        backAddress: '',
        readonly: false
    },
    methods: {
        /**
         * 保存退货信息
         */
        saveAjax: function() {
            let this_ = this;
            if(!this.backSeller) {
                parent.layer.msg('请填写姓名');
                return;
            }
            if(!this.backSellerPhone) {
                parent.layer.msg('请填写手机号');
                return;
            }
            if(!this.backAddress) {
                parent.layer.msg('请填写退货地址');
                return;
            }
            if(window.order_back_vue)
            {
                window.order_back_vue.updateState({
                    id: this.id,
                    state: this.state,
                    backSeller: this.backSeller,
                    backSellerPhone: this.backSellerPhone,
                    backAddress: this.backAddress
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