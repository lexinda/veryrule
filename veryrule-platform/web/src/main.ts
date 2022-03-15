import {createApp} from 'vue'
import ElementPlus from 'element-plus'  //引入插件
import 'element-plus/theme-chalk/index.css' //默认css样式
import zhCn from 'element-plus/es/locale/lang/zh-cn'   //引入中文包
import router from './router/index.ts'

import App from './App.vue'

const app = createApp(App);

app.use(router)

app.use(ElementPlus,{
  locale: zhCn,
});

import JsonViewer from "vue3-json-viewer";
app.use(JsonViewer);
// axios
import axios from 'axios';
// axios.defaults.baseURL = config.api_url; // 设置了主域名则接口就不需要+了
axios.defaults.withCredentials = false; // 跨域设置，false忽略跨域cookies（Access-Control-Allow-Headers:*）
axios.defaults.timeout = 16000; // 等待时间，ms
axios.defaults.headers={"Content-type": "application/x-www-form-urlencoded; charset=UTF-8"}

app.mount("#app");