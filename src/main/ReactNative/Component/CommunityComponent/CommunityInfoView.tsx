import React, {useEffect, useState} from "react";
import {Alert, Button, Image, Modal, Text, TextInput, TouchableOpacity, View} from "react-native";
import styles from "./styles.ts";
import axiosPost from "../../Util/AxiosUtil.ts";
import UserProfileModal from "./UserProfileModal.tsx";


export default function CommunityInfoView (props : any) {

    const [marker, setMaker] = useState<Community>()
    const [openModal, setOpenModal] = useState(false);
    const [subscribe, setSubscribe] = useState<Subscribe>()



    useEffect(() => {
        setMaker(props.marker);
    })

    if (!marker) {
        return null; // marker가 없을 경우 컴포넌트를 렌더링하지 않음
    }

    const commuApplyUser = () => {
        axiosPost.post("commu/commuApplyUser",
            JSON.stringify(marker)
        ).then((res) => {
            console.log(res.data);
            props.setCommuPosition()
        })
    }

    const getSubscribe = () => {
        axiosPost.post("/subscribe/getSubscribe", JSON.stringify({
            'subscriberOwnerUserSeq': props.marker.commuWrite.userSeq
        })).then(res => {
                setSubscribe(res.data)
            }
        )
    }


    return (
            <View>
                {/* 지도 오버레이 카드 */}
                <View style={[styles.infoCard]}>
                    <View style={styles.cardHeader}>
                        <Text style={styles.title}>{marker.commuTitle || "가게 이름"}</Text>
                        <Text style={styles.subInfo}>{marker.commuComent}</Text>
                    </View>
                    <Image
                        source={{ uri: 'https://via.placeholder.com/150' }}
                        style={styles.image}
                    />
                </View>
                {/* 하단 버튼 */}
                <View style={styles.buttonContainer}>
                    <View style={{flex : 1, display : 'flex', flexDirection : "row", justifyContent : "flex-start"}}>
                        <Text style={[styles.subInfo, {marginTop : 5}]}> 모집인원 :  {marker.userCount || 0} / {marker.totalUserCount}</Text>
                    </View>

                    <View style={{flex : 1, display : 'flex', flexDirection : "row", justifyContent : "flex-end"}}>
                        {marker.applyStatus === 'Y' ? (
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
                            getSubscribe();
                            setOpenModal(true)
                        }}>
                            <Text style={styles.buttonText}>신정자</Text>
                        </TouchableOpacity>

                        <TouchableOpacity style={styles.button} onPress={() => {
                            props.viewMode(marker)
                        }}>
                            <Text style={styles.buttonText}>상세</Text>
                        </TouchableOpacity>

                    </View>

                </View>

                <UserProfileModal
                    openModal={openModal}
                    setOpenModal={setOpenModal}
                    commuWrite={props.marker.commuWrite}
                    subscribe={subscribe}
                />

            </View>


    );

}