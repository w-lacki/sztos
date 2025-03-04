import {jwtDecode} from "jwt-decode";
import {createContext, useEffect, useState} from 'react';

export const AuthContext = createContext();

export const AuthProvider = ({children}) => {
    const [auth, setAuth] = useState({
        token: localStorage.getItem('token') || null,
        refreshToken: localStorage.getItem('refreshToken') || null,
        isAuthenticated: !!localStorage.getItem('token'),
        loading: true, // Track initial auth check
        roles: []
    });

    useEffect(() => {
        const verifyToken = async () => {
            const token = localStorage.getItem('token');

            const refreshToken = localStorage.getItem('refreshToken');

            if (token && refreshToken) {
                const decodedToken = jwtDecode(token)
                const rolesString = decodedToken.roles.replace(/(\w+)/g, '"$1"');
                const roles = JSON.parse(rolesString)
                const response = await fetch('http://localhost:8080/api/v1/user/me', {
                    headers: {Authorization: `Bearer ${token}`},
                });
                if (response.ok) {
                    setAuth({token, refreshToken, isAuthenticated: true, loading: false, roles: roles});
                } else {
                    localStorage.removeItem('token');
                    localStorage.removeItem('refreshToken');
                    setAuth({token: null, refreshToken: null, isAuthenticated: false, loading: false, roles: []});
                }
            } else {
                setAuth({token: null, refreshToken: null, isAuthenticated: false, loading: false, roles: []});
            }
        };
        verifyToken();
    }, []);

    const login = (token, refreshToken) => {
        localStorage.setItem('token', token);
        localStorage.setItem('refreshToken', refreshToken);
        const decodedToken = jwtDecode(token)
        const rolesString = decodedToken.roles.replace(/(\w+)/g, '"$1"');
        const roles = JSON.parse(rolesString)
        setAuth({token, refreshToken, isAuthenticated: true, loading: false, roles});
    };

    const logout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem("refreshToken");
        setAuth({token: null, refreshToken: null, isAuthenticated: false, loading: false, roles: []});
    };

    return (
        <AuthContext.Provider value={{auth, login, logout}}>
            {children}
        </AuthContext.Provider>
    );
};