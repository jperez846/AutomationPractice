import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],

  // ============================================================================
  // DEVELOPMENT SERVER CONFIGURATION
  // ============================================================================
  server: {
    // Port for dev server
    port: 3000,

    // Proxy API requests to backend during development
    // This is ONLY used in development (npm run dev)
    // In production (Docker), Nginx handles proxying
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      }
    }
  },

  // ============================================================================
  // BUILD CONFIGURATION
  // ============================================================================
  build: {
    // Output directory (where built files go)
    outDir: 'dist',

    // Clean output directory before build
    emptyOutDir: true,

    // Generate sourcemaps for debugging (disable in production)
    sourcemap: false,

    // Optimize for production
    minify: 'esbuild',

    // Chunk size warnings
    chunkSizeWarningLimit: 1000,
  },

  // ============================================================================
  // TESTING CONFIGURATION
  // ============================================================================
  test: {
    environment: 'jsdom',
    setupFiles: './src/test/setup.js',
    globals: true,
    coverage: {
      provider: 'v8',
      reporter: ['text', 'json', 'html'],
    },
  },
})