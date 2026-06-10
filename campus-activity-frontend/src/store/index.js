import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const savedUser = sessionStorage.getItem('user')

export default new Vuex.Store({
  state: {
    user: savedUser ? JSON.parse(savedUser) : null,
    token: sessionStorage.getItem('token') || ''
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user
      if (user) {
        sessionStorage.setItem('user', JSON.stringify(user))
      }
    },
    SET_TOKEN(state, token) {
      state.token = token
      if (token) {
        sessionStorage.setItem('token', token)
      }
    },
    CLEAR_USER(state) {
      state.user = null
      state.token = ''
      sessionStorage.removeItem('user')
      sessionStorage.removeItem('token')
    }
  },
  actions: {
    login({ commit }, { user, token }) {
      commit('SET_USER', user)
      commit('SET_TOKEN', token)
    },
    logout({ commit }) {
      commit('CLEAR_USER')
    }
  },
  getters: {
    isAdmin: state => state.user && state.user.role === 'admin',
    isPublisher: state => state.user && state.user.role === 'publisher',
    isStudent: state => state.user && state.user.role === 'student'
  }
})
