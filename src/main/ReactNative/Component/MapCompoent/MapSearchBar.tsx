import React from 'react'
import {Button, Image, StyleSheet, Text, TextInput, TouchableOpacity, View} from "react-native";
import Icon from "react-native-vector-icons/FontAwesome";

export default function MapSearchBar(props: any) {
    return (
        /*<>
            <View
                style={{
                    flexDirection: 'row',
                    justifyContent: 'space-between',
                }}>
                <TextInput
                    style={{width: '90%'}}
                    value={props.keyword}
                    onChangeText={props.setKeyword}
                />
                <TouchableOpacity
                    onPress={() => {
                        props.getLocations();
                        props.privateSetPosition(undefined);
                    }}
                    style={{
                        width: '10%',
                        alignItems: 'center',
                        backgroundColor: '#2196F3',
                        borderRadius: 3,
                    }}>
                    <Icon
                        style={{
                            marginVertical: 'auto',
                        }}
                        name="search"
                        size={20}
                        color="white"
                    />
                </TouchableOpacity>
            </View>
            <Button title="위치기반 모임확인" onPress={props.findMoimByMyPosition}/>
            <Button title="화면기반 모임확인" onPress={props.findMoimByCamera}/>
        </>*/
        // 배경 컴포넌트에 opacity 적용
        <View style={styles.container} >

            <View style={styles.searchBox}>
                {/* 메뉴 아이콘 */}
                <TouchableOpacity style={styles.iconButton}>
                    <Icon name="bars" size={20} color="#666" />
                </TouchableOpacity>

                {/* 검색 입력창 */}
                <TextInput
                    style={styles.input}
                    placeholder="장소, 버스, 지하철, 주소 검색"
                    placeholderTextColor="#999"
                />

                {/* 마이크 아이콘 */}
                <TouchableOpacity style={styles.iconButton}>
                    <Icon name="microphone" size={20} color="#666" />
                </TouchableOpacity>
            </View>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        position: 'relative',
        flexDirection: 'row',
        alignItems: 'center',
        zIndex : -1
    },

    searchBox: {

    },
    input: {

    },
    iconButton: {
        padding: 5,
    },
});