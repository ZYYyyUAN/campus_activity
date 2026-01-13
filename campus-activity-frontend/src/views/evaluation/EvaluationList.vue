<template>
  <div class="evaluation-list">
    <el-card>
      <div slot="header">
        <span>评价管理</span>
      </div>
      
      <!-- 统计信息 -->
      <el-row :gutter="20" class="statistics">
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="stat-item">
              <div class="stat-label">总评价数</div>
              <div class="stat-value">{{ totalCount }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="stat-item">
              <div class="stat-label">平均评分</div>
              <div class="stat-value">{{ averageRating.toFixed(1) }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="stat-item">
              <div class="stat-label">最高评分</div>
              <div class="stat-value">{{ maxRating }}</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="stat-item">
              <div class="stat-label">最低评分</div>
              <div class="stat-value">{{ minRating }}</div>
            </div>
          </el-card>
        </el-col>
      </el-row>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" class="search-form">
        <el-form-item label="关键词搜索">
          <el-input 
            v-model="keyword" 
            placeholder="请输入评价内容关键词" 
            clearable
            @input="handleSearch">
          </el-input>
        </el-form-item>
        <el-form-item label="评分排序">
          <el-select v-model="sortOrder" @change="handleSort" placeholder="请选择">
            <el-option label="默认排序" value=""></el-option>
            <el-option label="评分从高到低" value="desc"></el-option>
            <el-option label="评分从低到高" value="asc"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 评价表格 -->
      <el-table :data="displayList" border v-loading="loading" style="width: 100%">
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="activityName" label="活动名称" min-width="150"></el-table-column>
        <el-table-column prop="realName" label="评价人" width="100"></el-table-column>
        <el-table-column prop="userName" label="学号" width="120"></el-table-column>
        <el-table-column prop="rating" label="评分" width="150" sortable>
          <template slot-scope="scope">
            <el-rate v-model="scope.row.rating" disabled show-score></el-rate>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="评价内容" min-width="250" show-overflow-tooltip></el-table-column>
        <el-table-column prop="evalTime" label="评价时间" width="180">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.evalTime) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { evaluationApi } from '@/api'

export default {
  name: 'EvaluationList',
  data() {
    return {
      evaluationList: [],
      displayList: [],
      loading: false,
      keyword: '',
      sortOrder: ''
    }
  },
  computed: {
    totalCount() {
      return this.displayList.length
    },
    averageRating() {
      if (this.displayList.length === 0) return 0
      const sum = this.displayList.reduce((acc, item) => acc + item.rating, 0)
      return sum / this.displayList.length
    },
    maxRating() {
      if (this.displayList.length === 0) return 0
      return Math.max(...this.displayList.map(item => item.rating))
    },
    minRating() {
      if (this.displayList.length === 0) return 0
      return Math.min(...this.displayList.map(item => item.rating))
    }
  },
  mounted() {
    this.loadEvaluationList()
  },
  methods: {
    async loadEvaluationList() {
      this.loading = true
      try {
        const res = await evaluationApi.getAllEvaluations()
        if (res.code === 200) {
          this.evaluationList = res.data || []
          this.displayList = [...this.evaluationList]
        } else {
          this.$message.error(res.message || '加载失败')
        }
      } catch (error) {
        console.error('加载评价列表失败:', error)
        this.$message.error('加载失败，请重试')
      } finally {
        this.loading = false
      }
    },
    async handleSearch() {
      if (!this.keyword.trim()) {
        this.displayList = [...this.evaluationList]
        this.handleSort()
        return
      }
      
      this.loading = true
      try {
        const res = await evaluationApi.getEvaluationByKeyword(this.keyword)
        if (res.code === 200) {
          this.displayList = res.data || []
          this.handleSort()
        } else {
          this.$message.error(res.message || '查询失败')
        }
      } catch (error) {
        console.error('搜索失败:', error)
        this.$message.error('搜索失败，请重试')
      } finally {
        this.loading = false
      }
    },
    handleSort() {
      if (!this.sortOrder) {
        return
      }
      
      this.displayList.sort((a, b) => {
        if (this.sortOrder === 'desc') {
          return b.rating - a.rating
        } else if (this.sortOrder === 'asc') {
          return a.rating - b.rating
        }
        return 0
      })
    },
    handleReset() {
      this.keyword = ''
      this.sortOrder = ''
      this.displayList = [...this.evaluationList]
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
.evaluation-list {
  padding: 20px;
}

.statistics {
  margin-bottom: 20px;
}

.stat-item {
  text-align: center;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #409EFF;
}

.search-form {
  margin-bottom: 20px;
}
</style>
