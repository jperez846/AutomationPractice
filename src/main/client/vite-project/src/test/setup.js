/**
 * Test Setup File
 * Runs once before all tests
 * 
 * Purpose:
 * - Import global test utilities
 * - Configure testing library
 * - Set up global mocks
 * - Add custom matchers
 */

// Import jest-dom matchers
// Provides custom matchers like .toBeInTheDocument(), .toHaveTextContent(), etc.
import '@testing-library/jest-dom';

/**
 * Global Mock for console methods
 * Prevents cluttering test output with expected errors/warnings
 */
global.console = {
  ...console,
  // Uncomment to suppress console.error in tests
  // error: vi.fn(),
  // Uncomment to suppress console.warn in tests
  // warn: vi.fn(),
};

/**
 * Mock window.matchMedia
 * Many UI libraries use matchMedia which isn't available in jsdom
 */
Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: vi.fn().mockImplementation(query => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: vi.fn(), // Deprecated
    removeListener: vi.fn(), // Deprecated
    addEventListener: vi.fn(),
    removeEventListener: vi.fn(),
    dispatchEvent: vi.fn(),
  })),
});