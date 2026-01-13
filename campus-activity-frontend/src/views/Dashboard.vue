<template>
  <div class="dashboard">
    <!-- 管理员首页 -->
    <template v-if="isAdmin">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-card>
            <div slot="header">
              <span>快捷入口</span>
            </div>
            <el-row :gutter="20">
              <el-col :span="6">
                <el-card shadow="hover" class="quick-card" @click.native="$router.push('/user')">
                  <div class="quick-icon" style="background: #409EFF;">
                    <i class="el-icon-user"></i>
                  </div>
                  <div class="quick-title">用户管理</div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card shadow="hover" class="quick-card" @click.native="$router.push('/activity')">
                  <div class="quick-icon" style="background: #67C23A;">
                    <i class="el-icon-tickets"></i>
                  </div>
                  <div class="quick-title">活动管理</div>
                </el-card>
              </el-col>
              <el-col :span="6">
                <el-card shadow="hover" class="quick-card" @click.native="$router.push('/log')">
                  <div class="quick-icon" style="background: #E6A23C;">
                    <i class="el-icon-document"></i>
                  </div>
                  <div class="quick-title">操作日志</div>
                </el-card>
              </el-col>
            </el-row>
          </el-card>
        </el-col>
      </el-row>
    </template>

    <!-- 发布者首页 -->
    <template v-if="isPublisher">
      <el-row :gutter="20">
        <el-col :span="24">
          <el-card>
            <div slot="header" class="card-header">
              <span>我发布的活动</span>
              <el-button type="primary" size="small" @click="$router.push('/activity/add')">
                <i class="el-icon-plus"></i> 发布新活动
              </el-button>
            </div>
            <el-table :data="myActivities" border style="width: 100%" v-loading="loading">
              <el-table-column prop="activityName" label="活动名称" min-width="150"></el-table-column>
              <el-table-column prop="activityType" label="活动类型" width="120"></el-table-column>
              <el-table-column prop="startTime" label="开始时间" width="180">
                <template slot-scope="scope">
                  {{ formatDateTime(scope.row.startTime) }}
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="scope.row.status === '报名中' ? 'success' : 'info'">
                    {{ scope.row.status }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200">
                <template slot-scope="scope">
                  <el-button size="mini" @click="$router.push(`/activity/edit/${scope.row.activityId}`)">编辑</el-button>
                  <el-button size="mini" type="primary" @click="$router.push('/publisher/audit')">审核报名</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="8">
          <el-card>
            <div class="stat-card">
              <div class="stat-icon" style="background: #409EFF;">
                <i class="el-icon-tickets"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ myActivities.length }}</div>
                <div class="stat-label">我的活动</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div class="stat-card">
              <div class="stat-icon" style="background: #67C23A;">
                <i class="el-icon-user"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ pendingAuditCount }}</div>
                <div class="stat-label">待审核报名</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div class="stat-card">
              <div class="stat-icon" style="background: #E6A23C;">
                <i class="el-icon-data-line"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ totalRegistrations }}</div>
                <div class="stat-label">总报名数</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </template>

    <!-- 学生首页 -->
    <template v-if="isStudent">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card>
            <div slot="header">
              <span>可报名活动</span>
              <el-button type="text" style="float: right;" @click="$router.push('/student/browse')">查看更多</el-button>
            </div>
            <el-table :data="availableActivities" border style="width: 100%" v-loading="loading">
              <el-table-column prop="activityName" label="活动名称" min-width="150"></el-table-column>
              <el-table-column prop="activityType" label="类型" width="100"></el-table-column>
              <el-table-column prop="startTime" label="开始时间" width="150">
                <template slot-scope="scope">
                  {{ formatDateTime(scope.row.startTime) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100">
                <template slot-scope="scope">
                  <el-button size="mini" type="primary" @click="handleRegister(scope.row)">报名</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card>
            <div slot="header">
              <span>我的报名</span>
              <el-button type="text" style="float: right;" @click="$router.push('/student/registration')">查看更多</el-button>
            </div>
            <el-table :data="myRegistrations" border style="width: 100%" v-loading="loading">
              <el-table-column prop="activityName" label="活动名称" min-width="150"></el-table-column>
              <el-table-column prop="auditStatus" label="状态" width="100">
                <template slot-scope="scope">
                  <el-tag :type="getStatusType(scope.row.auditStatus)">
                    {{ scope.row.auditStatus || '待审' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="100">
                <template slot-scope="scope">
                  <el-button size="mini" type="danger" @click="handleCancel(scope.row)">取消</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="8">
          <el-card>
            <div class="stat-card">
              <div class="stat-icon" style="background: #409EFF;">
                <i class="el-icon-tickets"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ myRegistrations.length }}</div>
                <div class="stat-label">我的报名</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div class="stat-card">
              <div class="stat-icon" style="background: #67C23A;">
                <i class="el-icon-check"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ passedCount }}</div>
                <div class="stat-label">已通过</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <div class="stat-card">
              <div class="stat-icon" style="background: #E6A23C;">
                <i class="el-icon-star-on"></i>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ totalScore }}</div>
                <div class="stat-label">总积分</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </template>
  </div>
