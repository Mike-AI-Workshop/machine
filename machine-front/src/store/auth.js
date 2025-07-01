import { defineStore } from 'pinia';
import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import router from '../router';

export const useAuthStore = defineStore('auth', {
    state: () => ({
        token: localStorage.getItem('token') || null,
        user: JSON.parse(localStorage.getItem('user')) || null,
        isLoginModalVisible: false,
    }),
    getters: {
        isAuthenticated: (state) => !!state.token,
        isAdmin: (state) => state.user?.role === 'ROLE_ADMIN',
    },
    actions: {
        async login(credentials) {
            try {
                const response = await axios.post('/api/auth/login', credentials);
                const token = response.data.token;
                
                const decoded = jwtDecode(token);
                
                this.token = token;
                this.user = {
                    username: decoded.sub,
                    role: decoded.role
                };

                localStorage.setItem('token', token);
                localStorage.setItem('user', JSON.stringify(this.user));

                axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
                
                this.closeLoginModal();
            } catch (error) {
                console.error('Login failed:', error);
                // Here you can add user-facing error handling, e.g., using ElMessage
                throw error;
            }
        },
        async register(credentials) {
            try {
                // 注册成功后，后端会返回成功消息，但不会自动登录
                await axios.post('/api/auth/register', credentials);
            } catch (error) {
                console.error('Registration failed:', error);
                throw error;
            }
        },
        logout() {
            this.token = null;
            this.user = null;
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            delete axios.defaults.headers.common['Authorization'];
            router.push('/');
        },
        initialize() {
            if (this.token) {
                 axios.defaults.headers.common['Authorization'] = `Bearer ${this.token}`;
            }
        },
        openLoginModal() {
            this.isLoginModalVisible = true;
        },
        closeLoginModal() {
            this.isLoginModalVisible = false;
        }
    },
}); 