import request from '@/utils/request'

// 记录相关接口
export function getOperationLogRecord(params) {
  return request({
    url: '/admin/operation_log/page',
    method: 'post',
    data: params
  })
} 