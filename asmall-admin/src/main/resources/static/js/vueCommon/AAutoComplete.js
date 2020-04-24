/**
 * 搜索框构造器
 * 注：接收参数只能用小写
 */
Vue.component('my-a-auto-complete', {
    props: ['datasource'],
    methods: {
        handleSearch: function(value) {
            this.$emit('search', value);
        },
        onSelect(value) {
            this.$emit('select', value);
        },
    },
    template: `
        <a-auto-complete
               v-bind:dataSource="datasource"
               style="width: 100%"
               v-on:select="onSelect"
               v-on:search="handleSearch"
               placeholder="输入供应商名称搜索（模糊搜索）"
       />
    `,
});