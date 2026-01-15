<template>
  <div id="app">
    <!-- 상단 영역: 콤보박스 및 버튼 -->
    <div class="control-area">
      <select v-model="selectedOption" class="combo-box">
        <option disabled value="">옵션을 선택하세요</option>
        <option v-for="option in options" :key="option.value" :value="option.value">
          {{ option.text }}
        </option>
      </select>
      <button @click="loadData" class="action-button">데이터 불러오기</button>
    </div>

    <!-- 하단 영역: RealGrid -->
    <div class="grid-area">
      <!-- RealGrid 컨테이너 -->
      <div ref="realgridContainer" style="width: 100%; height: 500px;"></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { GridView, LocalDataProvider } from 'realgrid';
import 'realgrid/dist/realgrid-style.css'; // RealGrid 스타일 import

// Reactive 데이터 선언
const selectedOption = ref('');
const options = ref([
  { value: 'data1', text: '데이터셋 1' },
  { value: 'data2', text: '데이터셋 2' },
]);
const realgridContainer = ref(null);
let dataProvider;
let gridView;

// 버튼 클릭 시 실행될 함수
const loadData = () => {
  if (selectedOption.value) {
    console.log(`${selectedOption.value}에 대한 데이터 로드`);
    // 선택된 옵션에 따라 데이터 로직 구현
    const sampleData = getSampleData(selectedOption.value);
    dataProvider.setRows(sampleData.rows);
    gridView.setColumns(sampleData.columns);
  } else {
    alert('옵션을 선택해주세요.');
  }
};

// 샘플 데이터 반환 함수 (실제 데이터 연동 필요)
const getSampleData = (option) => {
  if (option === 'data1') {
    return {
      columns: [{ name: "col1", field: "field1", header: "컬럼1" }, { name: "col2", field: "field2", header: "컬럼2" }],
      rows: [{ field1: "값1-1", field2: "값1-2" }, { field1: "값2-1", field2: "값2-2" }]
    };
  } else if (option === 'data2') {
    return {
      columns: [{ name: "colA", field: "fieldA", header: "컬럼A" }, { name: "colB", field: "fieldB", header: "컬럼B" }],
      rows: [{ fieldA: "값A-1", fieldB: "값B-2" }, { fieldA: "값A-2", fieldB: "값B-2" }]
    };
  }
  return { columns: [], rows: [] };
};

// RealGrid 초기화
onMounted(() => {
  dataProvider = new LocalDataProvider(false);
  gridView = new GridView(realgridContainer.value);
  gridView.setDataSource(dataProvider);

  // 초기 컬럼 및 데이터 설정
  const initialData = getSampleData('data1');
  dataProvider.setFields(initialData.columns.map(col => ({ fieldName: col.field }))); // 필드 설정
  gridView.setColumns(initialData.columns);
  dataProvider.setRows(initialData.rows);
});

// 컴포넌트 언마운트 시 그리드 제거
onUnmounted(() => {
  if (gridView) {
    gridView.destroy();
  }
  if (dataProvider) {
    dataProvider.destroy();
  }
});
</script>

<style>
#app {
  font-family: Arial, sans-serif;
  padding: 20px;
}

.control-area {
  margin-bottom: 20px;
}

.combo-box, .action-button {
  padding: 8px 12px;
  margin-right: 10px;
}

.grid-area {
  border: 1px solid #ccc;
}
</style>