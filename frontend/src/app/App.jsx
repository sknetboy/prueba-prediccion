import { useState } from 'react'
import { PieChart, Pie, Tooltip } from 'recharts'
import { usePrediction } from '../features/prediction/usePrediction'
import { useStats } from '../features/stats/useStats'
import { PredictionCard } from '../components/PredictionCard'

export const App = () => {
  const [form, setForm] = useState({ tiempoContratoMeses: 12, retrasosPago: 1, usoMensual: 20, plan: 'Premium' })
  const { result, loading, runPrediction } = usePrediction()
  const { stats } = useStats()

  const submit = (e) => {
    e.preventDefault()
    runPrediction(form)
  }

  return (
    <main style={{ maxWidth: 960, margin: '0 auto', fontFamily: 'sans-serif' }}>
      <h1>ChurnInsight Dashboard</h1>
      <form onSubmit={submit} style={{ display: 'grid', gap: 8 }}>
        <input type='number' value={form.tiempoContratoMeses} onChange={(e) => setForm({ ...form, tiempoContratoMeses: Number(e.target.value) })} />
        <input type='number' value={form.retrasosPago} onChange={(e) => setForm({ ...form, retrasosPago: Number(e.target.value) })} />
        <input type='number' value={form.usoMensual} onChange={(e) => setForm({ ...form, usoMensual: Number(e.target.value) })} />
        <select value={form.plan} onChange={(e) => setForm({ ...form, plan: e.target.value })}>
          <option>Basic</option><option>Standard</option><option>Premium</option>
        </select>
        <button disabled={loading}>Predecir</button>
      </form>
      <PredictionCard result={result} />
      <h2>Estadísticas</h2>
      <p>Total evaluados: {stats.total_evaluados}</p>
      <p>Tasa churn: {stats.tasa_churn}</p>
      <PieChart width={300} height={220}>
        <Pie data={[{ name: 'Churn', value: stats.tasa_churn }, { name: 'Retención', value: 1 - stats.tasa_churn }]} dataKey='value' cx='50%' cy='50%' outerRadius={70} fill='#8884d8' />
        <Tooltip />
      </PieChart>
    </main>
  )
}
