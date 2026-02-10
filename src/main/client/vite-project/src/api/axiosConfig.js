import axios from 'axios';
/**
* Create a custom axios instance with default configuration
* This centralizes all API configuration in one place.
*/

const axiosInstance = axios.create({
// Base url for all requests
// In development: Vite proxy forwards /api to http://localhost:8080
// In production: use environment variable
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  // Timeout after 10 seconds
    timeout: 10000,

    // Default headers for all requests
    headers: {
      'Content-Type': 'application/json',
    },

    // Include credentials (cookies, authorization headers) with requests
    // Matches your Spring Boot CORS config: .allowCredentials(true)
    withCredentials: true,
});
/**
 * Response Interceptor
 * Runs after every response is received
 * Useful for global error handling, token refresh, etc.
 */

 axiosInstance.interceptors.response.use(
   (response) => {
     // Any status code within 2xx triggers this function
     // Log successful responses
     console.log('Response received:', response.status, response.data);
     return response;
   },
   (error) => {
     // Any status code outside 2xx triggers this function

     // Handle different error scenarios
     if (error.response) {
       // Server responded with error status (4xx, 5xx)
       console.error('Response error:', error.response.status, error.response.data);

       // Handle specific status codes globally
       switch (error.response.status) {
         case 401:
           // Unauthorized - redirect to login
           console.error('Unauthorized! Please log in.');
           // window.location.href = '/login';
           break;
         case 403:
           // Forbidden
           console.error('Access forbidden!');
           break;
         case 404:
           // Not found
           console.error('Resource not found!');
           break;
         case 500:
           // Server error
           console.error('Server error! Please try again later.');
           break;
         default:
           console.error('An error occurred:', error.response.data);
       }
     } else if (error.request) {
       // Request was made but no response received (network error)
       console.error('Network error - no response received:', error.request);
     } else {
       // Something else happened
       console.error('Error:', error.message);
     }

     return Promise.reject(error);
   }
 );

 export default axiosInstance;