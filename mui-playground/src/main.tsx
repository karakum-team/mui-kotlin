import React from 'react'
import ReactDOM from 'react-dom'
import './index.css'
import App from './App'

const element = <React.StrictMode>
  <App/>
</React.StrictMode>

const container = document.createElement('div')
document.body.appendChild(container)

ReactDOM.render(element, container)
