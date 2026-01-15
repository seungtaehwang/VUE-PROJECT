<!-- HeaderComponent.vue -->
<template>
  <header class="app-header">
    <div class="logo">내 애플리케이션</div>
    
    <!-- 여기에 div 버튼 추가 -->
    <div class="header-button" @click="handleButtonClick">
      마이페이지
    </div>
  </header>
</template>

<script>
export default {
  name: 'HeaderComponent',
  methods: {
    handleButtonClick() {
      // 버튼 클릭 시 실행할 로직
      console.log('헤더 버튼이 클릭되었습니다.');
      alert('버튼이 클릭되었습니다!');
      // 예: 라우터 이동
      // this.$router.push('/mypage'); 
    }
  }
}
</script>

<style scoped>
.app-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #f4f4f4;
  border-bottom: 1px solid #ddd;
}

.logo {
  font-size: 1.5rem;
  font-weight: bold;
}

/* div 버튼 스타일링 */
.header-button {
  padding: 8px 15px;
  background-color: #007bff; /* 파란색 배경 */
  color: white; /* 흰색 글씨 */
  border-radius: 4px; /* 둥근 모서리 */
  cursor: pointer; /* 마우스 오버 시 손가락 모양으로 변경 */
  transition: background-color 0.3s;
}

.header-button:hover {
  background-color: #0056b3; /* 호버 시 색상 변경 */
}
</style>