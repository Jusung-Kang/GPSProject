// frontend/src/api.ts
import axios from 'axios';

// 안드로이드 에뮬레이터에서 PC의 localhost(Spring Boot)에 접속하는 주소
const baseURL = 'http://10.0.2.2:8080';

const apiClient = axios.create({
  baseURL: baseURL,
});

export default apiClient;