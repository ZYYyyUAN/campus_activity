<template>
  <div class="evaluation-form">
    <el-card>
      <div slot="header">
        <span>活动评价</span>
      </div>
      
      <el-form :model="evaluationForm" :rules="rules" ref="evaluationForm" label-width="100px">
        <el-form-item label="活动名称">
          <el-input v-model="activityName" disabled></el-input>
        </el-form-item>
        
        <el-form-item label="评分" prop="rating">
          <el-rate v-model="evaluationForm.rating" :max="5" show-text></el-rate>
        </el-form-item>
        
        <el-form-item label="评价内容" prop="content">
          <el-input
            type="textarea"
            v-model="evaluationForm.content"
            :rows="6"
            placeholder="请输入您对本次活动的评价和建议"
            maxlength="500"
            show-word-limit>
          </el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">提交评价</el-button>
          <el-button @click="handleCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { evaluationApi, activityApi } from '@/api'

export default {
  name: 'EvaluationForm',
  data() {
    return {
      evaluationForm: {
        activityId: null,
        userId: null,
        rating: 5,
        content: ''
      },
      activityName: '',
      rules: {
        rating: [
          { required: true, message: '请选择评分', trigger: 'change' }
        ],
        content: [
          { required: true, message: '请输入评价内容', trigger: 'blur' },
          { min: 10, message: '评价内容至少10个字', trigger: 'blur' }
        ]
      }
    }
  },
  mounted() {
    this.evaluationForm.userId = this.$store.state.user.userId
    if (this.$route.params.activityId) {
      this.evaluationForm.activityId = parseInt(this.$route.params.activityId)
      this.loadActivityInfo()
    } else {
      this.$message.error('缺少活动信息')
      this.$router.back()
    }
  },
  methods: {
    async loadActivityInfo() {
      try {
        const res = await activityApi.getActivityById(this.evaluationForm.activityId)
        if (res.code === 200) {
          this.activityName = res.data.activityName
        }
      } catch (error) {
        console.error('加载活动信息失败:', error)
      }
    },
    handleSubmit() {
      this.$refs.evaluationForm.validate(async (valid) => {
        if (valid) {
          try {
            const res = await evaluationApi.addEvaluation(this.evaluationForm)
            if (res.code === 200) {
              this.$message.success('评价成功')
              this.$router.back()
            } else {
              this.$message.error(res.message || '评价失败')
            }
          } catch (error) {
            console.error('提交评价失败:', error)
            this.$message.error('评价失败，请重试')
          }
        }
      })
    },
    handleCancel() {
      this.$router.back()
    }
  }
}
</script>

<style scoped>
.evaluation-form {
  padding: 20px;
}

.el-form {
  max-width: 600px;
}
</style>
