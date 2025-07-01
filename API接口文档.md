收到，针对你的要求，做如下修改：

1. **获取机房列表**：去除分页参数，接口一次性返回全部机房数据。
2. **所有操作接口**：统一增加操作成功的返回码（如`code: 0`表示成功），便于前后端联调和测试。

---

# 1. 机房 Room

### 获取机房列表
- **GET** `/api/rooms`
- **描述**：获取全部机房列表（无分页）
- **返回**：
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "name": "A机房",
      "location": "一楼",
      "image_id": 10,
      "description": "主机房"
    }
  ]
}
```

### 获取单个机房详情
- **GET** `/api/rooms/{id}`
- **返回**：
```json
{
  "code": 0,
  "data": {
    "id": 1,
    "name": "A机房",
    "location": "一楼",
    "image_id": 10,
    "description": "主机房"
  }
}
```

### 新增机房
- **POST** `/api/rooms`
- **Body**：
```json
{
  "name": "A机房",
  "location": "一楼",
  "image_id": 10,
  "description": "主机房"
}
```
- **返回**：
```json
{
  "code": 0,
  "data": { /* 新增后的机房对象 */ }
}
```

### 修改机房
- **PUT** `/api/rooms/{id}`
- **Body**：同上
- **返回**：
```json
{
  "code": 0,
  "data": { /* 修改后的机房对象 */ }
}
```

### 删除机房
- **DELETE** `/api/rooms/{id}`
- **返回**：
```json
{
  "code": 0,
  "message": "删除成功"
}
```

---

# 2. 机柜排 Cabinet Row

### 获取机柜排列表
- **GET** `/api/rows?roomId=1`
- **描述**: 获取指定机房下的所有机柜排
- **返回**:
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "roomId": 1,
      "name": "A排",
      "description": "第一排"
    }
  ]
}
```

### 新增机柜排
- **POST** `/api/rows`
- **Body**:
```json
{
  "roomId": 1,
  "name": "B排",
  "description": "第二排"
}
```
- **返回**:
```json
{
  "code": 0,
  "data": { /* 新增后的机柜排对象 */ }
}
```

### 修改机柜排
- **PUT** `/api/rows/{id}`
- **Body**:
```json
{
  "name": "B排-修改后",
  "description": "修改后的描述"
}
```
- **返回**:
```json
{
  "code": 0,
  "data": { /* 修改后的机柜排对象 */ }
}
```

### 删除机柜排
- **DELETE** `/api/rows/{id}`
- **返回**:
```json
{
  "code": 0,
  "message": "删除成功"
}
```

---

# 3. 机柜 Cabinet

### 获取机柜列表
- **GET** `/api/cabinets?rowId=1`
- **描述**：获取指定机柜排下的机柜列表
- **返回**：
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "roomId": 1,
      "rowId": 1,
      "name": "A1柜",
      "number": 1,
      "columnNumber": 2,
      "imageFront": 11,
      "imageBack": 12,
      "description": "主交换机柜"
    }
  ]
}
```

### 获取单个机柜详情
- **GET** `/api/cabinets/{id}`
- **返回**：
```json
{
  "code": 0,
  "data": { /* 机柜对象, 包含rowId, 不含rowNumber */ }
}
```

### 新增机柜
- **POST** `/api/cabinets`
- **Body**:
```json
{
  "rowId": 1,
  "name": "A2柜",
  "number": 2,
  "columnNumber": 3,
  "imageFront": 13,
  "imageBack": 14,
  "description": "备用交换机柜"
}
```
- **返回**：
```json
{
  "code": 0,
  "data": { /* 新增后的机柜对象 */ }
}
```

### 修改机柜
- **PUT** `/api/cabinets/{id}`
- **Body**: 同上, 但包含id且可修改rowId
- **返回**：
```json
{
  "code": 0,
  "data": { /* 修改后的机柜对象 */ }
}
```

### 删除机柜
- **DELETE** `/api/cabinets/{id}`
- **返回**：
```json
{
  "code": 0,
  "message": "删除成功"
}
```

---

# 4. 设备 Device

### 获取设备列表
- **GET** `/api/devices?cabinet_id=1`
- **描述**：获取指定机柜下的设备列表
- **返回**：
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "cabinet_id": 1,
      "name": "服务器A",
      "number": 1,
      "image_front": 21,
      "image_back": 22,
      "description": "主服务器"
    }
  ]
}
```

### 获取单个设备详情
- **GET** `/api/devices/{id}`
- **返回**：
```json
{
  "code": 0,
  "data": { /* 设备对象 */ }
}
```

### 新增设备
- **POST** `/api/devices`
- **Body**：同上
- **返回**：
```json
{
  "code": 0,
  "data": { /* 新增后的设备对象 */ }
}
```

### 修改设备
- **PUT** `/api/devices/{id}`
- **Body**：同上
- **返回**：
```json
{
  "code": 0,
  "data": { /* 修改后的设备对象 */ }
}
```

### 删除设备
- **DELETE** `/api/devices/{id}`
- **返回**：
```json
{
  "code": 0,
  "message": "删除成功"
}
```

---

# 5. 图片资源 Image Resource

### 上传图片
- **POST** `/api/images`
- **Body**：`multipart/form-data`，字段：`file`、`type`
- **返回**：
```json
{
  "code": 0,
  "data": {
    "id": 101,
    "url": "https://xxx/xxx.jpg",
    "type": "device_front",
    "created_at": "2024-06-01 12:00:00"
  }
}
```

### 获取图片详情
- **GET** `/api/images/{id}`
- **返回**：
```json
{
  "code": 0,
  "data": { /* 图片对象 */ }
}
```

### 删除图片
- **DELETE** `/api/images/{id}`
- **返回**：
```json
{
  "code": 0,
  "message": "删除成功"
}
```

---

# 6. 图标标注 Marker

### 获取标注列表
- **GET** `/api/markers?parent_type=device&parent_id=1&image_type=front`
- **描述**：获取指定图片上的所有标注点
- **返回**：
```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "parent_type": "device",
      "parent_id": 1,
      "image_type": "front",
      "x": 0.5,
      "y": 0.7,
      "ref_type": "interface",
      "ref_id": 2,
      "info": "主接口"
    }
  ]
}
```

### 新增标注
- **POST** `/api/markers`
- **Body**：同上
- **返回**：
```json
{
  "code": 0,
  "data": { /* 新增后的标注对象 */ }
}
```

### 修改标注
- **PUT** `/api/markers/{id}`
- **Body**：同上
- **返回**：
```json
{
  "code": 0,
  "data": { /* 修改后的标注对象 */ }
}
```

### 删除标注
- **DELETE** `/api/markers/{id}`
- **返回**：
```json
{
  "code": 0,
  "message": "删除成功"
}
```

---

# 7. 附录
如需进一步细化错误码、参数校验、权限等细节，欢迎继续补充！
