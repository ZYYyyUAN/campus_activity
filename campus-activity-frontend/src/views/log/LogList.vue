<template>
  <div class="log-list">
    <el-card>
      <div slot="header">
        <span>系统日志</span>
      </div>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="操作类型">
          <el-select v-model="searchForm.operationType" placeholder="请选择" clearable>
            <el-option label="发布活动" value="发布活动"></el-option>
            <el-option label="删除活动" value="删除活动"></el-option>
            <el-option label="审核报名" value="审核报名"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker
            v-model="searchForm.startTime"
            type="datetime"
            placeholder="选择开始时间"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker
            v-model="searchForm.endTime"
            type="datetime"
            placeholder="选择结束时间"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 日志表格 -->
      <el-table :data="logList" border style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="realName" label="操作用户" width="120"></el-table-column>
        <el-table-column prop="operationType" label="操作类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getOperationTypeColor(scope.row.operationType)">
              {{ scope.row.operationType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operationContent" label="操作内容" min-width="200"></el-table-column>
        <el-table-column prop="operationTime" label="操作时间" width="180">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.operationTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </div>
    </el-card>
  </div>
</template>

<script>
import { logApi } from '@/api'

export default {
  name: 'LogList',
  data() {
    return {
      logList: [],
      loading: false,
      searchForm: {
        operationType: '',
        startTime: '',
        endTime: ''
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  mounted() {
    this.loadLogList()
  },
  methods: {
    async loadLogList() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          page: this.currentPage,
          size: this.pageSize
        }
        // 移除空值参数
        Object.keys(params).forEach(key => {
          if (!params[key]) {
            delete params[key]
          }
        })
        const res = await logApi.queryLog(params)
        if (res.code === 200) {
          this.logList = res.data || []
          this.total = this.logList.length
        }
      } catch (error) {
        console.error('加载日志列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.currentPage = 1
      this.loadLogList()
    },
    handleReset() {
      this.searchForm = {
        operationType: '',
        startTime: '',
        endTime: ''
      }
      this.currentPage = 1
      this.loadLogList()
    },
    handleDelete(row) {
      this.$confirm(`确定要删除这条日志吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await logApi.deleteLog(row.logId)
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.loadLogList()
          }
        } catch (error) {
          console.error('删除失败:', error)
        }
      }).catch(() => {
        // 用户点击取消，不需要处理
      })
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.loadLogList()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.loadLogList()
    },
    getOperationTypeColor(type) {
      const colorMap = {
        '发布活动': 'success',
        '删除活动': 'danger',
        '审核报名': 'warning'
      }
      return colorMap[type] || 'info'
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
.log-list {
  padding: 20px;
}

.search-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}
</style>

