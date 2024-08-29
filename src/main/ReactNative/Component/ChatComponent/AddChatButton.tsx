import React, {useState} from 'react';
import {Button} from 'react-native';
import {addChatRoomForOneToOne} from './chatUtils';

export default function AddChatButton({navigation, commuSeq, userSeq}: any) {
  const isPrivate = commuSeq === undefined;
  const handleAddChat = async () => {
    const roomId = isPrivate
      ? await addChatRoomForOneToOne(userSeq)
      : await addChatRoomForOneToOne(userSeq, commuSeq);
    navigation.navigate('ChatScreen', {roomId});
  };

  return (
    <Button
      title={
        isPrivate
          ? `addChat(상대유저:user0${userSeq})`
          : `addChat(모임번호:${commuSeq}, 상대유저:user0${userSeq})`
      }
      onPress={handleAddChat}
    />
  );
}
