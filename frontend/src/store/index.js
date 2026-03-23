import Vue from 'vue'
import Vuex from 'vuex'
import { conversationApi, documentApi, categoryApi, searchApi } from '@/api'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    conversations: [],
    currentConversation: null,
    conversationMessages: [],
    conversationsLoading: false,
    documents: [],
    currentDocument: null,
    documentsLoading: false,
    categories: [],
    categoriesLoading: false,
    searchResults: null,
    searchLoading: false,
    settingsMenu: 'data-dictionary'
  },

  mutations: {
    SET_CONVERSATIONS(state, conversations) {
      state.conversations = conversations
    },
    SET_CURRENT_CONVERSATION(state, conversation) {
      state.currentConversation = conversation
    },
    SET_CONVERSATION_MESSAGES(state, messages) {
      state.conversationMessages = messages
    },
    SET_CONVERSATIONS_LOADING(state, loading) {
      state.conversationsLoading = loading
    },
    ADD_CONVERSATION(state, conversation) {
      state.conversations.push(conversation)
    },
    REMOVE_CONVERSATION(state, conversationId) {
      state.conversations = state.conversations.filter(c => c.id !== conversationId)
      if (state.currentConversation && state.currentConversation.id === conversationId) {
        state.currentConversation = null
        state.conversationMessages = []
      }
    },
    ADD_MESSAGE(state, message) {
      state.conversationMessages.push(message)
    },
    SET_DOCUMENTS(state, documents) {
      state.documents = documents
    },
    SET_CURRENT_DOCUMENT(state, document) {
      state.currentDocument = document
    },
    SET_DOCUMENTS_LOADING(state, loading) {
      state.documentsLoading = loading
    },
    ADD_DOCUMENT(state, document) {
      state.documents.push(document)
    },
    UPDATE_DOCUMENT(state, document) {
      const index = state.documents.findIndex(d => d.id === document.id)
      if (index !== -1) {
        state.documents.splice(index, 1, document)
      }
    },
    REMOVE_DOCUMENT(state, documentId) {
      state.documents = state.documents.filter(d => d.id !== documentId)
      if (state.currentDocument && state.currentDocument.id === documentId) {
        state.currentDocument = null
      }
    },
    SET_CATEGORIES(state, categories) {
      state.categories = categories
    },
    SET_CATEGORIES_LOADING(state, loading) {
      state.categoriesLoading = loading
    },
    SET_SEARCH_RESULTS(state, results) {
      state.searchResults = results
    },
    SET_SEARCH_LOADING(state, loading) {
      state.searchLoading = loading
    },
    SET_SETTINGS_MENU(state, menu) {
      state.settingsMenu = menu
    }
  },

  actions: {
    async fetchConversations({ commit }, { page = 1, size = 20 } = {}) {
      commit('SET_CONVERSATIONS_LOADING', true)
      try {
        const response = await conversationApi.getConversations(page, size)
        if (response.code === 200 && response.data) {
          commit('SET_CONVERSATIONS', response.data.content || [])
        }
      } catch (error) {
        console.error('Failed to fetch conversations:', error)
      } finally {
        commit('SET_CONVERSATIONS_LOADING', false)
      }
    },

    async fetchConversation({ commit }, conversationId) {
      commit('SET_CONVERSATIONS_LOADING', true)
      try {
        const response = await conversationApi.getConversation(conversationId)
        console.log('fetchConversation response:', response)
        if (response.code === 200 && response.data) {
          commit('SET_CURRENT_CONVERSATION', response.data)
          commit('SET_CONVERSATION_MESSAGES', response.data.messages || [])
          console.log('Conversation loaded:', response.data)
        }
      } catch (error) {
        console.error('Failed to fetch conversation:', error)
      } finally {
        commit('SET_CONVERSATIONS_LOADING', false)
      }
    },

    async createConversation({ commit }, title) {
      try {
        const response = await conversationApi.createConversation(title)
        if (response.code === 200 && response.data) {
          commit('ADD_CONVERSATION', response.data)
          commit('SET_CURRENT_CONVERSATION', response.data)
          commit('SET_CONVERSATION_MESSAGES', [])
          return response.data
        }
      } catch (error) {
        console.error('Failed to create conversation:', error)
      }
    },

    async deleteConversation({ commit }, conversationId) {
      try {
        const response = await conversationApi.deleteConversation(conversationId)
        if (response.code === 200) {
          commit('REMOVE_CONVERSATION', conversationId)
        }
        return response.code === 200
      } catch (error) {
        console.error('Failed to delete conversation:', error)
        return false
      }
    },

    async sendMessage({ commit }, { conversationId, content }) {
      try {
        // 1. 添加用户消息到本地状态
        const userMessage = {
          id: Date.now(),
          conversationId: conversationId,
          role: 'user',
          content: content,
          createdAt: new Date()
        }
        commit('ADD_MESSAGE', userMessage)

        // 2. 发送请求到后端
        const response = await conversationApi.sendMessage(conversationId, content)
        if (response.code === 200 && response.data) {
          // 3. 添加 AI 消息到本地状态
          const aiMessage = {
            id: Date.now() + 1,
            conversationId: conversationId,
            role: 'assistant',
            content: response.data.answer,
            createdAt: new Date(),
            sources: response.data.sources,
            thoughtProcess: response.data.thought_process
          }
          commit('ADD_MESSAGE', aiMessage)

          return response.data
        }
      } catch (error) {
        console.error('Failed to send message:', error)
      }
    },

    async fetchDocuments({ commit }, { page = 1, size = 10, categoryId } = {}) {
      commit('SET_DOCUMENTS_LOADING', true)
      try {
        const response = await documentApi.getDocuments(page, size, categoryId)
        if (response.code === 200 && response.data) {
          commit('SET_DOCUMENTS', response.data.content || [])
        }
      } catch (error) {
        console.error('Failed to fetch documents:', error)
      } finally {
        commit('SET_DOCUMENTS_LOADING', false)
      }
    },

    async fetchDocument({ commit }, documentId) {
      commit('SET_DOCUMENTS_LOADING', true)
      try {
        const response = await documentApi.getDocument(documentId)
        if (response.code === 200 && response.data) {
          commit('SET_CURRENT_DOCUMENT', response.data)
          documentApi.increaseViewCount(documentId)
        }
      } catch (error) {
        console.error('Failed to fetch document:', error)
      } finally {
        commit('SET_DOCUMENTS_LOADING', false)
      }
    },

    async createDocument({ commit }, document) {
      try {
        const response = await documentApi.createDocument(document)
        if (response.code === 200 && response.data) {
          commit('ADD_DOCUMENT', response.data)
          return response.data
        }
      } catch (error) {
        console.error('Failed to create document:', error)
      }
    },

    async updateDocument({ commit }, { id, document }) {
      try {
        const response = await documentApi.updateDocument(id, document)
        if (response.code === 200 && response.data) {
          commit('UPDATE_DOCUMENT', response.data)
          return response.data
        }
      } catch (error) {
        console.error('Failed to update document:', error)
      }
    },

    async deleteDocument({ commit }, documentId) {
      try {
        const response = await documentApi.deleteDocument(documentId)
        if (response.code === 200) {
          commit('REMOVE_DOCUMENT', documentId)
        }
        return response.code === 200
      } catch (error) {
        console.error('Failed to delete document:', error)
        return false
      }
    },

    async fetchCategories({ commit }) {
      commit('SET_CATEGORIES_LOADING', true)
      try {
        const response = await categoryApi.getCategories()
        if (response.code === 200 && response.data) {
          commit('SET_CATEGORIES', response.data)
        }
      } catch (error) {
        console.error('Failed to fetch categories:', error)
      } finally {
        commit('SET_CATEGORIES_LOADING', false)
      }
    },

    async search({ commit }, { keyword, type = 'all' } = {}) {
      commit('SET_SEARCH_LOADING', true)
      try {
        const response = await searchApi.search(keyword, type)
        if (response.code === 200 && response.data) {
          commit('SET_SEARCH_RESULTS', response.data)
        }
      } catch (error) {
        console.error('Failed to search:', error)
      } finally {
        commit('SET_SEARCH_LOADING', false)
      }
    }
  },

  getters: {
    allConversations: state => state.conversations,
    currentConversation: state => state.currentConversation,
    conversationMessages: state => state.conversationMessages,
    conversationsLoading: state => state.conversationsLoading,
    allDocuments: state => state.documents,
    currentDocument: state => state.currentDocument,
    documentsLoading: state => state.documentsLoading,
    allCategories: state => state.categories,
    categoriesLoading: state => state.categoriesLoading,
    searchResults: state => state.searchResults,
    searchLoading: state => state.searchLoading,
    settingsMenu: state => state.settingsMenu,
    documentTree: state => {
      const buildTree = (categories, documents, parentId = 0) => {
        return categories
          .filter(cat => cat.parentId === parentId)
          .map(cat => ({
            id: cat.id,
            name: cat.name,
            type: 'category',
            children: [
              ...buildTree(categories, documents, cat.id),
              ...documents
                .filter(doc => doc.categoryId === cat.id)
                .map(doc => ({
                  id: doc.id,
                  name: doc.title,
                  type: 'document'
                }))
            ]
          }))
      }
      return buildTree(state.categories, state.documents)
    }
  }
})
