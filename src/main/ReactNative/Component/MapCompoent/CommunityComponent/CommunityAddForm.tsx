import React, {useEffect, useState} from 'react';
import {Alert, Button, Modal, ScrollView, Text, TextInput, TouchableOpacity, View} from 'react-native';
import styles from "./styles";
import DatePicker from "react-native-date-picker";
import axiosPost from "../../../Util/AxiosUtil.ts";
import {useNavigation} from "@react-navigation/native";
import {NativeStackNavigationProp} from "@react-navigation/native-stack";
import {RootStackParamList} from "../../../CommonTypes/RootStackParamList.ts";

export default function CommunityAddForm(props: any) {

    const [commuTitle, setCommuTitle] = useState('');
    const [commuComent, setCommuComent] = useState('');
    const [commuMeetingTime, setCommuMeetingTime] = useState(new Date());
    const [showDatePicker, setShowDatePicker] = useState(false);
    const [showTimePicker, setShowTimePicker] = useState(false);

    const [latitude, setLatitude] = useState(props?.position?.latitude || '');
    const [longitude, setLongitude] = useState(props?.position?.longitude || '');
    const [address, setAddress] = useState(props?.position?.address || '');

    const [totalUserCount, setTotalUserCount] = useState('');
    const navigation  = useNavigation<NativeStackNavigationProp<RootStackParamList>>();

    useEffect(() => {
        // props.position이 변경될 때마다 상태 업데이트
        setLatitude(props?.position?.latitude || '');
        setLongitude(props?.position?.longitude || '');
        setAddress(props?.position?.address || '');
    }, [props?.position]); // props.position이 변경될 때마다 useEffect 실행


    const addMoim = () => {
        const dummies = [...props.dummies, props.position];
        props.setDummies(dummies);
        props.closeAddForm();
        //console.log(dummies);
    };

    const addCommunity = () => {
        axiosPost.post("/commu/addCommunity", JSON.stringify({
                commuTitle,
                commuComent,
                commuMeetingTime: commuMeetingTime.toISOString(), // Convert date to ISO string
                latitude,
                longitude,
                address,
                totalUserCount: parseInt(totalUserCount, 10) // Convert to integer
            })
        ).then((res) => {

            if(res.data){
                Alert.alert("등록되었습니다.")
                props.setState('find');
            }
        })
    }

    return (
        <>


            <Modal
                visible={props.state === 'add'}
                onRequestClose={() => props.closeAddForm()}>


{/*                {Object.entries(props?.position).map((elem: any) => (
                    <Text key={elem[0]}>
                        {elem[0]} : {elem[1]}
                    </Text>
                ))}*/}



                <ScrollView style={styles.container}>
                    <View style={styles.header}>
                        <Text style={styles.headerTitle}>모임 등록하기</Text>
                    </View>
                    <View style={styles.formGroup}>
                        <TextInput
                            style={styles.input}
                            placeholder="모임 이름 *"
                            value={commuTitle}
                            onChangeText={setCommuTitle}
                        />
                    </View>
                    <View style={styles.formGroup}>
                        <TextInput
                            style={[styles.input, styles.textArea]}
                            placeholder="모임 설명"
                            value={commuComent}
                            onChangeText={setCommuComent}
                            multiline
                        />
                    </View>
                    <View style={styles.formGroup}>
                        <Text>날짜 및 시간 *</Text>
                        <TouchableOpacity onPress={() => setShowDatePicker(true)}>
                            <TextInput
                                style={styles.input}
                                placeholder="날짜 선택"
                                value={commuMeetingTime.toDateString()}
                                editable={false}
                            />
                        </TouchableOpacity>
                        {showDatePicker && (
                            <DatePicker
                                modal
                                open={showDatePicker}
                                date={commuMeetingTime}
                                onConfirm={(selectedDate) => {
                                    setShowDatePicker(false);
                                    setCommuMeetingTime(selectedDate);
                                }}
                                onCancel={() => setShowDatePicker(false)}
                            />
                        )}
                        <TouchableOpacity onPress={() => setShowTimePicker(true)}>
                            <TextInput
                                style={styles.input}
                                placeholder="시간 선택"
                                value={commuMeetingTime.toTimeString()}
                                editable={false}
                            />
                        </TouchableOpacity>
                        {showTimePicker && (
                            <DatePicker
                                modal
                                open={showTimePicker}
                                date={commuMeetingTime}
                                mode="time"
                                onConfirm={(selectedDate) => {
                                    setShowTimePicker(false);
                                    setCommuMeetingTime(selectedDate);
                                }}
                                onCancel={() => setShowTimePicker(false)}
                            />
                        )}
                    </View>
                    <View style={styles.formGroup}>
                        <TextInput
                            style={[styles.input]}
                            placeholder="위치 정보"
                            value={address}
                            /*onChangeText={setAddress}*/
                            multiline
                        />
                    </View>
                    <View style={styles.formGroup}>
                        <TextInput
                            style={styles.input}
                            placeholder="최대 참여 인원"
                            value={totalUserCount}
                            onChangeText={setTotalUserCount}
                            keyboardType="numeric"
                        />
                    </View>
                    <Button title="모임 등록하기" onPress={() => addCommunity()} />
                    <Button title="취소" onPress={() => { /* 취소 로직 */ }} />
                    <Button
                        title="뒤로"
                        onPress={() => {
                            props.closeAddForm();
                        }}
                    />
                </ScrollView>
            </Modal>
        </>
    );
}




