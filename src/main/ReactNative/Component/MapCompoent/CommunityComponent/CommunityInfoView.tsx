import React from "react";
import {Button, Modal, Text, TextInput, View} from "react-native";

export default function CommunityInfoView (props : any) {

    return (
        <Modal
            visible={props.openModal === true} // openModal 속성을 참조
            onRequestClose={() => props.closeAddForm()}
            transparent={true} // 필요에 따라 모달 배경을 투명하게 할 수 있음
        >
            <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
                <View style={{ padding: 20, backgroundColor: 'white', borderRadius: 10 }}>
                    <Text>{props?.marker?.commuTitle}</Text>
                    {/* 다른 필요한 정보를 표시할 수 있습니다. */}
                </View>
                <Button title={"닫기"} onPress={props.setOpenModal}></Button>
            </View>
        </Modal>
    );

}