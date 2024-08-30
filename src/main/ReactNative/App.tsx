import React from 'react';
import JoinFormMain from './Component/JoinComponent/JoinFormMain.tsx';
import LoginMain from './Component/LoginComponent/LoginMain.tsx';
import {NavigationContainer} from '@react-navigation/native';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import LoginForm from './Component/LoginComponent/LogionForm.tsx';
import {RootStackParamList} from './Types/RootStackParamList.ts';
import MapMain from './Component/MapCompoent/MapMain.tsx';
import ChatScreen from './Component/ChatComponent/ChatScreen.tsx';
import TabNavigation from "./Component/TabComponent/TabNavigation.tsx";
import { GestureHandlerRootView } from 'react-native-gesture-handler';
import { SafeAreaView, Text } from 'react-native';
import ChatRoomList from './Component/ChatComponent/ChatRoomList.tsx';

function App(): React.JSX.Element {
  const Stack = createNativeStackNavigator<RootStackParamList>();

  return (
      <GestureHandlerRootView style={{flex: 1}}>
        <SafeAreaView style={{flex: 1}}>
          <NavigationContainer>
            <Stack.Navigator initialRouteName="LoginMain" screenOptions={{ headerShown: false }}>
              <Stack.Screen name="Join" component={JoinFormMain} options={{ headerShown: false }}/>
              <Stack.Screen name="LoginMain" component={LoginMain} options={{ headerShown: false }}/>
              <Stack.Screen name="LoginForm" component={LoginForm} options={{ headerShown: false }}/>
              <Stack.Screen name="ChatScreen" component={ChatScreen} options={{ headerShown: false}}/>
              {/*<Stack.Screen name="MapMain" component={MapMain} options={{title: '지도'}} />*/}
              <Stack.Screen name="TabNavigation" component={TabNavigation} options={{ headerShown: false }} />
            </Stack.Navigator>
          </NavigationContainer>
        </SafeAreaView>
      </GestureHandlerRootView>
  );
}

export default App;
