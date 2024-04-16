import { createRouter, createWebHistory } from 'vue-router'
import App from '@/App.vue'
import SitesManage from '@/views/SitesManage.vue'
import TaskManage from '@/views/TaskManage.vue'
import UserManage from '@/views/UserManage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: App
    },
    {
        path: '/SitesManage',
        name: 'SitesManage',
        component: SitesManage
    },
    {
      path: '/TaskManage',
      name: 'TaskManage',
      component: TaskManage
    },
    {
      path: '/UserManage',
      name: 'UserManage',
      component: UserManage
    }
  ]
})

export default router
