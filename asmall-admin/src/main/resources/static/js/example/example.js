window.example_vue = new Vue({
    el: '#example_vue',
    data: {

    },
    methods: {

    },
    mounted: function () { // 页面第一次加载时执行
        this.$nextTick(() => {

        });
    },
    updated: function () { // Vue组件页面变化时执行
        this.$nextTick(() => {

        });
    }
});