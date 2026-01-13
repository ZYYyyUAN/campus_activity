<template>
  <div class="activity-statistics">
    <el-card>
      <div slot="header">
        <span>活动参与情况</span>
      </div>
      
      <el-table :data="activityList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="activityName" label="活动名称" min-width="150"></el-table-column>
        <el-table-column prop="activityType" label="活动类型" width="120"></el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="180">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="maxPeople" label="人数上限" width="100"></el-table-column>
        <el-table-column label="报名人数" width="120">
          <template slot-scope="scope">
            {{ getRegistrationCount(scope.row.activityId) }}
          </template>
        </el-table-column>
        <el-table-column label="签到人数" width="120">
          <template slot-scope="scope">
            {{ getSignCount(scope.row.activityId) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleViewDetail(scope.row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { activityApi, registrationApi, signScoreApi } from '@/api'

export default {
  name: 'ActivityStatistics',
  data() {
    return {
      activityList: [],
      registrationCountMap: {},
      signCountMap: {},
      loading: false
    }
  },
  computed: {
    isAdmin() {
      return this.$store.getters.isAdmin
    }
  },
  mounted() {
    this.loadActivityList()
  },
  methods: {
    async loadActivityList() {
      this.loading = true
      try {
        let res
        if (this.isAdmin) {
          // 管理员可以查看所有活动
          res = await activityApi.getActivityList()
        } else {
          // 发布者只能查看自己发布的活动
          const publisherId = this.$store.state.user.userId
          res = await activityApi.getActivityByPublisher(publisherId)
        }
        if (res.code === 200) {
          this.activityList = res.data || []
          // 加载每个活动的统计数据
          this.loadStatistics()
        }
      } catch (error) {
        console.error('加载活动列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    async loadStatistics() {
      if (!this.activityList || this.activityList.length === 0) {
        return
      }
      const promises = this.activityList.map(async (activity) => {
        try {
          const [regRes, signRes] = await Promise.all([
            registrationApi.getRegistrationCount(activity.activityId),
            signScoreApi.getSignCount(activity.activityId)
          ])
          if (regRes.code === 200) {
            this.$set(this.registrationCountMap, activity.activityId, regRes.data || 0)
          }
          if (signRes.code === 200) {
            this.$set(this.signCountMap, activity.activityId, signRes.data || 0)
          }
        } catch (error) {
          console.error(`加载活动 ${activity.activityId} 统计数据失败:`, error)
          // 设置默认值
          this.$set(this.registrationCountMap, activity.activityId, 0)
          this.$set(this.signCountMap, activity.activityId, 0)
        }
      })
      await Promise.all(promises)
    },
    getRegistrationCount(activityId) {
      return this.registrationCountMap[activityId] || 0
    },
    getSignCount(activityId) {
      return this.signCountMap[activityId] || 0
    },
    handleViewDetail(row) {
      // 查看活动详情
      this.$router.push(`/activity/${row.activityId}`)
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
.activity-statistics {
  padding: 20px;
}
</style>
