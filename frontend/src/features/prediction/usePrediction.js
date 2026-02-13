import { useState } from 'react'
import { predictChurn } from '../../services/api'

export const usePrediction = () => {
  const [result, setResult] = useState(null)
  const [loading, setLoading] = useState(false)

  const runPrediction = async (payload) => {
    setLoading(true)
    try {
      const data = await predictChurn(payload)
      setResult(data)
    } finally {
      setLoading(false)
    }
  }

  return { result, loading, runPrediction }
}
