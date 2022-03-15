import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
	server: {
	　　host: 'localhost',
	　　port: 3000, // 端口号
	　　https: false,
	　　cors: true, // 默认启用并允许任何源
	　　open: false, // 在服务器启动时自动在浏览器中打开应用程序
	　　//反向代理配置，注意rewrite写法，开始没看文档在这里踩了坑
	　　proxy: {
	　　　　'/api': {
	　　　　　　target: 'http://10.211.240.45:8088/veryrule', //代理接口
	　　　　　　changeOrigin: true,
	　　　　　　ws: true,
	　　　　　　secure: true,
	          rewrite:(path) => path.replace(/^\/api/,'')
	　　　　}
	　　}
	},
  plugins: [vue()]
})
