<template>
  <div class="activity-list">
    <el-card>
      <div slot="header" class="card-header">
        <span>活动列表</span>
        <el-button 
          type="primary" 
          icon="el-icon-plus" 
          @click="handleAdd"
          v-if="isAdmin || isPublisher">新增活动</el-button>
      </div>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="活动类型">
          <el-select v-model="searchForm.activityType" placeholder="请选择" clearable>
            <el-option label="讲座" value="讲座"></el-option>
            <el-option label="比赛" value="比赛"></el-option>
            <el-option label="学术论坛" value="学术论坛"></el-option>
            <el-option label="班级活动" value="班级活动"></el-option>
            <el-option label="志愿服务" value="志愿服务"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="searchForm.timeRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="yyyy-MM-dd HH:mm:ss">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="活动名称/地点" clearable></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 活动表格 -->
      <el-table :data="activityList" border style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="activityName" label="活动名称" min-width="150"></el-table-column>
        <el-table-column prop="activityType" label="活动类型" width="120"></el-table-column>
        <el-table-column prop="location" label="活动地点" width="150"></el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="180" sortable>
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间" width="180" sortable>
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="maxPeople" label="人数上限" width="100" sortable></el-table-column>
        <el-table-column prop="publisherName" label="发布者" width="120"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === '报名中' ? 'success' : 'info'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button 
              size="mini" 
              @click="handleEdit(scope.row)"
              v-if="isAdmin || (isPublisher && scope.row.publisherId === currentUserId)">编辑</el-button>
            <el-button 
              size="mini" 
              type="danger" 
              @click="handleDelete(scope.row)"
              v-if="isAdmin || (isPublisher && scope.row.publisherId === currentUserId)">删除</el-button>
            <span v-if="isStudent">-</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { activityApi } from '@/api'

export default {
  name: 'ActivityList',
  data() {
    return {
      activityList: [],
      loading: false,
      searchForm: {
        activityType: '',
        keyword: '',
        timeRange: null
      }
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
    currentUserId() {
      return this.$store.state.user ? this.$store.state.user.userId : null
    }
  },
  mounted() {
    this.loadActivityList()
  },
  methods: {
    async loadActivityList() {
      this.loading = true
      try {
        // 所有角色都显示全部活动
        const res = await activityApi.getActivityList()
        if (res.code === 200) {
          this.activityList = res.data || []
        }
      } catch (error) {
        console.error('加载活动列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleAdd() {
      this.$router.push('/activity/add')
    },
    handleEdit(row) {
      this.$router.push(`/activity/edit/${row.activityId}`)
    },
    handleDelete(row) {
      this.$confirm(`确定要删除活动"${row.activityName}"吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await activityApi.deleteActivity(row.activityId)
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.loadActivityList()
          }
        } catch (error) {
          console.error('删除失败:', error)
        }
      }).catch(() => {
        // 用户点击取消，不需要处理
      })
    },
    async handleSearch() {
      this.loading = true
      try {
        const params = {
          activityType: this.searchForm.activityType || null,
          keyword: this.searchForm.keyword || null
        }
        
        // 如果选择了时间范围
        if (this.searchForm.timeRange && this.searchForm.timeRange.length === 2) {
          params.startTime = this.searchForm.timeRange[0]
          params.endTime = this.searchForm.timeRange[1]
        }
        
        const res = await activityApi.queryActivity(params)
        if (res.code === 200) {
          this.activityList = res.data || []
        } else {
          this.$message.error(res.message || '查询失败')
        }
      } catch (error) {
        console.error('查询失败:', error)
        this.$message.error('查询失败，请重试')
      } finally {
        this.loading = false
      }
    },
    handleReset() {
      this.searchForm = {
        activityType: '',
        keyword: '',
        timeRange: null
      }
      // 重置后加载所有活动
      this.loadActivityList()
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
.activity-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}
</style>

