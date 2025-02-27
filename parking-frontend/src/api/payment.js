import request from '@/utils/request'

// 支付相关接口
export function createPayment(licensePlate) {
  return request({
    url: `/payment/create/offline`,
    method: 'get',
    params: { licensePlate }
  })
}

export function payOffline(vid, pid) {
  return request({
    url: `/payment/pay/offline`,
    method: 'post',
    params: { vid, pid }
  })
}

export function cancelPayment(vid, pid) {
  return request({
    url: `/payment/cancel`,
    method: 'post',
    params: { vid, pid }
  })
}

export function getPaymentRecord(params) {
  return request({
    url: '/payment/record/page',
    method: 'post',
    data: params
  })
} 