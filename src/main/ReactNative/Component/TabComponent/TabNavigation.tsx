import React from 'react';

import ApplyCommunityList from './Tabs/ApplyCommunityList';
import SubscriptionList from './Tabs/SubscriptionList';
import UserInfo from './Tabs/UserInfo';
import CommunityCalendar from './Tabs/CommunityCalendar';
import MapMain from "../MapCompoent/MapMain.tsx";
import Icon from "react-native-vector-icons/FontAwesome";
import {Animated, ColorValue} from "react-native";
import { createMaterialBottomTabNavigator } from '@react-navigation/material-bottom-tabs';
import {createBottomTabNavigator} from "@react-navigation/bottom-tabs";

const Tab = createMaterialBottomTabNavigator();

const TabIcon = (name: string, size: number | undefined, color: string | ColorValue | undefined,  focused: any ) => {
    const scale = focused ? new Animated.Value(1.2) : new Animated.Value(1);

    Animated.timing(scale, {
        toValue: focused ? 1.2 : 1,
        duration: 200,
        useNativeDriver: true,
    }).start();

    return (
        <Animated.View style={{ transform: [{ scale }] }}>
            <Icon name={name} size={size} color={color} />
        </Animated.View>
    );
};

export default function TabNavigation() {
    return (
        <Tab.Navigator
            screenOptions={({ route }) => ({
                headerShown: false,
                tabBarIcon: ({ focused, color}) => {
                    let iconName = "";

                    if (route.name === '지도') {
                        iconName = focused ? 'map' : 'map-o';
                    } else if (route.name === '신청목록') {
                        iconName = focused ? 'list' : 'th-list';
                    } else if (route.name === '구독목록') {
                        iconName = focused ? 'heart' : 'heart-o';
                    } else if (route.name === '캘린더') {
                        iconName = focused ? 'calendar' : 'calendar-alt';
                    } else if (route.name === '내정보') {
                        iconName = focused ? 'user-circle' : 'user-circle-o';
                    }

                    return TabIcon(iconName, 20, color, focused);
                },
                tabBarActiveTintColor: '#63CC63', // 활성화된 탭의 색상
                tabBarInactiveTintColor: 'gray', // 비활성화된 탭의 색상
            })}
        >
            <Tab.Screen
                name="지도"
                component={MapMain}
            />
            <Tab.Screen
                name="신청목록"
                component={ApplyCommunityList}
            />
            <Tab.Screen
                name="구독목록"
                component={SubscriptionList}
            />
            <Tab.Screen
                name="캘린더"
                component={CommunityCalendar}
            />
            <Tab.Screen
                name="내정보"
                component={UserInfo}
            />
        </Tab.Navigator>
    );
}
