<template>
  <div class="registration-audit">
    <el-card>
      <div slot="header">
        <span>报名审核</span>
      </div>
      
      <el-table :data="registrationList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="activityName" label="活动名称" min-width="150"></el-table-column>
        <el-table-column prop="realName" label="报名学生" width="120"></el-table-column>
        <el-table-column prop="registerTime" label="报名时间" width="180">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.registerTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="auditStatus" label="审核状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.auditStatus)">
              {{ scope.row.auditStatus || '待审' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button 
              size="mini" 
              type="success" 
              v-if="scope.row.auditStatus === '待审' || !scope.row.auditStatus"
              @click="handleAudit(scope.row, '通过')">通过</el-button>
            <el-button 
              size="mini" 
              type="danger" 
              v-if="scope.row.auditStatus === '待审' || !scope.row.auditStatus"
              @click="handleAudit(scope.row, '拒绝')">拒绝</el-button>
            <span v-else>已审核</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { registrationApi, auditApi } from '@/api'

export default {
  name: 'RegistrationAudit',
  data() {
    return {
      registrationList: [],
      loading: false
    }
  },
  computed: {
    isAdmin() {
      return this.$store.getters.isAdmin
    }
  },
  mounted() {
    this.loadRegistrationList()
  },
  methods: {
    async loadRegistrationList() {
      this.loading = true
      try {
        let res
        if (this.isAdmin) {
          // 管理员可以查看所有报名
          res = await registrationApi.getAllRegistrations()
        } else {
          // 发布者只能查看自己发布的活动报名
          const publisherId = this.$store.state.user.userId
          res = await registrationApi.getRegistrationByPublisher(publisherId)
        }
        if (res.code === 200) {
          this.registrationList = res.data || []
        }
      } catch (error) {
        console.error('加载报名列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    async handleAudit(row, result) {
      try {
        const res = await auditApi.auditRegistration({
          registerId: row.registerId,
          auditorId: this.$store.state.user.userId,
          auditResult: result === '通过' ? '通过' : '拒绝',
          auditComment: result === '通过' ? '审核通过' : '审核拒绝'
        })
        if (res.code === 200) {
          this.$message.success(`审核${result}成功`)
          this.loadRegistrationList()
        } else {
          this.$message.error(res.message || '审核失败')
        }
      } catch (error) {
        console.error('审核失败:', error)
        this.$message.error('审核失败，请重试')
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
      return date.toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.registration-audit {
  padding: 20px;
}
</style>
