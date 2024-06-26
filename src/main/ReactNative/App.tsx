import React from 'react';
import JoinFormMain from "./Component/JoinComponent/JoinFormMain.tsx";
import LoginMain from "./Component/LoginComponent/LoginMain.tsx";
import { NavigationContainer } from "@react-navigation/native";
import { createNativeStackNavigator } from '@react-navigation/native-stack';

type RootStackParamList = {
    Login: undefined;
    Join: undefined;
};


function App(): React.JSX.Element {
    const Stack = createNativeStackNavigator<RootStackParamList>();

    return (
        <NavigationContainer>
            <Stack.Navigator initialRouteName="Login">
                <Stack.Screen name="Join" component={JoinFormMain}/>
                <Stack.Screen name="Login" component={LoginMain}/>
            </Stack.Navigator>
        </NavigationContainer>
    );
}

export default App;
