<template>
  <div class="my-sign-score">
    <el-card>
      <div slot="header">
        <span>我的签到与积分</span>
      </div>
      
      <div class="score-summary">
        <el-card class="summary-card">
          <div class="summary-content">
            <div class="summary-label">总积分</div>
            <div class="summary-value">{{ totalScore }}</div>
          </div>
        </el-card>
      </div>
      
      <el-table :data="signScoreList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="activityName" label="活动名称" min-width="150"></el-table-column>
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
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleEvaluate(scope.row)">评价活动</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { signScoreApi } from '@/api'

export default {
  name: 'MySignScore',
  data() {
    return {
      signScoreList: [],
      totalScore: 0,
      loading: false
    }
  },
  mounted() {
    this.loadSignScoreList()
    this.loadTotalScore()
  },
  methods: {
    async loadSignScoreList() {
      this.loading = true
      try {
        const userId = this.$store.state.user.userId
        const res = await signScoreApi.getSignList(userId)
        if (res.code === 200) {
          this.signScoreList = res.data || []
        }
      } catch (error) {
        console.error('加载签到列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    async loadTotalScore() {
      try {
        const userId = this.$store.state.user.userId
        const res = await signScoreApi.getTotalScore(userId)
        if (res.code === 200) {
          this.totalScore = res.data || 0
        }
      } catch (error) {
        console.error('加载总积分失败:', error)
      }
    },
    handleEvaluate(row) {
      // 跳转到评价页面
      this.$router.push(`/student/evaluation/${row.activityId}`)
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
.my-sign-score {
  padding: 20px;
}

.score-summary {
  margin-bottom: 20px;
}

.summary-card {
  max-width: 300px;
}

.summary-content {
  text-align: center;
}

.summary-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.summary-value {
  font-size: 36px;
  font-weight: bold;
  color: #409EFF;
}
</style>
