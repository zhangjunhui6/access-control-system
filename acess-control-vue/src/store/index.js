import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        username: window.localStorage.getItem('username') == null ? "" : JSON.parse(window.localStorage.getItem('username')),
        devices: window.localStorage.getItem('devices') == null ? [] : JSON.parse(window.localStorage.getItem('devices')),
        collapse: false,
        tagsList: []
    },
    mutations: {
        login(state, username) {
            state.username = username;
            window.localStorage.setItem('username', JSON.stringify(username));
        },
        setDevices(state, devices) {
            state.devices = devices;
            window.localStorage.setItem("devices", JSON.stringify(devices));
        },
        logout(state) {
            window.localStorage.clear();
            state.username = "";
            state.devices = [];
            state.collapse = false;
            state.tagsList = [];
        },
        handleCollapse(state, data) {
            state.collapse = data;
        },
        /* 删除一个标签 */
        delTagsItem(state, data) {
            state.tagsList.splice(data.index,1);
        },
        /* 添加一个标签 */
        setTagsItem(state, data) {
            state.tagsList.push(data)
        },
        /* 清空 */
        clearTags(state) {
            state.tagsList = []
        },
        /* 关闭其他标签 */
        closeTagsOther(state, data) {
            state.tagsList = data;
        },
        closeCurrentTag(state, data) {
            for (let i = 0, len = state.tagsList.length; i < len; i++) {
                const item = state.tagsList[i];
                if (item.path === data.$route.fullPath) {
                    if (i < len - 1) {
                        data.$router.push(state.tagsList[i + 1].path);
                    } else if (i > 0) {
                        data.$router.push(state.tagsList[i - 1].path);
                    } else {
                        data.$router.push("/");
                    }
                    state.tagsList.splice(i, 1);
                    break;
                }
            }
        }
    }
})
