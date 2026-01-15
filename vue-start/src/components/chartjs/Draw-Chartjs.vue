<template>
  <div>
    <canvas ref="myChart"></canvas>
  </div>
</template>

<script>
import Chart from 'chart.js/auto'; // Chart.js 라이브러리 import

export default {
  name: 'Chartjs',
  props: {
    chartType: { // 부모로부터 받을 chart Type[bar, line, pie] prop 정의
      type: String,
      required: true,
    },
    chartData: { // 부모로부터 받을 데이터 prop 정의
      type: Object,
      required: true,
    },
    chartOptions: { // 옵션도 prop으로 받을 수 있음
      type: Object,
      default: () => ({}),
    },
  },
  data() {
    return {
      chartInstance: null,
    };
  },
  mounted() {
    this.createChart();
  },
  watch: {
    chartData: { // 데이터가 변경될 때마다 차트 업데이트
      handler() {
        if (this.chartInstance) {
          this.chartInstance.data = this.chartData;
          this.chartInstance.update();
        }
      },
      deep: true, // 데이터 객체 내부 변경 감지
    },
    chartOptions: { // 옵션 변경 시 업데이트
        handler() {
            if (this.chartInstance) {
                this.chartInstance.options = this.chartOptions;
                this.chartInstance.update();
            }
        },
        deep: true,
    }
  },
  methods: {
    createChart() {
      const ctx = this.$refs.myChart.getContext('2d');
      this.chartInstance = new Chart(ctx, {
        type: this.chartType,
        data: this.chartData, // 받은 데이터 사용
        options: this.chartOptions, // 받은 옵션 사용
      });
    },
  },
};
</script>