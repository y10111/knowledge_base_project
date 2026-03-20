好的，以下是后端接口定义（RESTful API），简洁列出：

---

### 一、对话管理

| 方法 | 路径 | 功能 | 请求参数 | 返回 |
|------|------|------|----------|------|
| POST | `/api/conversations` | 新建对话 | `{ "title": "新对话标题" }`（可选，不传则自动生成） | `{ "id": 1, "title": "...", "createdAt": "..." }` |
| GET | `/api/conversations` | 获取对话列表 | `?page=1&size=20` | `{ "total": 10, "items": [ { "id":1, "title":"...", "updatedAt":"..." } ] }` |
| GET | `/api/conversations/{id}` | 获取对话详情（含消息历史） | 路径参数 `id` | `{ "id":1, "title":"...", "messages": [ { "role":"user", "content":"...", "createdAt":"..." } ] }` |
| DELETE | `/api/conversations/{id}` | 删除对话 | 路径参数 `id` | `{ "message": "ok" }` |

---

### 二、对话消息

| 方法 | 路径 | 功能 | 请求参数 | 返回 |
|------|------|------|----------|------|
| POST | `/api/conversations/{id}/messages` | 发送新消息（问答） | `{ "content": "用户问题" }` | `{ "role": "assistant", "content": "AI回答", "sources": [{"docId":1,"title":"..."}] }` |
| POST | `/api/conversations/{id}/feedback` | 对答案反馈 | `{ "messageId": 123, "feedback": 1 }` (1有用，-1无用) | `{ "message": "ok" }` |

---

### 三、文档管理

| 方法 | 路径 | 功能 | 请求参数 | 返回 |
|------|------|------|----------|------|
| POST | `/api/docs/upload` | 上传文档 | multipart/form-data: `file`, `title`, `categoryId`（可选）, `uploader` | `{ "id":1, "title":"...", "categoryId":... }` |
| GET | `/api/docs` | 获取文档列表 | `?page=1&size=10&categoryId=2&keyword=边裂` | `{ "total":100, "items": [ {...} ] }` |
| GET | `/api/docs/{id}` | 获取文档详情 | 路径参数 `id` | `{ "id":1, "title":"...", "content":"...", "views":100 }` |
| PUT | `/api/docs/{id}` | 更新文档 | `{ "title":"...", "categoryId":... }` | `{ "id":1, ... }` |
| DELETE | `/api/docs/{id}` | 删除文档 | 路径参数 `id` | `{ "message": "ok" }` |
| POST | `/api/docs/{id}/view` | 增加浏览次数 | 路径参数 `id` | `{ "views": 101 }` |

---

### 四、文档分类树（数据字典）

| 方法 | 路径 | 功能 | 请求参数 | 返回 |
|------|------|------|----------|------|
| GET | `/api/categories` | 获取完整分类树 | 无 | `[ { "id":1, "name":"技术文档", "children": [ { "id":2, "name":"前端开发" } ] } ]` |
| POST | `/api/categories` | 新增分类 | `{ "name": "新分类", "parentId": 1 }` | `{ "id": 10, "name": "...", "parentId":1 }` |
| PUT | `/api/categories/{id}` | 修改分类 | `{ "name": "新名称", "sortOrder": 1 }` | `{ "id":1, ... }` |
| DELETE | `/api/categories/{id}` | 删除分类 | 路径参数 `id` | `{ "message": "ok" }` |

---

### 五、搜索

| 方法 | 路径 | 功能 | 请求参数 | 返回 |
|------|------|------|----------|------|
| GET | `/api/search` | 统一搜索（文档+对话） | `?q=边裂&type=all`（type可选 doc/conversation/all） | `{ "docs": [ ... ], "conversations": [ ... ] }` |

---

### 六、系统状态

| 方法 | 路径 | 功能 | 请求参数 | 返回 |
|------|------|------|----------|------|
| GET | `/api/health` | 健康检查 | 无 | `{ "status": "ok", "pythonAI": "connected" }` |

---

以上接口覆盖了你的前端所有功能：对话、文档库、分类树、搜索。需要更详细的字段说明可以随时提出。