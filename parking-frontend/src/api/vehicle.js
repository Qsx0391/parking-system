import request from '@/utils/request'

// 车辆相关接口
export function vehicleEntry(licensePlate) {
  return request({
    url: `/vehicle/entry`,
    method: 'post',
    params: { licensePlate }
  })
}

export function vehicleExit(licensePlate) {
  return request({
    url: `/vehicle/exit`,
    method: 'post',
    params: { licensePlate }
  })
}

export function getParkingRecord(params) {
  return request({
    url: '/vehicle/record/page',
    method: 'post',
    data: params
  })
}