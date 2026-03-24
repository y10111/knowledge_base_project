```bash
cd /Users/yanghuaqiang/Desktop/knowledge_base_project/python_rag_service
# 创建虚拟环境
python -m venv venv
# 激活虚拟环境
source venv/bin/activate
# 安装依赖
pip install -r requirements.txt
# 启动服务
python app.py
```

---

RAG 服务（Python Flask）共提供三个接口，以下是详细列表：

---

### 1. 文档索引接口
| 属性 | 说明 |
|------|------|
| **URL** | `/index` |
| **方法** | POST |
| **Content-Type** | `application/json` |
| **请求体** | `{"doc_id": int, "content": "string", "file_path": "string", "file_type": "string"}` <br/> - `doc_id`：必填，文档唯一标识。<br/> - `content`：可选，文档纯文本内容（与 `file_path` 二选一）。<br/> - `file_path`：可选，文件绝对路径。<br/> - `file_type`：当提供 `file_path` 时必填，支持 `pdf`、`docx`、`txt`、`xlsx`。 |
| **成功响应** | `{"status": "ok", "chunks": int}`<br/> - `chunks`：文档被分割成的片段数。 |
| **错误响应** | `{"error": "错误信息"}` |

---

### 2. 智能问答接口
| 属性 | 说明 |
|------|------|
| **URL** | `/ask` |
| **方法** | POST |
| **Content-Type** | `application/json` |
| **请求体** | `{"question": "用户问题"}` |
| **成功响应** | `{"answer": "答案文本", "sources": [{"doc_id": int, "score": float}], "thought_process": "思考过程（含\n换行）"}`<br/> - `sources`：引用的文档列表，按相似度降序。<br/> - `thought_process`：逐步记录思考过程的文本。 |
| **错误响应** | `{"error": "错误信息"}` |

---

### 3. 健康检查接口
| 属性 | 说明 |
|------|------|
| **URL** | `/health` |
| **方法** | GET |
| **成功响应** | `{"status": "ok", "index_size": int}`<br/> - `index_size`：当前内存索引中的文档片段总数。 |

---

### 调用示例（curl）
```bash
# 索引文档
curl -X POST http://localhost:5001/index \
  -H "Content-Type: application/json" \
  -d '{"doc_id":1,"content":"边裂处理：提高终轧温度10-20℃"}'

# 提问
curl -X POST http://localhost:5001/ask \
  -H "Content-Type: application/json" \
  -d '{"question":"边裂怎么处理？"}'

# 健康检查
curl http://localhost:5001/health
```
