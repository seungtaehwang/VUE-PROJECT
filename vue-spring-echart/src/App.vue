<template>
  <div class="controls top-controls">
    <button :class="{active: selectedType === 'scatter'}" @click="selectedType = 'scatter'">Scatter</button>
    <button :class="{active: selectedType === 'bar'}" @click="selectedType = 'bar'">Bar</button>
    <button :class="{active: selectedType === 'box'}" @click="selectedType = 'box'">Box</button>
  </div>
  <main>
    <div class="grid">
      <SelectChart v-for="i in charts" :key="i" :ckey="i" :seriesList="chartSeries[i]" :chartTile="`Scatter Chart ${i+1}`" :chartType="selectedType" />
    </div>
  </main>
</template>

<script>
import { ref } from 'vue'
import SelectChart from './charts/SelectChart.vue'
import axios from 'axios';

export default {
  data() {
    return {
      ckey: '',
      name: '',
      data: [],
      chartSeries: Array.from({ length: 15 }, () => []),
      charts: Array.from({ length: 15 }, (_, i) => i),
      selectedType: 'scatter'
    };
  },
  created() {
    this.loadAllSeries(); // load series data for all charts
  },
  methods: {
    async fetchSeries(i) {
      try {
        const response = await axios.get('http://localhost:8080/data_list', {
          params: { chartIndex: i + 1 }
        });
        const series = [];
        let findKey = '';
        let newData = [];
        for (const item of response.data) {
          if (item.name !== findKey) {
            if (findKey !== '') {
              series.push({ name: findKey, data: newData });
            }
            findKey = item.name;
            newData = [];
          }
          newData.push([item.xvalue, item.yvalue]);
          console.log(findKey + ' : ' + item.xvalue + ', ' + item.yvalue);
        }
        if (findKey !== '') series.push({ name: findKey, data: newData });
        return series;
      } catch (error) {
        console.error('Series 데이터를 불러오는 중 오류 발생:', error);
        return [];
      }
    },
    async loadAllSeries() {
      const cnt = 15;
      for (let i = 0; i < cnt; i++) {
        this.chartSeries[i] = await this.fetchSeries(i);
      }
    }
  }
};



</script>



<style scoped>

/* 3x3 chart grid - wider charts */
.grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(400px, 1fr));
  gap: 5px;
  align-items: stretch;
  grid-auto-rows: 400px; /* taller for better aspect */
  max-width: 1500px;
  width: calc(100% - 2rem);
  margin: 0 auto;
}

.grid > * {
  min-width: 0;
}
.top-controls {
  width: calc(100% - 2rem);
  max-width: 1500px;
  margin: 12px auto;
  display: flex;
  justify-content: flex-start;
  align-items: center ;
  padding-left: 0;
  gap: 3px;
}
.controls button { padding: 8px 12px; border: 1px solid #ccc; background: #fff; cursor: pointer; border-radius: 4px; }
.controls button.active { background: #0ea5e9; color: #fff; border-color: #0ea5e9; }

/* responsive breakpoints */
@media (max-width: 1500px) {
  .grid {
    grid-template-columns: repeat(3, minmax(500px, 1fr));
    grid-auto-rows: 400px;
  }
}

</style>
