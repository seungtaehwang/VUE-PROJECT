import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import Test from './tests/TestSample.vue'
import TestRes from './tests/TestRealGrid.vue'
import eChartBySpring from './components/echart/e-ChartBySpring.vue'
import DrawTest from './tests/DrawTest.vue'

//createApp(App).mount('#app')
createApp(eChartBySpring).mount('#app')