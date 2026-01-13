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
        component: () => import('@/views/Dashboard.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'activity',
        name: 'Activity',
        component: () => import('@/views/activity/ActivityList.vue'),
        meta: { requiresAuth: true, roles: ['管理员', '发布者'] }
      },
      {
        path: 'activity/add',
        name: 'ActivityAdd',
        component: () => import('@/views/activity/ActivityForm.vue'),
        meta: { requiresAuth: true, roles: ['管理员', '发布者'] }
      },
      {
        path: 'activity/edit/:id',
        name: 'ActivityEdit',
        component: () => import('@/views/activity/ActivityForm.vue'),
        meta: { requiresAuth: true, roles: ['管理员', '发布者'] }
      },
      {
        path: 'activity/:id',
        name: 'ActivityDetail',
        component: () => import('@/views/activity/ActivityDetail.vue'),
        meta: { requiresAuth: true, roles: ['管理员', '发布者'] }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('@/views/user/UserList.vue'),
        meta: { requiresAuth: true, roles: ['管理员'] }
      },
      {
        path: 'log',
        name: 'Log',
        component: () => import('@/views/log/LogList.vue'),
        meta: { requiresAuth: true, roles: ['管理员'] }
      },
      {
        path: 'evaluation',
        name: 'EvaluationList',
        component: () => import('@/views/evaluation/EvaluationList.vue'),
        meta: { requiresAuth: true, roles: ['管理员'] }
      },
      // 发布者路由
      {
        path: 'publisher/audit',
        name: 'RegistrationAudit',
        component: () => import('@/views/publisher/RegistrationAudit.vue'),
        meta: { requiresAuth: true, roles: ['发布者', '管理员'] }
      },
      {
        path: 'publisher/statistics',
        name: 'ActivityStatistics',
        component: () => import('@/views/publisher/ActivityStatistics.vue'),
        meta: { requiresAuth: true, roles: ['发布者', '管理员'] }
      },
      // 学生路由
      {
        path: 'student/browse',
        name: 'ActivityBrowse',
        component: () => import('@/views/student/ActivityBrowse.vue'),
        meta: { requiresAuth: true, roles: ['学生'] }
      },
      {
        path: 'student/registration',
        name: 'MyRegistration',
        component: () => import('@/views/student/MyRegistration.vue'),
        meta: { requiresAuth: true, roles: ['学生'] }
      },
      {
        path: 'student/signscore',
        name: 'MySignScore',
        component: () => import('@/views/student/MySignScore.vue'),
        meta: { requiresAuth: true, roles: ['学生'] }
      },
      {
        path: 'student/evaluation/:activityId',
        name: 'EvaluationForm',
        component: () => import('@/views/student/EvaluationForm.vue'),
        meta: { requiresAuth: true, roles: ['学生'] }
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

  const requiresAuth = to.matched.some(record => record.meta && record.meta.requiresAuth)
  if (requiresAuth && !user) {
    next('/login')
    return
  }

  if (to.path === '/login' && user) {
    next('/dashboard')
    return
  }

  const roleRoute = to.matched.find(record => record.meta && record.meta.roles)
  if (roleRoute && user) {
    const userRole = user.role
    if (!roleRoute.meta.roles.includes(userRole)) {
      next('/dashboard')
      return
    }
  }

  next()
})

export default router
