import React, {useEffect, useState} from 'react';
import {Button, FlatList, StyleSheet, Text, View} from 'react-native';
import generateRandomString from '../../Util/generateRandomString';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {jwtDecode} from 'jwt-decode';

interface ChatRoom {
  id: string;
}

export default function ChatRoomList({navigation}: any) {
  const [rooms, setRooms] = useState<ChatRoom[]>([]);
  const [userId, setUserId] = useState<string>('');
  const [seleted, setSelected] = useState<String>('public');
  useEffect(() => {
    getUserId();
    setUserId(generateRandomString(6));
  }, []);
  const getUserId = async () => {
    const accessToken = await AsyncStorage.getItem('AccessToken');
    const data = jwtDecode(accessToken!);
    console.log(data);
    const RefreshToken = await AsyncStorage.getItem('RefreshToken');
    console.log(jwtDecode(RefreshToken!));
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
          navigation.navigate('ChatScreen', {roomId: 'room12', userId});
        }}
      />
      <FlatList
        data={rooms}
        keyExtractor={item => item.id}
        renderItem={({item}) => (
          <Button
            title={item.id}
            onPress={() => {
              navigation.navigate('ChatScreen', {roomId: item.id, userId});
            }}
          />
        )}
      />
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
