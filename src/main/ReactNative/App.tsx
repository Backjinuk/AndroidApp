import React from 'react';
import JoinFormMain from './Component/JoinComponent/JoinFormMain.tsx';
import LoginMain from './Component/LoginComponent/LoginMain.tsx';
import {NavigationContainer} from '@react-navigation/native';
import {createNativeStackNavigator} from '@react-navigation/native-stack';
import LoginForm from './Component/LoginComponent/LogionForm.tsx';
import {RootStackParamList} from './CommonTypes/RootStackParamList.ts';
import MapMain from './Component/MapCompoent/MapMain.tsx';
import ChatScreen from './Component/ChatComponent/ChatScreen.tsx';

function App(): React.JSX.Element {
  const Stack = createNativeStackNavigator<RootStackParamList>();

  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="LoginForm">
        <Stack.Screen name="Join" component={JoinFormMain} />
        <Stack.Screen name="LoginMain" component={LoginMain} />
        <Stack.Screen name="LoginForm" component={LoginForm} />
        <Stack.Screen name="ChatScreen" component={ChatScreen} />
        <Stack.Screen
          name="MapMain"
          component={MapMain}
          options={{title: '지도'}}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

export default App;
