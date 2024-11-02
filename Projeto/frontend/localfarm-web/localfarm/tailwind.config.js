/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './**/*.html',
    './**/*.js',
  ],
  theme: {
    extend: {
      colors: {
        'custom-green': '#405743',
        'custom-red-20': 'rgba(56, 21, 21, 0.20)',
        'custom-brown-25': 'rgba(80, 65, 62, 0.25)',
        'custom-beige': '#FFEBCA',
        'custom-orange': '#C2823A',
        'custom-brown': '#50413E',
        'custom-brown-20': 'rgba(80, 65, 62, 0.20)',
        'custom-orange-20': 'rgba(194, 130, 58, 0.20)',
        'custom-green-93': 'rgba(107, 155, 114, 0.93)',
        'custom-pink-87': 'rgba(255, 245, 245, 0.87)',
        'text-green': '#405743',
        'custom-green-95': 'rgba(95, 114, 97, 0.95)',
      },
      fontFamily: {
        'poppins': ['Poppins', 'sans-serif'],
      },
      lineHeight: {
        'tight-24': '24px',
        'tight-20': '20px',
      },
    },
  },
  plugins: [],
}
