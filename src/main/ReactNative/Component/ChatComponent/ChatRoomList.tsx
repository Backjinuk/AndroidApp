import React, {useEffect, useRef, useState} from 'react';
import {Button, FlatList, StyleSheet, Text, View} from 'react-native';
import {RNG} from '../../Util/generateRandomString';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {jwtDecode} from 'jwt-decode';
import AddChatButton from './AddChatButton';
import {TextInput} from 'react-native-gesture-handler';
import Config from 'react-native-config';

interface ChatRoom {
  id: string;
}

export default function ChatRoomList({navigation}: any) {
  const [rooms, setRooms] = useState<ChatRoom[]>([]);
  const [userSeq, setUserSeq] = useState<number>(0);
  const [seleted, setSelected] = useState<String>('public');
  const wsurl = Config.CHAT_URL;
  const client = useRef<WebSocket>();
  useEffect(() => {
    getuserSeq();
    connect();
    return () => {
      disconnect();
    };
  }, []);
  const getuserSeq = async () => {
    const accessToken = await AsyncStorage.getItem('AccessToken');
    const data: any = jwtDecode(accessToken!);
    setUserSeq(data.userSeq);
  };

  const connect = async () => {
    if (wsurl) {
      const accessToken = await AsyncStorage.getItem('AccessToken');
      const accessTokenData: any = jwtDecode(accessToken!);
      const refreshToken = await AsyncStorage.getItem('RefreshToken');
      client.current = new WebSocket(
        wsurl + `?type=chatRoomList&userSeq=${accessTokenData.userSeq}`,
        null,
        {
          headers: {
            AccessToken: `Bearer ${accessToken}`,
            RefreshToken: `${refreshToken}`,
          },
        },
      );
      console.log(client.current);
      client.current.onopen = () => {
        console.log('소켓 통신 활성화');
      };
      client.current.onmessage = e => {
        console.log('받은 데이터 : ' + e.data);
        const jsondata = JSON.parse(e.data);
        console.log(jsondata.payload.length > 0);
        if (jsondata.payload.length > 0) {
          console.log('list 출력');
          const rooms: any[] = [];
          jsondata.payload.forEach((data: any) => {
            const newRoom = {
              id: data.id,
              chatters: data.chatters,
              commuSeq: data.commuSeq,
              type: data.type,
            };
            rooms.push(newRoom);
          });
          setRooms(prevRooms => [...prevRooms, ...rooms]);
        }
        console.log('받은 데이터 등록 완료');
      };
      client.current.onerror = e => {
        console.log('에러 발생 : ' + e.message);
      };
      client.current.onclose = e => {
        console.log('소켓 통신 해제');
      };
    }
  };

  const disconnect = () => {
    client.current?.close();
    setRooms([]);
  };

  const reset = () => {
    disconnect();
    connect();
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
      <AddChatButton navigation={navigation} />
      <Button title="Reset" onPress={reset} />
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
