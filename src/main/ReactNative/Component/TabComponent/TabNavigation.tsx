import React from 'react';
import ApplyCommunityList from './Tabs/ApplyCommunityList';
import UserInfo from './Tabs/UserInfo';
import CommunityCalendar from './Tabs/CommunityCalendar';
import MapMain from "../MapCompoent/MapMain.tsx";
import Icon from "react-native-vector-icons/FontAwesome";
import { Animated, ColorValue, View } from "react-native";
import { createMaterialBottomTabNavigator } from '@react-navigation/material-bottom-tabs';
import ChatRoomList from "../ChatComponent/ChatRoomList.tsx";
import { createMaterialTopTabNavigator } from "@react-navigation/material-top-tabs";
import SubscribeInfoCard from "./ProfileCard/SubscribeInfoCard.tsx";
import SubscriptionList from "./Tabs/SubscriptionList.tsx";

const Tab = createMaterialBottomTabNavigator();
const TopTab = createMaterialTopTabNavigator();

// TabIcon 컴포넌트
const TabIcon = (name: string, size: number | undefined, color: number | ColorValue | undefined, focused: boolean) => {
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

// TopTabNavigator를 컴포넌트 외부로 이동
const TopTabNavigator = () => {
    return (
        <TopTab.Navigator
            screenOptions={({ route }) => ({
                headerShown: false,
                tabBarActiveTintColor: '#63CC63',
                tabBarInactiveTintColor: 'gray',
            })}
        >
            <TopTab.Screen
                name="구독"
                component={SubscriptionList} // 구독 목록 화면
            />
            <TopTab.Screen
                name="신청"
                component={ApplyCommunityList} // 신청 목록 화면
            />
        </TopTab.Navigator>
    );
};

export default function TabNavigation() {
    return (
        <Tab.Navigator
            screenOptions={({ route }) => ({
                headerShown: false,
                tabBarIcon: ({ focused, color }) => {
                    let iconName = "";

                    if (route.name === '지도') {
                        iconName = focused ? 'map' : 'map-o';
                    }  else if (route.name === '구독목록') {
                        iconName = focused ? 'heart' : 'heart-o';
                    } else if (route.name === '캘린더') {
                        iconName = focused ? 'calendar' : 'calendar';
                    } else if (route.name === '내정보') {
                        iconName = focused ? 'user-circle' : 'user-circle-o';
                    } else if (route.name === '채팅') {
                        iconName = focused ? 'comment' : 'comment-o';
                    }

                    return TabIcon(iconName, 20, color, focused);
                },
                tabBarActiveTintColor: '#63CC63',
                tabBarInactiveTintColor: 'gray',
            })}
        >
            <Tab.Screen
                name="지도"
                component={MapMain}
            />
            <Tab.Screen
                name="캘린더"
                component={CommunityCalendar}
            />
            <Tab.Screen
                name="구독목록"
                component={TopTabNavigator} // 상단 탭 네비게이션을 연결
            />
            <Tab.Screen
                name="채팅"
                component={ChatRoomList}
            />
            <Tab.Screen
                name="내정보"
                component={UserInfo}
            />
        </Tab.Navigator>
    );
}
