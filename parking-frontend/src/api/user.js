import request from '@/utils/request'

export function login(data) {
  return request.post('user/login', data)
}

// 检查用户名是否可用
export function checkUsername(username) {
  return request({
    url: '/user/check/username',
    method: 'post',
    params: {
      username
    }
  })
}

// 注册用户
export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data: {
      username: data.username,
      password: data.password
    }
  })
} 