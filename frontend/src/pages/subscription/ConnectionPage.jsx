import { useState } from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import { useAuth } from '../../hooks/useAuth'
import { isDemo } from '../../context/authState'
import { errorMessage } from '../../api/axios'

export default function ConnectionPage() {
  const { session, demoLogin } = useAuth()
  const { state } = useLocation()
  const [email, setEmail] = useState('user@submate.test')
  const [password, setPassword] = useState('')
  const [busy, setBusy] = useState(false)
  const [error, setError] = useState('')
  if (session) return <Navigate to={state?.from || '/subscriptions'} replace />
  async function handleLogin(event) {
    event.preventDefault()
    setBusy(true)
    setError('')
    try {
      await demoLogin(email, password)
      setPassword('')
    } catch (failure) {
      setError(errorMessage(failure))
    } finally {
      setBusy(false)
    }
  }
  if (!isDemo)
    return (
      <section className="panel login">
        <p className="eyebrow">SUBMATE</p>
        <h1>로그인이 필요해요</h1>
        <p>회원 인증 연결 후 구독 서비스를 이용할 수 있습니다.</p>
      </section>
    )
  return (
    <form className="panel login" onSubmit={handleLogin}>
      <p className="eyebrow">SUBMATE / LOCAL DEMO</p>
      <h1>구독을 한곳에서</h1>
      <p className="muted">구독·결제 기능을 확인하는 로컬 데모입니다.</p>
      <label>
        데모 계정
        <select
          disabled={busy}
          value={email}
          onChange={(event) => setEmail(event.target.value)}
        >
          <option value="user@submate.test">일반 회원</option>
          <option value="other@submate.test">다른 회원</option>
          <option value="admin@submate.test">관리자</option>
        </select>
      </label>
      <label>
        데모 비밀번호
        <input
          type="password"
          autoComplete="current-password"
          required
          value={password}
          disabled={busy}
          onChange={(event) => setPassword(event.target.value)}
        />
      </label>
      {error && (
        <p className="message error" role="alert">
          {error}
        </p>
      )}
      <button className="primary full" disabled={busy}>
        {busy ? '확인 중…' : '로그인'}
      </button>
      <p className="fine">서버 실행 시 지정한 데모 비밀번호를 사용합니다.</p>
    </form>
  )
}
