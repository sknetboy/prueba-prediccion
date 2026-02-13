import { useEffect, useState } from 'react'
import { fetchStats } from '../../services/api'

export const useStats = () => {
  const [stats, setStats] = useState({ total_evaluados: 0, tasa_churn: 0 })

  useEffect(() => {
    fetchStats().then(setStats).catch(() => {})
  }, [])

  return { stats }
}
