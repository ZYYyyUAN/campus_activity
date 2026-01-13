<template>
  <div class="activity-detail">
    <el-card>
      <div slot="header">
        <el-page-header @back="goBack" content="活动详情"></el-page-header>
      </div>

      <!-- 活动基本信息 -->
      <div v-if="activity" class="detail-section">
        <h3>活动信息</h3>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="活动名称">{{ activity.activityName }}</el-descriptions-item>
          <el-descriptions-item label="活动类型">{{ activity.activityType }}</el-descriptions-item>
          <el-descriptions-item label="活动地点">{{ activity.location }}</el-descriptions-item>
          <el-descriptions-item label="活动状态">
            <el-tag :type="activity.status === '报名中' ? 'success' : 'info'">
              {{ activity.status }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="开始时间">{{ formatDateTime(activity.startTime) }}</el-descriptions-item>
          <el-descriptions-item label="结束时间">{{ formatDateTime(activity.endTime) }}</el-descriptions-item>
          <el-descriptions-item label="人数上限">{{ activity.maxPeople }}</el-descriptions-item>
          <el-descriptions-item label="发布者">{{ activity.publisherName }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 参与统计 -->
      <div class="detail-section">
        <h3>参与统计</h3>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-card shadow="hover">
              <div class="stat-card">
                <div class="stat-label">报名人数</div>
                <div class="stat-value">{{ registrationCount }}</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover">
              <div class="stat-card">
                <div class="stat-label">签到人数</div>
                <div class="stat-value">{{ signCount }}</div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card shadow="hover">
              <div class="stat-card">
                <div class="stat-label">签到率</div>
                <div class="stat-value">{{ signRate }}%</div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 报名列表 -->
      <div class="detail-section">
        <h3>报名列表</h3>
        <el-table :data="registrationList" border v-loading="loading">
          <el-table-column type="index" label="序号" width="60"></el-table-column>
          <el-table-column prop="realName" label="学生姓名" width="120"></el-table-column>
          <el-table-column prop="userName" label="用户名" width="150"></el-table-column>
          <el-table-column prop="registerTime" label="报名时间" width="180">
            <template slot-scope="scope">
              {{ formatDateTime(scope.row.registerTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="auditStatus" label="审核状态" width="120">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.auditStatus)">
                {{ scope.row.auditStatus }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="是否签到" width="120">
            <template slot-scope="scope">
              <el-tag :type="scope.row.hasSigned ? 'success' : 'info'">
                {{ scope.row.hasSigned ? '已签到' : '未签到' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 签到列表 -->
      <div class="detail-section">
        <h3>签到记录</h3>
        <el-table :data="signList" border v-loading="loading">
          <el-table-column type="index" label="序号" width="60"></el-table-column>
          <el-table-column prop="realName" label="学生姓名" width="120"></el-table-column>
          <el-table-column prop="userName" label="用户名" width="150"></el-table-column>
          <el-table-column prop="signTime" label="签到时间" width="180">
            <template slot-scope="scope">
              {{ formatDateTime(scope.row.signTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="score" label="获得积分" width="120">
            <template slot-scope="scope">
              <el-tag type="success">+{{ scope.row.score }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 评价列表 -->
      <div class="detail-section">
        <h3>活动评价</h3>
        <el-table :data="evaluationList" border v-loading="loading">
          <el-table-column type="index" label="序号" width="60"></el-table-column>
          <el-table-column prop="realName" label="学生姓名" width="120"></el-table-column>
          <el-table-column prop="rating" label="评分" width="150">
            <template slot-scope="scope">
              <el-rate v-model="scope.row.rating" disabled></el-rate>
            </template>
          </el-table-column>
          <el-table-column prop="content" label="评价内容" min-width="200"></el-table-column>
          <el-table-column prop="evalTime" label="评价时间" width="180">
            <template slot-scope="scope">
              {{ formatDateTime(scope.row.evalTime) }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script>
import { activityApi, registrationApi, signScoreApi, evaluationApi } from '@/api'

export default {
  name: 'ActivityDetail',
  data() {
    return {
      activityId: null,
      activity: null,
      registrationList: [],
      signList: [],
      evaluationList: [],
      registrationCount: 0,
      signCount: 0,
      loading: false
    }
  },
  computed: {
    signRate() {
      if (this.registrationCount === 0) return 0
      return ((this.signCount / this.registrationCount) * 100).toFixed(1)
    }
  },
  mounted() {
    this.activityId = parseInt(this.$route.params.id)
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        // 加载活动信息
        await this.loadActivity()
        // 先加载签到列表
        await this.loadSigns()
        // 再加载报名列表（需要用到签到列表数据）
        await this.loadRegistrations()
        // 加载评价列表
        await this.loadEvaluations()
      } catch (error) {
        console.error('加载数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    async loadActivity() {
      const res = await activityApi.getActivityById(this.activityId)
      if (res.code === 200) {
        this.activity = res.data
      }
    },
    async loadRegistrations() {
      const res = await registrationApi.getRegistrationByActivity(this.activityId)
      if (res.code === 200) {
        this.registrationList = res.data || []
        this.registrationCount = this.registrationList.length
        
        // 为每个报名记录添加是否签到的标识，使用$set确保响应式
        this.registrationList.forEach(reg => {
          const hasSigned = this.signList.some(sign => 
            sign.userId != null && reg.userId != null && 
            Number(sign.userId) === Number(reg.userId)
          )
          this.$set(reg, 'hasSigned', hasSigned)
        })
      }
    },
    async loadSigns() {
      const res = await signScoreApi.getSignListByActivity(this.activityId)
      if (res.code === 200) {
        this.signList = res.data || []
        this.signCount = this.signList.length
      }
    },
    async loadEvaluations() {
      const res = await evaluationApi.getEvaluationList(this.activityId)
      if (res.code === 200) {
        this.evaluationList = res.data || []
      }
    },
    goBack() {
      this.$router.go(-1)
    },
    getStatusType(status) {
      const map = {
        '待审': 'warning',
        '通过': 'success',
        '拒绝': 'danger'
      }
      return map[status] || 'info'
    },
    formatDateTime(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      return date.toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.activity-detail {
  padding: 20px;
}

.detail-section {
  margin-bottom: 30px;
}

.detail-section h3 {
  margin-bottom: 15px;
  color: #303133;
  border-left: 4px solid #409EFF;
  padding-left: 10px;
}

.stat-card {
  text-align: center;
  padding: 20px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;
}
</style>
