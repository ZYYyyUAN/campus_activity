<template>
  <div class="activity-form">
    <el-card>
      <div slot="header">
        <span>{{ isEdit ? '编辑活动' : '新增活动' }}</span>
      </div>
      
      <el-form :model="activityForm" :rules="rules" ref="activityForm" label-width="100px">
        <el-form-item label="活动名称" prop="activityName">
          <el-input v-model="activityForm.activityName" placeholder="请输入活动名称"></el-input>
        </el-form-item>
        
        <el-form-item label="活动类型" prop="activityType">
          <el-select v-model="activityForm.activityType" placeholder="请选择活动类型" style="width: 100%">
            <el-option label="讲座" value="讲座"></el-option>
            <el-option label="比赛" value="比赛"></el-option>
            <el-option label="学术论坛" value="学术论坛"></el-option>
            <el-option label="班级活动" value="班级活动"></el-option>
            <el-option label="志愿服务" value="志愿服务"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="活动地点" prop="location">
          <el-input v-model="activityForm.location" placeholder="请输入活动地点"></el-input>
        </el-form-item>
        
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="activityForm.startTime"
            type="datetime"
            placeholder="选择开始时间"
            style="width: 100%"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="activityForm.endTime"
            type="datetime"
            placeholder="选择结束时间"
            style="width: 100%"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
          ></el-date-picker>
        </el-form-item>
        
        <el-form-item label="人数上限" prop="maxPeople">
          <el-input-number v-model="activityForm.maxPeople" :min="0" placeholder="请输入人数上限" style="width: 100%"></el-input-number>
        </el-form-item>
        
        <el-form-item label="活动状态" prop="status">
          <el-select v-model="activityForm.status" placeholder="请选择活动状态" style="width: 100%">
            <el-option label="报名中" value="报名中"></el-option>
            <el-option label="已结束" value="已结束"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">保存</el-button>
          <el-button @click="handleCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { activityApi } from '@/api'

export default {
  name: 'ActivityForm',
  data() {
    return {
      activityForm: {
        activityName: '',
        activityType: '',
        location: '',
        startTime: '',
        endTime: '',
        maxPeople: 0,
        status: '报名中',
        publisherId: null
      },
      rules: {
        activityName: [
          { required: true, message: '请输入活动名称', trigger: 'blur' }
        ],
        activityType: [
          { required: true, message: '请选择活动类型', trigger: 'change' }
        ],
        location: [
          { required: true, message: '请输入活动地点', trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: '请选择开始时间', trigger: 'change' }
        ],
        endTime: [
          { required: true, message: '请选择结束时间', trigger: 'change' }
        ],
        maxPeople: [
          { required: true, message: '请输入人数上限', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择活动状态', trigger: 'change' }
        ]
      },
      isEdit: false,
      activityId: null
    }
  },
  mounted() {
    this.activityForm.publisherId = this.$store.state.user.userId
    if (this.$route.params.id) {
      this.isEdit = true
      this.activityId = this.$route.params.id
      this.loadActivity()
    }
  },
  methods: {
    async loadActivity() {
      try {
        const res = await activityApi.getActivityById(this.activityId)
        if (res.code === 200) {
          this.activityForm = {
            ...res.data,
            startTime: this.formatDateTime(res.data.startTime),
            endTime: this.formatDateTime(res.data.endTime)
          }
        }
      } catch (error) {
        console.error('加载活动信息失败:', error)
      }
    },
    handleSubmit() {
      this.$refs.activityForm.validate(async (valid) => {
        if (valid) {
          try {
            const formData = {
              ...this.activityForm,
              startTime: new Date(this.activityForm.startTime).toISOString().slice(0, 19).replace('T', ' '),
              endTime: new Date(this.activityForm.endTime).toISOString().slice(0, 19).replace('T', ' ')
            }
            let res
            if (this.isEdit) {
              formData.activityId = this.activityId
              res = await activityApi.updateActivity(formData)
            } else {
              res = await activityApi.addActivity(formData)
            }
            if (res.code === 200) {
              this.$message.success(this.isEdit ? '更新成功' : '添加成功')
              this.$router.push('/activity')
            }
          } catch (error) {
            console.error('保存失败:', error)
          }
        }
      })
    },
    handleCancel() {
      this.$router.push('/activity')
    },
    formatDateTime(dateStr) {
      if (!dateStr) return ''
      const date = new Date(dateStr)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hour = String(date.getHours()).padStart(2, '0')
      const minute = String(date.getMinutes()).padStart(2, '0')
      const second = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`
    }
  }
}
</script>

<style scoped>
.activity-form {
  padding: 20px;
}

.el-form {
  max-width: 600px;
}
</style>

