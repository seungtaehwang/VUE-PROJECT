import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import Test from './samples/HeaderTest.vue'
import TestRes from './samples/TestRespose.vue'
import ChartBySpring from './charts/ChartBySpring.vue'

//createApp(App).mount('#app')
createApp(TestRes).mount('#app')