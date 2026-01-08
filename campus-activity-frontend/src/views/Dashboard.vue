<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="box-card">
          <div class="card-content">
            <div class="card-icon" style="background: #409EFF;">
              <i class="el-icon-tickets"></i>
            </div>
            <div class="card-info">
              <div class="card-value">{{ activityCount }}</div>
              <div class="card-label">活动总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div class="card-content">
            <div class="card-icon" style="background: #67C23A;">
              <i class="el-icon-user"></i>
            </div>
            <div class="card-info">
              <div class="card-value">{{ userCount }}</div>
              <div class="card-label">用户总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div class="card-content">
            <div class="card-icon" style="background: #E6A23C;">
              <i class="el-icon-edit-outline"></i>
            </div>
            <div class="card-info">
              <div class="card-value">{{ logCount }}</div>
              <div class="card-label">操作日志</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div class="card-content">
            <div class="card-icon" style="background: #F56C6C;">
              <i class="el-icon-warning"></i>
            </div>
            <div class="card-info">
              <div class="card-value">系统运行中</div>
              <div class="card-label">系统状态</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { activityApi, userApi, logApi } from '@/api'

export default {
  name: 'Dashboard',
  data() {
    return {
      activityCount: 0,
      userCount: 0,
      logCount: 0
    }
  },
  mounted() {
    this.loadStatistics()
  },
  methods: {
    async loadStatistics() {
      try {
        const [activityRes, userRes, logRes] = await Promise.all([
          activityApi.getActivityList(),
          userApi.getUserList(),
          logApi.getLogList()
        ])
        if (activityRes.code === 200) {
          this.activityCount = activityRes.data ? activityRes.data.length : 0
        }
        if (userRes.code === 200) {
          this.userCount = userRes.data ? userRes.data.length : 0
        }
        if (logRes.code === 200) {
          this.logCount = logRes.data ? logRes.data.length : 0
        }
      } catch (error) {
        console.error('加载统计信息失败:', error)
      }
    }
  }
}
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.box-card {
  margin-bottom: 20px;
}

.card-content {
  display: flex;
  align-items: center;
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 30px;
  margin-right: 15px;
}

.card-info {
  flex: 1;
}

.card-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.card-label {
  font-size: 14px;
  color: #909399;
}
</style>

