import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api'; //'https://ecomsample-a4g8fhc7h2f6d0b6.canadacentral-01.azurewebsites.net/api';

// Create axios instance with default config
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Product API functions
export const productService = {
  // Get all products
  getAllProducts: () => api.get('/products'),
  
  // Get product by ID
  getProductById: (id) => api.get(`/products/${id}`),
  
  // Create new product
  createProduct: (product) => api.post('/products', product),
  
  // Update product
  updateProduct: (id, product) => api.put(`/products/${id}`, product),
  
  // Delete product
  deleteProduct: (id) => api.delete(`/products/${id}`),
  
  // Get products by category
  getProductsByCategory: (category) => api.get(`/products/category/${category}`),
  
  // Search products
  searchProducts: (name) => api.get(`/products/search?name=${name}`),
};

export default api;
