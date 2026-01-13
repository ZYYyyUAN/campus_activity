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

// 报名相关API
export const registrationApi = {
  getRegistrationById(registerId) {
    return axios.get(`/registration/${registerId}`)
  },
  getRegistrationList(userId) {
    return axios.get(`/registration/user/${userId}`)
  },
  getRegistrationByActivity(activityId) {
    return axios.get(`/registration/activity/${activityId}`)
  },
  getRegistrationCount(activityId) {
    return axios.get(`/registration/activity/${activityId}/count`)
  },
  getPassedRegistrationCount(activityId) {
    return axios.get(`/registration/activity/${activityId}/count/passed`)
  },
  getRegistrationByPublisher(publisherId) {
    return axios.get(`/registration/publisher/${publisherId}`)
  },
  getAllRegistrations() {
    return axios.get('/registration/all')
  },
  registerActivity(data) {
    return axios.post('/registration/add', data)
  },
  cancelRegistration(registerId) {
    return axios.delete(`/registration/${registerId}`)
  }
}

// 审核相关API
export const auditApi = {
  getAuditById(auditId) {
    return axios.get(`/audit/${auditId}`)
  },
  getAuditList(activityId) {
    return axios.get(`/audit/activity/${activityId}`)
  },
  getAuditByAuditor(auditorId) {
    return axios.get(`/audit/auditor/${auditorId}`)
  },
  auditRegistration(data) {
    return axios.post('/audit/add', data)
  }
}

// 签到积分相关API
export const signScoreApi = {
  getSignScoreById(signId) {
    return axios.get(`/signscore/${signId}`)
  },
  getSignList(userId) {
    return axios.get(`/signscore/user/${userId}`)
  },
  getSignListByActivity(activityId) {
    return axios.get(`/signscore/activity/${activityId}`)
  },
  getSignCount(activityId) {
    return axios.get(`/signscore/activity/${activityId}/count`)
  },
  signActivity(data) {
    return axios.post('/signscore/sign', data)
  },
  getTotalScore(userId) {
    return axios.get(`/signscore/total/${userId}`)
  }
}

// 评价相关API
export const evaluationApi = {
  getEvaluationById(evalId) {
    return axios.get(`/evaluation/${evalId}`)
  },
  getAllEvaluations() {
    return axios.get('/evaluation/all')
  },
  getEvaluationList(activityId) {
    return axios.get(`/evaluation/activity/${activityId}`)
  },
  getEvaluationByUser(userId) {
    return axios.get(`/evaluation/user/${userId}`)
  },
  getEvaluationByKeyword(keyword) {
    return axios.get(`/evaluation/keyword/${keyword}`)
  },
  addEvaluation(data) {
    return axios.post('/evaluation/add', data)
  },
  updateEvaluation(data) {
    return axios.put('/evaluation/update', data)
  },
  deleteEvaluation(evalId) {
    return axios.delete(`/evaluation/${evalId}`)
  }
}
