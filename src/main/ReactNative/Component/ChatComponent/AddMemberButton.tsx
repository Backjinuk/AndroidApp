import React from 'react';
import {Button} from 'react-native';
import {addMember} from './chatUtils';

export default function AddMemberButton({commuSeq}: any) {
  const handleAddMember = () => {
    addMember(commuSeq);
  };

  return (
    <Button
      title={`addMember(모임번호:${commuSeq})`}
      onPress={handleAddMember}
    />
  );
}
