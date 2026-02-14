export const PredictionCard = ({ result }) => {
  if (!result) return null
  const color = result.probabilidad >= 0.7 ? '#ef4444' : result.probabilidad >= 0.4 ? '#f59e0b' : '#22c55e'
  return (
    <div style={{ border: `2px solid ${color}`, borderRadius: 8, padding: 16 }}>
      <h3>{result.prevision}</h3>
      <p>Probabilidad: {(result.probabilidad * 100).toFixed(1)}%</p>
      <p>Factores: {result.factores_clave?.join(', ')}</p>
    </div>
  )
}
