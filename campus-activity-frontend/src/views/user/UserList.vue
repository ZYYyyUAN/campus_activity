<template>
  <div class="user-list">
    <el-card>
      <div slot="header" class="card-header">
        <span>用户列表</span>
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增用户</el-button>
      </div>
      
      <!-- 搜索栏 -->
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="用户角色">
          <el-select v-model="searchForm.role" placeholder="请选择" clearable>
            <el-option label="管理员" value="admin"></el-option>
            <el-option label="发布者" value="publisher"></el-option>
            <el-option label="学生" value="student"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      
      <!-- 用户表格 -->
      <el-table :data="userList" border style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60"></el-table-column>
        <el-table-column prop="username" label="用户名" width="150"></el-table-column>
        <el-table-column prop="realName" label="真实姓名" width="120"></el-table-column>
        <el-table-column prop="role" label="角色" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRoleType(scope.row.role)">
              {{ scope.row.role }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="warning" @click="handleResetPassword(scope.row)">重置密码</el-button>
            <el-button size="mini" :type="scope.row.status === 1 ? 'danger' : 'success'" 
                       @click="handleToggleStatus(scope.row)">
              {{ scope.row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    
    <!-- 用户编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="500px">
      <el-form :model="userForm" :rules="rules" ref="userForm" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" :disabled="isEdit"></el-input>
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="userForm.realName"></el-input>
        </el-form-item>
        <el-form-item label="用户角色" prop="role">
          <el-select v-model="userForm.role" placeholder="请选择" style="width: 100%">
            <el-option label="管理员" value="admin"></el-option>
            <el-option label="发布者" value="publisher"></el-option>
            <el-option label="学生" value="student"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户状态" prop="status">
          <el-select v-model="userForm.status" placeholder="请选择" style="width: 100%">
            <el-option :label="1" :value="1">正常</el-option>
            <el-option :label="0" :value="0">禁用</el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { userApi } from '@/api'

export default {
  name: 'UserList',
  data() {
    return {
      userList: [],
      loading: false,
      searchForm: {
        role: ''
      },
      dialogVisible: false,
      dialogTitle: '新增用户',
      isEdit: false,
      userForm: {
        username: '',
        realName: '',
        role: '',
        status: 1
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        realName: [
          { required: true, message: '请输入真实姓名', trigger: 'blur' }
        ],
        role: [
          { required: true, message: '请选择用户角色', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择用户状态', trigger: 'change' }
        ]
      }
    }
  },
  mounted() {
    this.loadUserList()
  },
  methods: {
    async loadUserList() {
      this.loading = true
      try {
        let res
        if (this.searchForm.role) {
          res = await userApi.getUserByRole(this.searchForm.role)
        } else {
          res = await userApi.getUserList()
        }
        if (res.code === 200) {
          this.userList = res.data || []
        }
      } catch (error) {
        console.error('加载用户列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleAdd() {
      this.dialogTitle = '新增用户'
      this.isEdit = false
      this.userForm = {
        username: '',
        realName: '',
        role: '',
        status: 1
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑用户'
      this.isEdit = true
      this.userForm = {
        userId: row.userId,
        username: row.username,
        realName: row.realName,
        role: row.role,
        status: row.status
      }
      this.dialogVisible = true
    },
    handleSave() {
      this.$refs.userForm.validate(async (valid) => {
        if (valid) {
          try {
            let res
            if (this.isEdit) {
              res = await userApi.updateUser(this.userForm)
            } else {
              res = await userApi.addUser(this.userForm)
            }
            if (res.code === 200) {
              this.$message.success(this.isEdit ? '更新成功' : '添加成功')
              this.dialogVisible = false
              this.loadUserList()
            }
          } catch (error) {
            console.error('保存失败:', error)
          }
        }
      })
    },
    handleDelete(row) {
      this.$confirm(`确定要删除用户"${row.realName}"吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await userApi.deleteUser(row.userId)
          if (res.code === 200) {
            this.$message.success('删除成功')
            this.loadUserList()
          }
        } catch (error) {
          console.error('删除失败:', error)
        }
      }).catch(() => {
        // 用户点击取消，不需要处理
      })
    },
    handleResetPassword(row) {
      this.$confirm(`确定要重置用户"${row.realName}"的密码为123456吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await userApi.resetPassword(row.userId)
          if (res.code === 200) {
            this.$message.success('密码已重置为123456')
          }
        } catch (error) {
          console.error('重置密码失败:', error)
        }
      }).catch(() => {
        // 用户点击取消，不需要处理
      })
    },
    handleToggleStatus(row) {
      const newStatus = row.status === 1 ? 0 : 1
      const statusText = newStatus === 1 ? '启用' : '禁用'
      this.$confirm(`确定要${statusText}用户"${row.realName}"吗?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const res = await userApi.updateStatus({
            userId: row.userId,
            status: newStatus
          })
          if (res.code === 200) {
            this.$message.success(`${statusText}成功`)
            this.loadUserList()
          }
        } catch (error) {
          console.error('更新状态失败:', error)
        }
      }).catch(() => {
        // 用户点击取消，不需要处理
      })
    },
    handleSearch() {
      this.loadUserList()
    },
    handleReset() {
      this.searchForm.role = ''
      this.loadUserList()
    },
    getRoleType(role) {
      const roleMap = {
        'admin': 'danger',
        'publisher': 'warning',
        'student': 'success'
      }
      return roleMap[role] || 'info'
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
.user-list {
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

