<template>
  <div class="login-page">
    <div class="login-visual" aria-hidden="true"></div>
    <div class="login-panel">
      <div class="login-panel-inner">
        <h1 class="login-title">创建账号</h1>
        <p class="login-subtitle">填写信息完成注册</p>
        <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          class="login-form"
          label-width="70px"
          label-position="top"
        >
          <el-form-item label="账号" prop="account" class="form-item">
            <el-input
              v-model="form.account"
              placeholder="请输入用户名"
              autocomplete="username"
              class="form-input"
            />
          </el-form-item>

          <el-form-item label="昵称" prop="nickname" class="form-item">
            <el-input
              v-model="form.nickname"
              placeholder="请输入昵称"
              class="form-input"
            />
          </el-form-item>

          <el-form-item label="手机号" prop="phone" class="form-item">
            <el-input
              v-model="form.phone"
              placeholder="请输入手机号"
              class="form-input"
            />
          </el-form-item>

          <el-form-item label="密码" prop="password" class="form-item">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              autocomplete="new-password"
              class="form-input"
              show-password
            />
          </el-form-item>

          <el-form-item label="确认密码" prop="confirmPassword" class="form-item">
            <el-input
              v-model="form.confirmPassword"
              type="password"
              placeholder="请再次输入密码"
              class="form-input"
              show-password
            />
          </el-form-item>

          <el-form-item class="agreement-item">
            <el-checkbox v-model="form.agree">
              我已阅读并同意
              <a class="login-link" href="#">《用户协议》</a>
              和
              <a class="login-link" href="#">《隐私政策》</a>
            </el-checkbox>
          </el-form-item>

          <el-form-item>
            <el-button
              type="primary"
              class="login-button"
              :loading="submitting"
              @click="onSubmit"
            >
              注册
            </el-button>
          </el-form-item>
        </el-form>

        <p class="login-footer">
          已有账户？
          <a class="login-link" href="#" @click.prevent="goLogin">返回登录</a>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import type { FormInstance, FormRules } from 'element-plus';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { registerApi } from '../api/user';

const formRef = ref<FormInstance>();
const submitting = ref(false);
const router = useRouter();

const form = reactive({
  account: '',
  nickname: '',
  phone: '',
  password: '',
  confirmPassword: '',
  agree: false,
});

const rules: FormRules = {
  account: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '手机号格式错误', trigger: 'blur' }
  ],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    {
      validator: (_rule, value, callback) => {
        if (value !== form.password) {
          callback(new Error('两次密码不一致'));
        } else {
          callback();
        }
      },
      trigger: 'blur',
    },
  ],
};

const onSubmit = () => {
  if (!formRef.value) return;
  formRef.value.validate(async (valid) => {
    if (!valid) return;
    if (!form.agree) {
      ElMessage.warning('请先勾选并同意协议');
      return;
    }
    submitting.value = true;
    try {
      const res = await registerApi({
        account: form.account,
        nickname: form.nickname,
        phone: form.phone,
        password: form.password
      });

      if (res && res.id) {
        ElMessage.success('注册成功，请登录');
        router.push('/login');
      }
    } catch (error) {
      console.error('Register error:', error);
    } finally {
      submitting.value = false;
    }
  });
};

const goLogin = () => {
  router.push('/login');
};
</script>

<style lang="scss" scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: stretch;
  background: #ffffff;
}

.login-visual {
  width: 50%;
  min-height: 100vh;
  background-image: linear-gradient(
    90deg,
    rgba(255, 255, 255, 0) 0%,
    rgba(255, 255, 255, 0.15) 40%,
    rgba(255, 255, 255, 0.7) 70%,
    rgba(255, 255, 255, 1) 100%
  ),
  url('../assets/login/loginBG.png');
  background-size: cover;
  background-position: center;
}

.login-panel {
  width: 50%;
  display: flex;
  align-items: center;
  justify-content: left;
}

.login-panel-inner {
  width: 600px;
  padding: 0 60px;
}

.login-title {
  font-size: 24px;
  font-weight: 600;
  color: #111827;
  text-align: center;
}

.login-subtitle {
  font-size: 13px;
  color: #9ca3af;
  text-align: center;
  margin-bottom: 24px;
}

.login-form {
  width: 420px;
  margin: 0 auto;
}

.login-button {
  width: 100%;
  height: 44px;
  border-radius: 8px;
  background: #4770fa;
  font-weight: 600;
  color: #fff;
  border: none;
  cursor: pointer;
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  font-size: 13px;
}

.login-link {
  color: #4b61f7;
  text-decoration: none;
}
</style>