</template>

<script>
import { activityApi, registrationApi, signScoreApi } from '@/api'

export default {
  name: 'DashboardPage',
  data() {
    return {
      loading: false,
      // 发布者数据
      myActivities: [],
      pendingAuditCount: 0,
      totalRegistrations: 0,
      // 学生数据
      availableActivities: [],
      myRegistrations: [],
      totalScore: 0
    }
  },
  computed: {
    isAdmin() {
      return this.$store.getters.isAdmin
    },
    isPublisher() {
      return this.$store.getters.isPublisher
    },
    isStudent() {
      return this.$store.getters.isStudent
    },
    passedCount() {
      return this.myRegistrations.filter(r => r.auditStatus === '通过').length
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        if (this.isPublisher) {
          await this.loadPublisherData()
        } else if (this.isStudent) {
          await this.loadStudentData()
        }
      } catch (error) {
        console.error('加载数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    async loadPublisherData() {
      const publisherId = this.$store.state.user.userId
      // 加载我发布的活动
      const activityRes = await activityApi.getActivityByPublisher(publisherId)
      if (activityRes.code === 200) {
        this.myActivities = (activityRes.data || []).slice(0, 5) // 只显示最近5个
      }
      // 加载报名数据统计
      const regRes = await registrationApi.getRegistrationByPublisher(publisherId)
      if (regRes.code === 200) {
        const registrations = regRes.data || []
        this.totalRegistrations = registrations.length
        this.pendingAuditCount = registrations.filter(r => r.auditStatus === '待审' || !r.auditStatus).length
      }
    },
    async loadStudentData() {
      const userId = this.$store.state.user.userId
      // 加载可报名活动（显示最近5个）
      const activityRes = await activityApi.getActivityList()
      if (activityRes.code === 200) {
        const allActivities = activityRes.data || []
        // 过滤出状态为"报名中"的活动
        this.availableActivities = allActivities
          .filter(a => a.status === '报名中')
          .slice(0, 5)
      }
      // 加载我的报名
      const regRes = await registrationApi.getRegistrationList(userId)
      if (regRes.code === 200) {
        this.myRegistrations = (regRes.data || []).slice(0, 5) // 只显示最近5个
      }
      // 加载总积分
      const scoreRes = await signScoreApi.getTotalScore(userId)
      if (scoreRes.code === 200) {
        this.totalScore = scoreRes.data || 0
      }
    },
    async handleRegister(activity) {
      try {
        const userId = this.$store.state.user.userId
        const res = await registrationApi.registerActivity({
          activityId: activity.activityId,
          userId: userId
        })
        if (res.code === 200) {
          this.$message.success('报名成功')
          this.loadStudentData()
        }
      } catch (error) {
        console.error('报名失败:', error)
      }
    },
    async handleCancel(row) {
      try {
        await this.$confirm('确定要取消报名吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const res = await registrationApi.cancelRegistration(row.registerId)
        if (res.code === 200) {
          this.$message.success('取消报名成功')
          this.loadStudentData()
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('取消报名失败:', error)
        }
      }
    },
    getStatusType(status) {
      const statusMap = {
        '通过': 'success',
        '拒绝': 'danger',
        '待审': 'warning'
      }
      return statusMap[status] || 'info'
    },
    formatDateTime(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      })
    }
  }
}
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.quick-card {
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.quick-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.quick-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 30px;
  margin: 0 auto 15px;
}

.quick-title {
  font-size: 16px;
  color: #303133;
}

.stat-card {
  display: flex;
  align-items: center;
}

.stat-icon {
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

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}
</style>
