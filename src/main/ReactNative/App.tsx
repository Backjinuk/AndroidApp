import React from 'react';
import JoinFormMain from "./Component/JoinComponent/JoinFormMain.tsx";
import LoginMain from "./Component/LoginComponent/LoginMain.tsx";
import { NavigationContainer } from "@react-navigation/native";
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import LoginForm from "./Component/LoginComponent/LogionForm.tsx";
import {RootStackParamList} from "./CommonTypes/RootStackParamList.ts";
import MapMain from "./Component/MapCompoent/MapMain.tsx";




function App(): React.JSX.Element {
    const Stack = createNativeStackNavigator<RootStackParamList>();

    return (
        <NavigationContainer>
            <Stack.Navigator initialRouteName="LoginMain">
                <Stack.Screen name="Join" component={JoinFormMain}/>
                <Stack.Screen name="LoginMain" component={LoginMain}/>
                <Stack.Screen name="LoginForm" component={LoginForm}/>
                <Stack.Screen name={"MapMain"} component={MapMain} />
            </Stack.Navigator>
        </NavigationContainer>
    );
}

export default App;
