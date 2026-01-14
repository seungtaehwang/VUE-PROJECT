<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import * as echarts from 'echarts'

// If `echarts` is not installed yet, run: npm install echarts

const props = defineProps({
  seriesList: {
    type: Array,
    default: () => [
      { name: 'Series A', data: [[10,22],[20,36],[30,10],[40,50],[50,25]] },
      { name: 'Series B', data: [[12,30],[25,10],[35,40],[55,20],[65,35]] },
      { name: 'Series C', data: [[15,15],[45,44],[60,20],[70,60],[30,45]] }
    ]
  },

  chartType: {
    type: String,
    default: 'scatter'
  },

  chartTile: {
    type: String,
    default: 'Scatter Chart — Multiple Series'
  } 

})

const chartContainer = ref(null)
let chart = null
const fallbackSeries = [
  { name: 'Series A', data: [[10,22],[20,36],[30,10],[40,50],[50,25]] },
  { name: 'Series B', data: [[12,30],[25,10],[35,40],[55,20],[65,35]] }
]
const localSeries = ref((props.seriesList && props.seriesList.length) ? props.seriesList.slice() : fallbackSeries.slice())

let ro = null

onMounted(() => {
  if (chartContainer.value) {
    chart = echarts.init(chartContainer.value)
    // ensure layout is settled before rendering
    requestAnimationFrame(() => {
      setOption()
      if (chart) chart.resize()
      // warn if container has no size
      const el = chartContainer.value
      if (el && (el.clientWidth === 0 || el.clientHeight === 0)) {
        console.warn('SelectChart: container size is 0 — chart may not display until it has a size')
      }
    })

    // observe size changes and resize chart accordingly
    if (window.ResizeObserver) {
      ro = new ResizeObserver(() => {
        if (chart) {
          chart.resize()
          setOption()
        }
      })
      ro.observe(chartContainer.value)
    }

    window.addEventListener('resize', resizeChart)
  }
})


function resizeChart() {
  if (chart) chart.resize()
}

function setOption() {
  if (!chart) return
  const type = props.chartType || 'scatter'
  let option = {
    title: { text: props.chartTile || (type === 'box' ? 'Box Plot' : 'Chart') },
    tooltip: { trigger: 'item' },
    legend: {},
    xAxis: {},
    yAxis: {},
    series: []
  }

  if (type === 'scatter') {
    option.legend = { data: localSeries.value.map(s => s.name) }
    option.xAxis = { type: 'value' }
    option.yAxis = { type: 'value' }
    option.series = localSeries.value.map(s => ({
      name: s.name,
      type: 'scatter',
      data: s.data,
      symbolSize: s.symbolSize || 8
    }))
  } else if (type === 'bar') {
    const maxLen = Math.max(...localSeries.value.map(s => s.data.length))
    const categories = Array.from({ length: maxLen }, (_, k) => String(k + 1))
    option.legend = { data: localSeries.value.map(s => s.name) }
    option.xAxis = { type: 'category', data: categories }
    option.yAxis = { type: 'value' }
    option.series = localSeries.value.map(s => ({
      name: s.name,
      type: 'bar',
      data: Array.from({ length: maxLen }, (_, k) => (s.data[k] ? s.data[k][1] : null))
    }))
  } else if (type === 'box') {
    // compute boxplot data per series from y-values
    const computeMedian = (arr) => {
      if (!arr.length) return 0
      const m = arr.length
      const mid = Math.floor(m / 2)
      return m % 2 ? arr[mid] : (arr[mid - 1] + arr[mid]) / 2
    }
    const boxData = localSeries.value.map(s => {
      const ys = s.data.map(p => p[1]).slice().sort((a, b) => a - b)
      if (!ys.length) return [0, 0, 0, 0, 0]
      const min = ys[0]
      const max = ys[ys.length - 1]
      const m = Math.floor(ys.length / 2)
      const lower = ys.slice(0, m)
      const upper = ys.slice((ys.length % 2) ? m + 1 : m)
      const q1 = computeMedian(lower)
      const q2 = computeMedian(ys)
      const q3 = computeMedian(upper)
      return [min, q1, q2, q3, max]
    })
    option.xAxis = { type: 'category', data: localSeries.value.map(s => s.name) }
    option.yAxis = { type: 'value' }
    option.series = [
      { name: 'box', type: 'boxplot', data: boxData }
    ]
  }

  chart.setOption(option, true)
}

watch(() => props.seriesList, (newVal) => {
  if (newVal && newVal.length) {
    localSeries.value = newVal.slice()
  }
  setOption()
}, { deep: true })

watch(() => props.chartType, () => {
  setOption()
})

</script>

<template>
  <div class="chart-wrap">
    <div ref="chartContainer" class="chart"></div>
  </div>
</template>

<style scoped>
.chart-wrap { width: 100%; height: 100%; display: flex; flex-direction: column; }
.chart { flex: 1; min-height: 120px; width: 100%; }
</style>