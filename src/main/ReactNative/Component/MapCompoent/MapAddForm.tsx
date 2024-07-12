import React from 'react';
import {Button, Text} from 'react-native';

export default function MapAddForm(props: any) {
  const setDummies = () => {
    const dummies = [...props.dummies, props.position];
    props.setDummies(dummies);
    props.setState('find');
    console.log(dummies);
  };

  return (
    <>
      {Object.entries(props.position).map((elem: any) => (
        <Text key={elem[0]}>
          {elem[0]} : {elem[1]}
        </Text>
      ))}
      <Button title="등록" onPress={setDummies} />
      <Button
        title="뒤로"
        onPress={() => {
          props.setState('find');
        }}
      />
    </>
  );
}
