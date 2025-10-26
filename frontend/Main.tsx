import React from 'react';
import { View, Text, StyleSheet } from 'react-native';

const Main = () => {
    return (
        <View style={styles.container}>
            <Text style={styles.title}>Welcome to the Homepage!</Text>
            <Text style={styles.subtitle}>This is the main screen of your app.</Text>
        </View>
    )
}

const styles = StyleSheet.create({
  container: {
    flex: 1, // 화면 전체를 차지하도록 설정
    justifyContent: 'center', // 내용을 세로 중앙에 정렬
    alignItems: 'center', // 내용을 가로 중앙에 정렬
    padding: 20, // 내부 여백 추가
    backgroundColor: '#f5f5f5', // 배경색 설정 (선택 사항)
  },
  title: {
    fontSize: 24, // 제목 글자 크기
    fontWeight: 'bold', // 굵은 글씨체
    marginBottom: 10, // 아래쪽 여백
  },
  subtitle: {
    fontSize: 16, // 부제목 글자 크기
    color: 'gray', // 글자색 설정
  },
});

export default Main;