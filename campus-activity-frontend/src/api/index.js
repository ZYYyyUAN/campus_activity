import axios from 'axios'

// 用户相关API
export const userApi = {
  login(data) {
    return axios.post('/user/login', data)
  },
  getUserById(userId) {
    return axios.get(`/user/${userId}`)
  },
  getUserList() {
    return axios.get('/user/list')
  },
  getUserByRole(role) {
    return axios.get(`/user/role/${role}`)
  },
  addUser(data) {
    return axios.post('/user/add', data)
  },
  updateUser(data) {
    return axios.put('/user/update', data)
  },
  deleteUser(userId) {
    return axios.delete(`/user/${userId}`)
  },
  changePassword(data) {
    return axios.post('/user/changePassword', data)
  },
  resetPassword(userId) {
    return axios.post(`/user/resetPassword/${userId}`)
  },
  updateStatus(data) {
    return axios.put('/user/status', data)
  }
}

// 活动相关API
export const activityApi = {
  getActivityById(activityId) {
    return axios.get(`/activity/${activityId}`)
  },
  getActivityList() {
    return axios.get('/activity/list')
  },
  getActivityByPublisher(publisherId) {
    return axios.get(`/activity/publisher/${publisherId}`)
  },
  queryActivity(params) {
    return axios.get('/activity/query', { params })
  },
  addActivity(data) {
    return axios.post('/activity/add', data)
  },
  updateActivity(data) {
    return axios.put('/activity/update', data)
  },
  deleteActivity(activityId) {
    return axios.delete(`/activity/${activityId}`)
  },
  checkActivityFull(activityId) {
    return axios.get(`/activity/checkFull/${activityId}`)
  }
}

// 日志相关API
export const logApi = {
  getLogById(logId) {
    return axios.get(`/log/${logId}`)
  },
  getLogList() {
    return axios.get('/log/list')
  },
  getLogByUserId(userId) {
    return axios.get(`/log/user/${userId}`)
  },
  getLogByOperationType(operationType) {
    return axios.get(`/log/type/${operationType}`)
  },
  queryLog(params) {
    return axios.get('/log/query', { params })
  },
  deleteLog(logId) {
    return axios.delete(`/log/${logId}`)
  },
  cleanExpiredLogs(date) {
    return axios.delete('/log/clean', { params: { date } })
  }
}

