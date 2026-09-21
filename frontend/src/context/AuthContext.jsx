import { useState } from 'react'
import api, { setAuthorization } from '../api/axios'
import { AuthContext, isDemo } from './authState'

export function AuthProvider({ children, initialSession = null, onLogout }) {
  const [session, setSession] = useState(() => {
    if (initialSession?.accessToken)
      setAuthorization(`Bearer ${initialSession.accessToken}`)
    return initialSession
  })
  async function demoLogin(email, password) {
    if (!isDemo) throw new Error('데모 모드가 아닙니다.')
    setAuthorization(`Basic ${btoa(`${email}:${password}`)}`)
    try {
      await api.get('/subscriptions', { params: { size: 1 } })
      setSession({
        email,
        role: email === 'admin@submate.test' ? 'ADMIN' : 'USER',
      })
    } catch (error) {
      setAuthorization(null)
      throw error
    }
  }
  function logout() {
    setAuthorization(null)
    setSession(null)
    onLogout?.()
  }
  return (
    <AuthContext.Provider value={{ session, demoLogin, logout }}>
      {children}
    </AuthContext.Provider>
  )
}
