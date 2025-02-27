import request from '@/utils/request'

// 配置相关接口
export function getParkingConfig() {
  return request({
    url: '/config/get',
    method: 'get'
  })
} 

export function updateParkingConfig(data) {
  return request({
    url: 'admin/config/update',
    method: 'post',
    data
  })
} 