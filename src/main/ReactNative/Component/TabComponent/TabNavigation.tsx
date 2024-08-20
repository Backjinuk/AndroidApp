import React from 'react';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import ApplyCommunityList from './Tabs/ApplyCommunityList';
import SubscriptionList from './Tabs/SubscriptionList';
import UserInfo from './Tabs/UserInfo';
import CommunityCalendar from './Tabs/CommunityCalendar';
import MapMain from '../MapCompoent/MapMain.tsx';
import ChatRoomList from '../ChatComponent/ChatRoomList.tsx';

const Tab = createBottomTabNavigator();

export default function TabNavigation() {
  return (
    <Tab.Navigator screenOptions={{headerShown: false}}>
      <Tab.Screen name="지도" component={MapMain} />
      <Tab.Screen name="신청목록" component={ApplyCommunityList} />
      <Tab.Screen name="구독목록" component={SubscriptionList} />
      <Tab.Screen name="캘린더" component={CommunityCalendar} />
      <Tab.Screen name="내정보" component={UserInfo} />
      <Tab.Screen name="채팅" component={ChatRoomList} />
    </Tab.Navigator>
  );
}
