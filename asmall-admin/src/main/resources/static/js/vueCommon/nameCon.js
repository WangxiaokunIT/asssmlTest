/**
 * 文本框
 */
Vue.component('name-con', {
    model: {
        prop: 'value',
        event: 'input'
    },
    props: ['value', 'name', 'readonly', 'id', 'placeholder', 'disabled'],
    template: `
         <div class="input-group">
            <div class="input-group-btn">
                <button data-toggle="dropdown" class="btn btn-white dropdown-toggle"
                        type="button">{{ name }}
                </button>
            </div>
            <input type="text" class="form-control" v-bind:value="value"  v-bind:id="id" v-bind:name="id" v-bind:readonly="!readonly ? false : 'readonly'" 
            v-bind:disabled="!disabled ? false : 'disabled'" v-bind:placeholder="placeholder" v-on:input="$emit('input', $event.target.value)" />
        </div>
    `
})
