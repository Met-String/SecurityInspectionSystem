import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import Toast from 'vue-toastification'
import 'element-plus/dist/index.css'
import 'vue-toastification/dist/index.css'


const app = createApp(App)
app.use(ElementPlus)
app.use(router)
app.use(Toast)
app.mount('#app')
