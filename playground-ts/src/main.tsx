import React from 'react'
import './index.css'
import App from './App'
import {createRoot} from "react-dom/client"

const container = document.createElement('div')

createRoot(container).render(
    <React.StrictMode>
        <App/>
    </React.StrictMode>
)

document.body.appendChild(container)
