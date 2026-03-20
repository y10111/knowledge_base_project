import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api'

// 创建axios实例
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 响应拦截器
apiClient.interceptors.response.use(
  response => response.data,
  error => {
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

// 对话管理API
export const conversationApi = {
  // 获取对话列表
  getConversations: (page = 1, size = 20) => {
    return apiClient.get('/conversations', { params: { page, size } })
  },
  
  // 获取对话详情
  getConversation: (id) => {
    return apiClient.get(`/conversations/${id}`)
  },
  
  // 创建对话
  createConversation: (title) => {
    return apiClient.post('/conversations', { title })
  },
  
  // 删除对话
  deleteConversation: (id) => {
    return apiClient.delete(`/conversations/${id}`)
  },
  
  // 发送消息
  sendMessage: (conversationId, content) => {
    return apiClient.post(`/conversations/${conversationId}/messages`, { content })
  }
}

// 文档管理API
export const documentApi = {
  // 获取文档列表
  getDocuments: (page = 1, size = 10, categoryId) => {
    return apiClient.get('/docs', { params: { page, size, categoryId } })
  },
  
  // 获取文档详情
  getDocument: (id) => {
    return apiClient.get(`/docs/${id}`)
  },
  
  // 创建文档
  createDocument: (document) => {
    return apiClient.post('/docs', document)
  },
  
  // 更新文档
  updateDocument: (id, document) => {
    return apiClient.put(`/docs/${id}`, document)
  },
  
  // 删除文档
  deleteDocument: (id) => {
    return apiClient.delete(`/docs/${id}`)
  },
  
  // 增加浏览次数
  increaseViewCount: (id) => {
    return apiClient.post(`/docs/${id}/view`)
  }
}

// 文档分类API
export const categoryApi = {
  // 获取分类列表
  getCategories: () => {
    return apiClient.get('/categories')
  },
  
  // 获取分类详情
  getCategory: (id) => {
    return apiClient.get(`/categories/${id}`)
  },
  
  // 创建分类
  createCategory: (category) => {
    return apiClient.post('/categories', category)
  },
  
  // 更新分类
  updateCategory: (id, category) => {
    return apiClient.put(`/categories/${id}`, category)
  },
  
  // 删除分类
  deleteCategory: (id) => {
    return apiClient.delete(`/categories/${id}`)
  }
}

// 搜索API
export const searchApi = {
  // 搜索
  search: (keyword, type = 'all') => {
    return apiClient.get('/search', { params: { keyword, type } })
  }
}

// 健康检查API
export const healthApi = {
  // 健康检查
  checkHealth: () => {
    return apiClient.get('/health')
  }
}