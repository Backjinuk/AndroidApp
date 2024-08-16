import React from 'react';
import JoinFormMain from './Component/JoinComponent/JoinFormMain.tsx';
import LoginMain from './Component/LoginComponent/LoginMain.tsx';
import {NavigationContainer} from '@react-navigation/native';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import LoginForm from './Component/LoginComponent/LogionForm.tsx';
import {RootStackParamList} from './CommonTypes/RootStackParamList.ts';
import MapMain from './Component/MapCompoent/MapMain.tsx';
import ChatScreen from './Component/ChatComponent/ChatScreen.tsx';
import TabNavigation from "./Component/TabComponent/TabNavigation.tsx";

function App(): React.JSX.Element {
  const Stack = createNativeStackNavigator<RootStackParamList>();

  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="LoginMain" screenOptions={{ headerShown: false }} >
        <Stack.Screen name="Join" component={JoinFormMain} options={{ headerShown: false }}/>
        <Stack.Screen name="LoginMain" component={LoginMain} options={{ headerShown: false }}/>
        <Stack.Screen name="LoginForm" component={LoginForm} options={{ headerShown: false }}/>
        <Stack.Screen name="ChatScreen" component={ChatScreen} options={{ headerShown: false }}/>
        {/*<Stack.Screen name="MapMain" component={MapMain} options={{title: '지도'}} />*/}
        <Stack.Screen name="TabNavigation" component={TabNavigation} options={{ headerShown: false }} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

export default App;
