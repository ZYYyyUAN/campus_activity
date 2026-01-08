import Vue from 'vue'
import VueRouter from 'vue-router'
import store from '../store'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/layout/MainLayout.vue'),
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue')
      },
      {
        path: 'activity',
        name: 'Activity',
        component: () => import('@/views/activity/ActivityList.vue')
      },
      {
        path: 'activity/add',
        name: 'ActivityAdd',
        component: () => import('@/views/activity/ActivityForm.vue')
      },
      {
        path: 'activity/edit/:id',
        name: 'ActivityEdit',
        component: () => import('@/views/activity/ActivityForm.vue')
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/user/UserList.vue')
      },
      {
        path: 'log',
        name: 'Log',
        component: () => import('@/views/log/LogList.vue')
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const user = store.state.user
  if (to.meta.requiresAuth && !user) {
    next('/login')
  } else if (to.path === '/login' && user) {
    next('/')
  } else {
    next()
  }
})

export default router

