import React from "react";
import {Button, Image, Modal, Text, TextInput, TouchableOpacity, View} from "react-native";
import styles from "./styles.ts";
import axiosPost from "../../../Util/AxiosUtil.ts";

export default function CommunityInfoView (props : any) {

    if (!props.marker) {
        return null; // marker가 없을 경우 컴포넌트를 렌더링하지 않음
    }

    const commuApplyUser = () => {
        console.log(props.marker)
        axiosPost.post("commu/commuApplyUser",
            JSON.stringify(props.marker)
        ).then((res) => {
            console.log(res.data);
        })
    }

    return (
            <View style={[styles.container2]}>
                {/* 지도 오버레이 카드 */}
                <View style={[styles.infoCard]}>
                    <View style={styles.cardHeader}>
                        <Text style={styles.title}>{props.marker.commuTitle || "가게 이름"}</Text>
                        <Text style={styles.subInfo}>{props.marker.commuComent}</Text>
                        <Text style={[styles.subInfo, {marginTop : 5}]}> 모집인원 :  {props.marker.usercount || 0} / {props.marker.totalUserCount}</Text>
                    </View>
                    <Image
                        source={{ uri: props.marker.image || 'https://via.placeholder.com/150' }}
                        style={styles.image}
                    />
                </View>

                {/* 하단 버튼 */}
                <View style={styles.buttonContainer}>
                    <TouchableOpacity style={styles.button} onPress={() => commuApplyUser()}>
                        <Text style={styles.buttonText}>신청</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.button} onPress={() => console.log('상세')}>
                        <Text style={styles.buttonText}>상세</Text>
                    </TouchableOpacity>
                </View>
            </View>

        /*

        <Text>{props.marker.commuTitle}</Text>
            <Button title={"신청"} onPress={props.setOpenModal}></Button>
            <Button title={"닫기"} onPress={props.setOpenModal}></Button>
       */
        // <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', width : '100%', height : 80 }}>
        //         <View style={{ padding: 20, backgroundColor: 'white', borderRadius: 10 , width : '100%', height : 300}}>
        //
        //             {/* 다른 필요한 정보를 표시할 수 있습니다. */}
        //
        //             <Button title={"신청"} onPress={props.setOpenModal}></Button>
        //             <Button title={"닫기"} onPress={props.setOpenModal}></Button>
        //         </View>
        // </View>
    );

}