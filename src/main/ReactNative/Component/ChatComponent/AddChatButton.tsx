import React, {useState} from 'react';
import {Button} from 'react-native';
import {addChatRoomForOneToOne} from './chatUtils';

export default function AddChatButton({navigation, userSeq}: any) {
  const handleAddChat = async () => {
    const roodId = await addChatRoomForOneToOne(2, 1);
    navigation.navigate('ChatScreen', {roomId: roodId, userSeq});
  };

  return <Button title="addChat" onPress={handleAddChat} />;
}
