import axios from 'axios'

const api = axios.create({ baseURL: 'http://localhost:8080/api/v1' })

export const predictChurn = async (payload) => (await api.post('/predict', payload)).data
export const fetchStats = async () => (await api.get('/stats')).data
