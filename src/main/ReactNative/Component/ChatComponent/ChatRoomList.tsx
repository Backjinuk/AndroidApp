import React, {useEffect, useState} from 'react';
import {Button, FlatList, StyleSheet, Text, View} from 'react-native';
import {RNG} from '../../Util/generateRandomString';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {jwtDecode} from 'jwt-decode';
import AddChatButton from './AddChatButton';
import {TextInput} from 'react-native-gesture-handler';

interface ChatRoom {
  id: string;
}

export default function ChatRoomList({navigation}: any) {
  const [rooms, setRooms] = useState<ChatRoom[]>([]);
  const [userSeq, setUserSeq] = useState<number>(0);
  const [seleted, setSelected] = useState<String>('public');
  useEffect(() => {
    getuserSeq();
  }, []);
  const getuserSeq = async () => {
    const accessToken = await AsyncStorage.getItem('AccessToken');
    const data: any = jwtDecode(accessToken!);
    setUserSeq(data.userSeq);
  };

  return (
    <View style={styles.container}>
      <View
        style={{
          flexDirection: 'row',
          paddingBottom: 10,
        }}>
        <View style={{flex: 1}}>
          <Button
            title="public"
            color={seleted == 'public' ? '' : '#f194ff'}
            onPress={() => {
              setSelected('public');
            }}
          />
        </View>
        <View style={{flex: 1}}>
          <Button
            title="private"
            color={seleted == 'private' ? '' : '#f194ff'}
            onPress={() => {
              setSelected('private');
            }}
          />
        </View>
      </View>
      <Button
        title="room12"
        onPress={() => {
          navigation.navigate('ChatScreen', {roomId: 'room12', userSeq});
        }}
      />
      <FlatList
        data={rooms}
        keyExtractor={item => item.id}
        renderItem={({item}) => (
          <Button
            title={item.id}
            onPress={() => {
              navigation.navigate('ChatScreen', {roomId: item.id});
            }}
          />
        )}
      />
      <Button title="token" onPress={getuserSeq} />
      <AddChatButton userSeq={userSeq} navigation={navigation} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
  },
  input: {
    height: 40,
    borderColor: 'gray',
    borderWidth: 1,
    marginBottom: 10,
  },
});
