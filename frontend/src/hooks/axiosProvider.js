import axios from 'axios';
import {useAuth} from './useAuth.js';

export const useAxiosAuth = () => {
    const {auth} = useAuth();
    const axiosInstance = axios.create({
        baseURL: 'http://localhost:8080/api/v1',
    });

    axiosInstance.interceptors.request.use((config) => {
        if (auth.token) {
            config.headers.Authorization = `Bearer ${auth.token}`;
        }
        return config;
    });

    return axiosInstance;
};

export const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/api/v1',
});