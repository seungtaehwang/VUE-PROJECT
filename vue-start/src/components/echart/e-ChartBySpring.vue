<template>
  <div class="chart-container">
    <div class="chart-box" ref="chart1"></div>
    <div class="chart-box" ref="chart2"></div>
    <div class="chart-box" ref="chart3"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import axios from 'axios';

export default {
  data() {
    return {
      chartDataList: null, // 배열 형태로 3개 데이터셋을 기대하거나 단일 데이터는 복사
      myCharts: [],
      resizeHandler: null,
    };
  },
  mounted() {
    this.fetchDataAndDrawCharts();
  },
  methods: {
    async fetchDataAndDrawCharts() {
      try {
        // Spring Boot API 엔드포인트로 요청
        const response = await axios.get('http://localhost:8080/api/chart-data');
        // API가 단일 데이터 객체를 반환하면 세 차트에 동일한 데이터를 사용
        if (Array.isArray(response.data) && response.data.length >= 3) {
          this.chartDataList = response.data.slice(0, 3);
        } else {
          // 예: { categories: [...], values: [...] }
          this.chartDataList = [response.data, response.data, response.data];
        }
        this.drawCharts();
      } catch (error) {
        console.error("차트 데이터를 가져오는 중 오류 발생:", error);
      }
    },

    drawCharts() {
      // 기존 인스턴스가 있으면 해제
      if (this.myCharts && this.myCharts.length) {
        this.myCharts.forEach(chart => {
          try { chart.dispose(); } catch (e) {}
        });
        this.myCharts = [];
      }

      // 차트 타입을 예시로 지정 (Bar, Line, Pie)
      const chartTypes = ['bar', 'line', 'pie'];

      // refs 사용: this.$refs.chart1 / chart2 / chart3
      const refs = [this.$refs.chart1, this.$refs.chart2, this.$refs.chart3];

      refs.forEach((el, idx) => {
        if (!el) return;
        const chart = echarts.init(el);
        const data = this.chartDataList[idx] || this.chartDataList[0];

        let option = {};
        if (chartTypes[idx] === 'pie') {
          // 파이 차트용 포맷으로 변환
          const pieData = (data.categories || []).map((name, i) => ({ name, value: data.values ? data.values[i] : 0 }));
          option = {
            title: { text: 'Pie Chart' },
            tooltip: { trigger: 'item' },
            series: [
              {
                name: '비율',
                type: 'pie',
                radius: '50%',
                data: pieData,
                emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)' } }
              }
            ]
          };
        } else {
          option = {
            title: { text: chartTypes[idx] === 'bar' ? 'Bar Chart' : 'Line Chart' },
            tooltip: {},
            xAxis: { type: 'category', data: data.categories || [] },
            yAxis: { type: 'value' },
            series: [
              {
                name: '값',
                type: chartTypes[idx],
                data: data.yvalue || []
              }
            ]
          };
        }

        chart.setOption(option);
        this.myCharts.push(chart);
      });

      // 윈도우 리사이즈 핸들러 등록 (한 번만)
      if (!this.resizeHandler) {
        this.resizeHandler = () => {
          this.myCharts.forEach(c => { try { c.resize(); } catch (e) {} });
        };
        window.addEventListener('resize', this.resizeHandler);
      }
    }
  },
  // 컴포넌트가 파괴될 때 ECharts 인스턴스 정리
  beforeUnmount() {
    if (this.myCharts && this.myCharts.length) {
      this.myCharts.forEach(chart => {
        try { chart.dispose(); } catch (e) {}
      });
      this.myCharts = [];
    }
    if (this.resizeHandler) {
      window.removeEventListener('resize', this.resizeHandler);
      this.resizeHandler = null;
    }
  }
};
</script>

<style scoped>
.chart-container {
  display: grid;
  grid-template-columns: repeat(3, minmax(400px, 1fr));
  gap: 10px;
  width: 100%;
  padding: 10px;
}
.chart-box {
  min-width: 400px;
  height: 360px;
  border: 1px solid #eee;
  border-radius: 6px;
}
</style>