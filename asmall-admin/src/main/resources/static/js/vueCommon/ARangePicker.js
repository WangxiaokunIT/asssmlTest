/**
 * 时间框封装
 * 注：接收参数只能用小写
 */
Vue.component('my-a-range-picker', {
    props: ['defaultvalue', 'format', 'default'],
    methods: {
        onChange: function (date, dateString) {
            this.$emit('change', date, dateString);
        }
    },
    template: `
        <a-date-picker   v-on:change="onChange" />
    `,
});