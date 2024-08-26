import {Text, View} from "react-native";
import React, {useEffect, useState} from "react";
import axiosPost from "../../../Util/AxiosUtil.ts";
import SubscribeInfoCard from "../ProfileCard/SubscribeInfoCard.tsx";
import styles from "../ProfileCard/styles.ts";
import {createMaterialTopTabNavigator} from "@react-navigation/material-top-tabs";
import ApplyCommunityList from "./ApplyCommunityList.tsx";

const TopTab = createMaterialTopTabNavigator();

export default function SubscriptionList(){
    const [subscribeList, setSubscribeList] = useState<Subscribe[]>([])

    useEffect(() => {
        axiosPost.post('/subscribe/getSubscribeList')
            .then((res) => {
                setSubscribeList(res.data.subscriberDtoList)
            })
    }, []);


    return(
        <>
        <TopTab.Navigator
            screenOptions={({ route }) => ({
                headerShown: false,
                tabBarActiveTintColor: '#63CC63', // 활성화된 탭의 색상
                tabBarInactiveTintColor: 'gray', // 비활성화된 탭의 색상
            })}
        >
            <TopTab.Screen
                name="구독목록"
                component={SubscriptionList}
            />
            <TopTab.Screen
                name="신청목록"
                component={ApplyCommunityList}
            />
        </TopTab.Navigator>


            <View style={styles.container}>
                {subscribeList.map( (subscribe, index) => {
                    return (
                        <SubscribeInfoCard/>
                    )
                })}
            </View>
        </>
    )
}