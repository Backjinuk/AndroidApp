import React from "react";
import {Alert, Button, Image, Modal, Text, TextInput, TouchableOpacity, View} from "react-native";
import styles from "./styles.ts";
import axiosPost from "../../../Util/AxiosUtil.ts";

export default function CommunityInfoView (props : any) {

    if (!props.marker) {
        return null; // marker가 없을 경우 컴포넌트를 렌더링하지 않음
    }

    const commuApplyUser = () => {

        axiosPost.post("commu/commuApplyUser",
            JSON.stringify(props.marker)
        ).then((res) => {
            console.log(res.data);
            props.setCommuPosition()
        })
    }

    return (
            <View style={[styles.container2]}>
                {/* 지도 오버레이 카드 */}
                <View style={[styles.infoCard]}>
                    <View style={styles.cardHeader}>
                        <Text style={styles.title}>{props.marker.commuTitle || "가게 이름"}</Text>
                        <Text style={styles.subInfo}>{props.marker.commuComent}</Text>
                        <Text style={[styles.subInfo, {marginTop : 5}]}> 모집인원 :  {props.marker.userCount || 0} / {props.marker.totalUserCount}</Text>
                    </View>
                    <Image
                        source={{ uri: props.marker.image || 'https://via.placeholder.com/150' }}
                        style={styles.image}
                    />
                </View>

                {/* 하단 버튼 */}
                <View style={styles.buttonContainer}>



                    {props.marker.applyStatus === 'Y' ? (
                        <TouchableOpacity style={[styles.button, { backgroundColor: 'red' }]} onPress={() => commuApplyUser()
                            // Alert.alert("이미 신청하였습니다.")
                        }>
                            <Text style={styles.buttonText}>신청</Text>
                        </TouchableOpacity>
                    ) : (
                        <TouchableOpacity style={styles.button} onPress={() => commuApplyUser()} >
                            <Text style={styles.buttonText}>신청</Text>
                        </TouchableOpacity>
                    )}

                    <TouchableOpacity style={styles.button} onPress={() => {
                        props.viewMode(props.marker)
                    }}>
                        <Text style={styles.buttonText}>상세</Text>
                    </TouchableOpacity>

                </View>
            </View>
    );

}