import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const OAuthCallbackPage: React.FC = () => {
    const navigate = useNavigate();
    const { login } = useAuth();

    useEffect(() => {
        const params = new URLSearchParams(window.location.search);
        const token = params.get('token');

        if (token) {
            login(token);
            // navigate() 사용 시 React 상태 업데이트 전에 PrivateRoute가 실행되어
            // isAuthenticated=false로 판단, /login으로 튕기는 문제가 있음.
            // 하드 리다이렉트로 페이지를 새로 로드하면 AuthProvider가
            // localStorage에서 토큰을 읽어 isAuthenticated=true로 초기화됨.
            window.location.href = '/';
        } else {
            // 토큰이 없으면 로그인 페이지로 이동
            navigate('/login', { replace: true });
        }
    }, [login, navigate]);

    return <div>로그인 처리 중...</div>;
};

export default OAuthCallbackPage;
