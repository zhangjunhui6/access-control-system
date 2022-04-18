// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
/* 引入ElementUI */
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
/* 引入store */
import store from './store'
/* 引入Echarts */
import * as echarts from 'echarts'


/* 引入axios，设置反向代理，前端默认发送到 http://localhost:8443/api */
var axios = require('axios');
axios.defaults.baseURL = 'http://localhost:8086/api';
// 全局注册,之后其他组件可以通过this.$axios发送数据
Vue.prototype.$axios = axios;
Vue.config.productionTip = false;

/* 使用ElementUI */
Vue.use(ElementUI)

/* 使用Echarts*/
Vue.prototype.$echarts = echarts;

/* 阻止Vue启动时生成生产提示 */
Vue.config.productionTip = false

/* 访问每个路由前调用钩子函数 */
router.beforeEach((to, from, next) => {
    // 判断是否需要超级管理员或管理员才能进入
    if (to.meta.requireAuth === "admin") {
        if (store.state.username === "admin"){
            next()
        } else {
            next({
                path: 'login',
                query: {redirect: to.fullPath}
            })
        }
    } else if (to.meta.requireAuth === "otherAdmin") {
        if (store.state.username !== "" && store.state.username !== "admin"){
            next()
        } else {
            next({
                path: 'login',
                query: {redirect: to.fullPath}
            })
        }
    } else {
        next()
    }
})

/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    store,
    components: {App},
    template: '<App/>'
})
