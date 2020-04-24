/**
 * 字典下拉框
 */
Vue.component('select-con', {
    model: {
        prop: 'value',
        event: 'change'
    },
    props: ['name', 'options', 'value', 'id', 'readonly', 'disabled'],
    template: `
         <div class="input-group">
            <div class="input-group-btn">
                <button data-toggle="dropdown" class="btn btn-white dropdown-toggle" type="button">
                    {{ name }}
                </button>
            </div>
            <select class="form-control" v-bind:id="id" v-bind:name="id" v-on:change="$emit('change', $event.target.value)" 
                v-bind:readonly="!readonly ? false : 'readonly'" v-bind:disabled="!disabled ? false : 'disabled'">
                <option value="">- 请选择 -</option>
                <option v-for="(item,index) in options" v-bind:value="item.code" v-bind:selected="value == item.code">{{ item.name}}</option>
            </select>
        </div>
    `
})
