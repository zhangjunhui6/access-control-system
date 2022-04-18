import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

import Login from "../components/Login";
import Home from "../components/Home";
import SystemView from "../components/admin/SystemView";
import RecordsManager from "../components/admin/RecordsManager";
import DeviceManager from "../components/admin/DeviceManager";
import UserManager from "../components/admin/UserManager";
import AdminManager from "../components/admin/AdminManager";
import Manager from "../components/otherAdmin/Manager";

export default new Router({
    routes: [
        {
            path: '/',    /*登录*/
            name: 'Login',
            redirect: '/login',
            component: Login
        },
        {
            path: '/login',    /*登录*/
            name: 'Login',
            component: Login
        },
        {
            path: '/home',      /* 超级管理员 */
            name: 'Home',
            component: Home,
            redirect: '/index',
            children: [
                {
                    path: '/index',
                    name: 'Index',
                    component: SystemView,
                    meta:{
                        requireAuth: "admin",
                        title: "主页"
                    }
                },
                {
                    path: '/access',
                    name: 'Access',
                    component: RecordsManager,
                    meta:{
                        requireAuth: "admin",
                        title: "门禁管理"
                    }
                },
                {
                    path: '/device',
                    name: 'Device',
                    component: DeviceManager,
                    meta:{
                        requireAuth: "admin",
                        title: "设备管理"
                    }
                },
                {
                    path: '/user',
                    name: 'User',
                    component: UserManager,
                    meta:{
                        requireAuth: "admin",
                        title: "用户管理"
                    }
                },
                {
                    path: '/admin',
                    name: 'Admin',
                    component: AdminManager,
                    meta:{
                        requireAuth: "admin",
                        title: "管理员管理"
                    }
                },
            ]
        },
        {
            path: '/manager',    /* 普通管理员 */
            name: 'Manager',
            component: Manager,
            meta:{
                requireAuth: "otherAdmin",
                title: "普通门禁管理"
            }
        },
    ]
})
