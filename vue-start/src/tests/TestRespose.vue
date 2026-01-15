<script>
import axios from 'axios';

export default {
  data() {
    return {
      seriesKey: '',
      users: [],
      tests: []
    };
  },
  created() {
    this.fetchUsers(); // 컴포넌트 생성 시 초기 데이터 로드
  },
  methods: {
    async fetchUsers() {
      try {
        // Axios의 params 옵션을 사용하여 쿼리 파라미터를 전달합니다.
        const response = await axios.get('http://localhost:8080/data_list',
          {
            params: {
              chartIndex: 1
            }
          } 
        );
        this.users = response.data; // Spring Boot에서 반환된 List<User>를 받습니다.
        console.log(response.data); // 콘솔에서 데이터 확인
      } catch (error) {
        console.error('Series 데이터를 불러오는 중 오류 발생:', error);
      }
    },
    async fetchTest() {
      try {
        // Axios의 params 옵션을 사용하여 쿼리 파라미터를 전달합니다.
        const response = await axios.get('http://localhost:5000/api/chartdata-get',
        {
          params:{category: 'test'}
        });
        this.tests = response.data; // Spring Boot에서 반환된 List<User>를 받습니다.
        console.log(response.data); // 콘솔에서 데이터 확인
      } catch (error) {
        console.error('Test 데이터를 불러오는 중 오류 발생:', error);
      }
    }
  }
};
</script>

<template>
  <div>
    <h1>Series Data</h1>
    <input v-model="seriesKey" placeholder="Series Key" />
    <button @click="fetchUsers">검색</button>
    <button @click="fetchTest">Test</button>
    <ul>
      <li v-for="user in tests">
        {{ user.name }} - {{ user.xvalue }} - {{ user.yvalue }}
      </li>
    </ul>
  </div>
</template>