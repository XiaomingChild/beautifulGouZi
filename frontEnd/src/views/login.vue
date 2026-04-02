<template>
  <div class="login-page">
    <div class="login-visual" aria-hidden="true"></div>
    <div class="login-panel">
      <div class="login-panel-inner">
        <h1 class="login-title">欢迎回来</h1>
        <p class="login-subtitle">请输入您的账户信息以继续</p>
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
              placeholder="请输入用户名或邮箱"
              autocomplete="username"
              class="form-input"
            />
          </el-form-item>
          <el-form-item label="密码" prop="password" class="form-item">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              autocomplete="current-password"
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
              登录
            </el-button>
          </el-form-item>
        </el-form>
        <div class="login-divider"><span>或使用其他方式登录</span></div>
        <div class="login-providers">
          <button type="button" class="provider">微博</button>
          <button type="button" class="provider">微信</button>
          <button type="button" class="provider">QQ</button>
        </div>
        <p class="login-footer">
          还没有账户？
          <a class="login-link" href="#" @click.prevent="goRegister">立即注册</a>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import type { FormInstance, FormRules } from 'element-plus';
import { useRouter } from 'vue-router';
import { useUserStore } from '../store/userInfo';
import { loginApi } from '../api/user';
import { ElMessage } from 'element-plus';

const formRef = ref<FormInstance>();
const submitting = ref(false);
const router = useRouter();
const userStore = useUserStore();
const form = reactive({
  account: '',
  password: '',
  agree: false,
});

const rules: FormRules = {
  account: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
};

const onSubmit = () => {
  if (!formRef.value) return;
  formRef.value.validate(async (valid) => {
    if (!valid) return;
    if (!form.agree) {
      ElMessage.warning('请先勾选同意协议');
      return;
    }
    submitting.value = true;
    try {
      // 调用解耦后的 API 函数
      const res: any = await loginApi({
        account: form.account,
        password: form.password
      });

      // 验证后端返回逻辑
      if (res && typeof res === 'object' && res.id) {
        ElMessage.success('登录成功');
        localStorage.setItem('token', 'TOKEN_' + res.id);
        
        userStore.setUserInfo({
          id: res.id,
          account: res.account,
          nickname: res.nickname || res.account,
          phone: res.phone || '',
          bio: res.bio || '',
          avatar: res.avatar || '',
          token: 'TOKEN_' + res.id,
        });
        
        router.push('/home');
      } else {
        ElMessage.error(res || '账号或密码错误');
      }
    } catch (error) {
      // 这里的错误会被 request.ts 的拦截器统一处理，ElMessage 已经弹出了
      console.error('Login error details:', error);
    } finally {
      submitting.value = false;
    }
  });
};

const goRegister = () => {
  router.push('/register');
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
   background-image:
    linear-gradient(
          90deg,
          rgba(255, 255, 255, 0) 0%,
          rgba(255, 255, 255, 0.15) 40%,
          rgba(255, 255, 255, 0.7) 70%,
         rgba(255, 255, 255, 1) 100%
       ),
    url('../assets/login/loginBG.png');
  background-size: cover;
  background-position: center;
  background-color: lin;
}

.login-panel {
  width: 50%;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: left;
  background: #ffffff;
}

.login-panel-inner {
  width: 600px;
  padding: 0 0 0 60px;
}

.login-title {
  font-size: 24px;
  font-weight: 600;
  color: #111827;
  text-align: center;
  margin: 0 0 8px;
}

.login-subtitle {
  font-size: 13px;
  color: #9ca3af;
  text-align: center;
  margin: 0 0 20px;
}

.login-form {
  display: grid;
  gap: 18px;
  width: 420px;
  max-width: 100%;
  margin: 0 auto;
}

.form-item {
  margin-bottom: 4px;
}

.agreement-item {
  margin: 8px 0 4px;
}

:deep(.el-form-item__label) {
  color: #374151;
  font-size: 13px;
}

:deep(.form-input .el-input__wrapper) {
  box-shadow: none;
  border-radius: 8px;
  padding: 0 12px;
  height: 44px;
  border: 1px solid #e5e7eb;
}

:deep(.form-input .el-input__wrapper.is-focus) {
  border-color: #8ea8ff;
  box-shadow: 0 0 0 3px rgba(142, 168, 255, 0.18);
}

:deep(.el-form-item__error) {
  margin-top: 4px;
  font-size: 12px;
}

.form-error {
  font-size: 12px;
  color: #e53935;
}

.login-button {
  margin-top: 8px;
  height: 44px;
  border: none;
  border-radius: 8px;
  background: #4770fa;
  color: #ffffff;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  width: 100%;
}

.login-button:hover {
  background: #5a80fc;
}

.login-divider {
  margin: 26px auto 18px;
  display: flex;
  align-items: center;
  gap: 12px;
  color: #9ca3af;
  font-size: 12px;
  width: 420px;
  max-width: 100%;
}

.login-divider::before,
.login-divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: #e5e7eb;
}

.login-providers {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
  margin: 0 auto 18px;
  width: 420px;
  max-width: 100%;
}

.provider {
  height: 36px;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  background: #ffffff;
  color: #4b5563;
  font-size: 12px;
  cursor: pointer;
  width: 100%;
}

.provider:hover {
  border-color: #c7d4ff;
  color: #5a74f5;
}

.login-footer {
  font-size: 12px;
  color: #6b7280;
  text-align: center;
  margin: 0 auto;
  width: 420px;
  max-width: 100%;
}

.login-link {
  color: #4b61f7;
  text-decoration: none;
  margin-left: 4px;
}

.login-agreement {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  font-size: 12px;
  color: #6b7280;
}

.login-agreement input {
  margin-top: 2px;
}

.login-agreement .login-link {
  margin: 0 2px;
}
</style>
