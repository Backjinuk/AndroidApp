import React, {useState} from 'react';
import {Button} from 'react-native';
import {addChatRoomForGroup, addChatRoomForOneToOne} from './chatUtils';

export default function AddChatButton({navigation, commuSeq, userSeq}: any) {
  const isPrivate = commuSeq === undefined;
  const isGroup = userSeq === undefined;
  const handleAddChat = async () => {
    const roomId = isPrivate
      ? await addChatRoomForOneToOne(userSeq)
      : await addChatRoomForOneToOne(userSeq, commuSeq);
    navigation.navigate('ChatScreen', {roomId});
  };
  const handleAddGroupChat = () => {
    addChatRoomForGroup(commuSeq);
  };

  return isGroup ? (
    <Button
      title={`addChat(모임번호:${commuSeq})`}
      onPress={handleAddGroupChat}
    />
  ) : (
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
