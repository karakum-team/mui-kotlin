import {cwd} from 'node:process'
import {defineConfig, loadEnv} from 'vite'

export default defineConfig(({mode}) => {
    const env = loadEnv(mode, cwd(), '')
    return {
        root: 'kotlin',
        server: {
            fs: {
                allow: ['..'],
            },
        },
        build: {
            rollupOptions: {
                input: [
                    env.ENTRY_PATH,
                ],
                output: {
                    entryFileNames: '[name].js',
                },
            },
        },
    }
})
