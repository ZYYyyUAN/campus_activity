<template>
  <el-container class="main-container">
    <el-header class="header">
      <div class="header-left">
        <h2>校园活动报名管理系统</h2>
      </div>
      <div class="header-right">
        <el-dropdown @command="handleCommand">
          <span class="user-info">
            <i class="el-icon-user"></i>
            {{ user.realName || user.username }}
            <i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>
    <el-container>
      <el-aside width="200px" class="sidebar">
        <el-menu
          :default-active="activeMenu"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
        >
          <el-menu-item index="/dashboard">
            <i class="el-icon-data-line"></i>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/activity">
            <i class="el-icon-tickets"></i>
            <span>活动管理</span>
          </el-menu-item>
          <el-menu-item index="/user" v-if="isAdmin">
            <i class="el-icon-user"></i>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/log" v-if="isAdmin">
            <i class="el-icon-document"></i>
            <span>日志管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main class="main-content">
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'MainLayout',
  computed: {
    user() {
      return this.$store.state.user || {}
    },
    isAdmin() {
      return this.$store.getters.isAdmin
    },
    activeMenu() {
      return this.$route.path
    }
  },
  methods: {
    handleCommand(command) {
      if (command === 'logout') {
        this.$confirm('确定要退出登录吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$store.dispatch('logout')
          this.$message.success('已退出登录')
          this.$router.push('/login')
        })
      }
    }
  }
}
</script>

<style scoped>
.main-container {
  height: 100vh;
}

.header {
  background: #304156;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.header-left h2 {
  margin: 0;
  font-size: 20px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.sidebar {
  background: #304156;
}

.main-content {
  background: #f0f2f5;
  padding: 20px;
}
</style>

