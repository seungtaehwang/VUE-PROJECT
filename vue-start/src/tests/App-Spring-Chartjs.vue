<template>
  <div>
    <h1>우리 회사 매출 현황</h1>
    <!-- BarChart 컴포넌트 사용 및 데이터 바인딩 -->
    <DrawChart :chartData="salesData" :chartOptions="salesOptions" />
  </div>
</template>

<script>
import DrawChart from './components/chartjs/DrawChartjs.vue'; // BarChart 컴포넌트 import

export default {
  name: 'App',
  components: {
    DrawChart,
  },
  data() {
    return {
      salesData: { // App.vue에서 생성한 데이터 객체
        labels: ['1월', '2월', '3월', '4월', '5월', '6월'],
        datasets: [
          {
            label: '매출액',
            data: [120, 190, 3, 5, 2, 3],
            backgroundColor: 'rgba(75, 192, 192, 0.6)', // 막대 색상
          },
        ],
      },
      salesOptions: { // 옵션 객체 (필요시)
        responsive: true,
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      },
    };
  },
};
</script>