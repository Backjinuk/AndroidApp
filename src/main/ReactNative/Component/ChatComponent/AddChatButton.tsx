import React, {useState} from 'react';
import {Button} from 'react-native';
import {addChatRoomForOneToOne} from './chatUtils';

export default function AddChatButton({navigation}: any) {
  const handleAddChat = async () => {
    const roomId = await addChatRoomForOneToOne(2, 1);
    navigation.navigate('ChatScreen', {roomId});
  };

  return (
    <Button
      title="addChat(모임번호:1, 상대유저:user02)"
      onPress={handleAddChat}
    />
  );
}
