<template>
  <div class="my-registration">
    <el-card>
      <div slot="header">
        <span>我的报名</span>
      </div>
      
      <el-table :data="registrationList" border style="width: 100%" v-loading="loading">
        <el-table-column prop="activityName" label="活动名称" min-width="150"></el-table-column>
        <el-table-column prop="activityType" label="活动类型" width="100"></el-table-column>
        <el-table-column prop="location" label="活动地点" width="120"></el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="180">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="registerTime" label="报名时间" width="180">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.registerTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="auditStatus" label="审核状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.auditStatus)">
              {{ scope.row.auditStatus || '待审' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="签到状态" min-width="120">
          <template slot-scope="scope">
            <el-tag :type="scope.row.hasSigned ? 'success' : 'info'">
              {{ scope.row.hasSigned ? '已签到' : '未签到' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="评价状态" min-width="120">
          <template slot-scope="scope">
            <el-tag :type="scope.row.hasEvaluated ? 'success' : 'info'">
              {{ scope.row.hasEvaluated ? '已评价' : '未评价' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="260" fixed="right">
          <template slot-scope="scope">
            <!-- 待审状态：可以取消报名 -->
            <el-button 
              v-if="scope.row.auditStatus === '待审' || !scope.row.auditStatus"
              size="mini" 
              type="danger"
              @click="handleCancel(scope.row)">取消报名</el-button>
            
            <!-- 审核通过且未签到：显示签到按钮 -->
            <el-button 
              v-if="scope.row.auditStatus === '通过' && !scope.row.hasSigned"
              size="mini" 
              type="primary"
              @click="handleSign(scope.row)">签到</el-button>
            
            <!-- 已签到且未评价：显示评价按钮 -->
            <el-button 
              v-if="scope.row.hasSigned && !scope.row.hasEvaluated"
              size="mini" 
              type="success"
              @click="handleEvaluate(scope.row)">评价活动</el-button>
            
            <!-- 已评价：显示标签 -->
            <el-tag v-if="scope.row.hasEvaluated" type="success" size="small">已评价</el-tag>
            
            <!-- 审核拒绝：显示原因（如果有） -->
            <span v-if="scope.row.auditStatus === '拒绝'" style="color: #909399; font-size: 12px;">
              已拒绝
            </span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { registrationApi, signScoreApi, evaluationApi } from '@/api'

export default {
  name: 'MyRegistration',
  data() {
    return {
      registrationList: [],
      loading: false
    }
  },
  mounted() {
    this.loadRegistrationList()
  },
  methods: {
    async loadRegistrationList() {
      this.loading = true
      try {
        const userId = this.$store.state.user.userId
        
        // 加载报名列表
        const res = await registrationApi.getRegistrationList(userId)
        if (res.code === 200) {
          this.registrationList = res.data || []
          
          // 为每个报名记录加载活动详情、签到状态和评价状态
          await this.loadAdditionalInfo()
        }
      } catch (error) {
        console.error('加载报名列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    
    async loadAdditionalInfo() {
      const userId = this.$store.state.user.userId
      
      // 批量加载所有活动的签到和评价状态
      const promises = this.registrationList.map(async (registration) => {
        try {
          // 检查是否已签到
          const signRes = await signScoreApi.getSignListByActivity(registration.activityId)
          if (signRes.code === 200) {
            const signList = signRes.data || []
            const hasSigned = signList.some(sign => 
              sign.userId != null && userId != null && 
              Number(sign.userId) === Number(userId)
            )
            // 使用$set确保响应式
            this.$set(registration, 'hasSigned', hasSigned)
          } else {
            this.$set(registration, 'hasSigned', false)
          }
          
          // 检查是否已评价
          const evalRes = await evaluationApi.getEvaluationList(registration.activityId)
          if (evalRes.code === 200) {
            const evalList = evalRes.data || []
            const hasEvaluated = evalList.some(evaluation => 
              evaluation.userId != null && userId != null && 
              Number(evaluation.userId) === Number(userId)
            )
            // 使用$set确保响应式
            this.$set(registration, 'hasEvaluated', hasEvaluated)
          } else {
            this.$set(registration, 'hasEvaluated', false)
          }
        } catch (error) {
          console.error(`加载活动 ${registration.activityId} 附加信息失败:`, error)
          this.$set(registration, 'hasSigned', false)
          this.$set(registration, 'hasEvaluated', false)
        }
      })
      
      await Promise.all(promises)
    },
    
    async handleSign(row) {
      try {
        const res = await signScoreApi.signActivity({
          activityId: row.activityId,
          userId: this.$store.state.user.userId,
          score: 10
        })
        if (res.code === 200) {
          this.$message.success('签到成功！获得10积分')
          // 使用$set更新签到状态，确保响应式
          this.$set(row, 'hasSigned', true)
        }
      } catch (error) {
        console.error('签到失败:', error)
        this.$message.error(error.response?.data?.message || '签到失败')
      }
    },
    
    handleEvaluate(row) {
      // 跳转到评价页面
      this.$router.push(`/student/evaluation/${row.activityId}`)
    },
    
    async handleCancel(row) {
      try {
        await this.$confirm('确定要取消报名吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const res = await registrationApi.cancelRegistration(row.registerId)
        if (res.code === 200) {
          this.$message.success('取消报名成功')
          this.loadRegistrationList()
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('取消报名失败:', error)
          this.$message.error('取消报名失败')
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
      return date.toLocaleString('zh-CN')
    }
  }
}
</script>

<style scoped>
.my-registration {
  padding: 20px;
}
</style>
