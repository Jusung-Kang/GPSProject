import React from 'react';
/*
    StatusBar: 스마트폰 맨 위에 시간, 배터리 잔량, Wi-Fi 신호 등 상태 표시줄을 제어하기위해 필요
    StyleSheet: CSS와 유사하게 React-Native 컴포넌트들의 스타일을 정의하고 최적화 하는데 사용
    useColorScheme: 기기 설정이 라이트모드인지 다크모드인지 알아내기위해 사용
    View: 화면에 내용을 배치하는 가장 기본적인 상자. 웹개발의 <div>와 비슷하다고 생각하면 된다.
*/
import { StatusBar, StyleSheet, useColorScheme, View } from 'react-native';
/*
    SafeAreaProvider: 아이폰의 상단 카메라부분, 안드로이드의 상태표시줄/네비게이션바영역 처럼, 앱 콘텐츠가 시스템 UI에 가려지지 안도록
                      안전영역을 계산하고 관리해주는 최상위 컴포넌트. 앱전체를 SafeAreaProvider로 감싸주는게 일반적.
    useSafeAreaInsets: SafeAreaProvider가 계산한 안전영역의 실제간격을 컴포넌트 내에서 직접 가져와서 사용하고 싶을때 사용.
*/

import {
  SafeAreaProvider,
  useSafeAreaInsets,
} from 'react-native-safe-area-context';
import Main from './Main';

function App() {
  const isDarkMode = useColorScheme() === 'dark';

  return (
    <SafeAreaProvider>
      <StatusBar barStyle={isDarkMode ? 'light-content' : 'dark-content'} />
      <Main />
    </SafeAreaProvider>
  );
}





export default App;
