import React, {useState} from 'react';
import {Button, ScrollView, Text, TextInput, TouchableOpacity, View} from 'react-native';
import "./styles.ts";
import styles from "./styles.ts";
import DatePicker from "react-native-date-picker";
import { FloatingLabelInput } from 'react-native-floating-label-input';


export default function CommunityAddForm(props: any) {

    const [eventName, setEventName] = useState('');
    const [eventDescription, setEventDescription] = useState('');
    const [date, setDate] = useState(new Date());
    const [showDatePicker, setShowDatePicker] = useState(false);
    const [showTimePicker, setShowTimePicker] = useState(false);
    const [location, setLocation] = useState({
        latitude: props.dummies,
        longitude: props.position,
    });
    const [maxParticipants, setMaxParticipants] = useState('');
    const [otherDetails, setOtherDetails] = useState('');


  const setDummies = () => {
    const dummies = [...props.dummies, props.position];
    props.setDummies(dummies);
    props.setState('find');
    console.log(dummies);
  };

  return (
    <>
{/*      {Object.entries(props.position).map((elem: any) => (
        <Text key={elem[0]}>
          {elem[0]} : {elem[1]}
        </Text>
      ))}
*/}
        <ScrollView style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.headerTitle}>모임 등록하기</Text>
            </View>
            <View style={styles.formGroup}>
                <FloatingLabelInput
                    label="모임 이름 *"
                    value={eventName}
                    onChangeText={setEventName}
                    containerStyles={styles.inputContainer}
                    customLabelStyles={{ colorFocused: '#000', fontSizeFocused: 12 }}
                />
            </View>
            <View style={styles.formGroup}>
                <FloatingLabelInput
                    label="모임 설명"
                    value={eventDescription}
                    onChangeText={setEventDescription}
                    containerStyles={styles.textAreaContainer}
                    customLabelStyles={{ colorFocused: '#000', fontSizeFocused: 12 }}
                    multiline
                />
            </View>
            <View style={styles.formGroup}>
                <Text>날짜 및 시간 *</Text>
                <TouchableOpacity onPress={() => setShowDatePicker(true)}>
                    <FloatingLabelInput
                        label="날짜 선택"
                        value={date.toDateString()}
                        editable={false}
                        containerStyles={styles.inputContainer}
                        customLabelStyles={{ colorFocused: '#000', fontSizeFocused: 12 }}
                    />
                </TouchableOpacity>
                {showDatePicker && (
                    <DatePicker
                        modal
                        open={showDatePicker}
                        date={date}
                        onConfirm={(selectedDate) => {
                            setShowDatePicker(false);
                            setDate(selectedDate);
                        }}
                        onCancel={() => setShowDatePicker(false)}
                    />
                )}
                <TouchableOpacity onPress={() => setShowTimePicker(true)}>
                    <FloatingLabelInput
                        label="시간 선택"
                        value={date.toTimeString()}
                        editable={false}
                        containerStyles={styles.inputContainer}
                        customLabelStyles={{ colorFocused: '#000', fontSizeFocused: 12 }}
                    />
                </TouchableOpacity>
                {showTimePicker && (
                    <DatePicker
                        modal
                        open={showTimePicker}
                        date={date}
                        mode="time"
                        onConfirm={(selectedDate) => {
                            setShowTimePicker(false);
                            setDate(selectedDate);
                        }}
                        onCancel={() => setShowTimePicker(false)}
                    />
                )}
            </View>
           {/* <View style={styles.formGroup}>
                <Text>위치 *</Text>
                <MapView
                    style={styles.map}
                    initialRegion={{
                        ...location,
                        latitudeDelta: 0.0922,
                        longitudeDelta: 0.0421,
                    }}
                    onPress={(e) => setLocation(e.nativeEvent.coordinate)}
                >
                    <Marker coordinate={location} />
                </MapView>
                <FloatingLabelInput
                    label="선택된 주소"
                    value={`위도: ${location.latitude}, 경도: ${location.longitude}`}
                    editable={false}
                    containerStyles={styles.inputContainer}
                    customLabelStyles={{ colorFocused: '#000', fontSizeFocused: 12 }}
                />
            </View>*/}
            <View style={styles.formGroup}>
                <FloatingLabelInput
                    label="최대 참여 인원"
                    value={maxParticipants}
                    onChangeText={setMaxParticipants}
                    containerStyles={styles.inputContainer}
                    customLabelStyles={{ colorFocused: '#000', fontSizeFocused: 12 }}
                    keyboardType="numeric"
                />
            </View>
            <Button title="모임 등록하기" onPress={setDummies} />
            <Button title="취소" onPress={() => { /* 취소 로직 */ }} />
            <Button title="뒤로" onPress={() => { props.setState('find'); }} />
        </ScrollView>

    </>
  );
}
