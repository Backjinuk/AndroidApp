import React, {useEffect, useRef, useState} from 'react';
import {Button, FlatList, StyleSheet, Text, View} from 'react-native';
import AsyncStorage from '@react-native-async-storage/async-storage';
import {jwtDecode} from 'jwt-decode';
import AddChatButton from './AddChatButton';
import Config from 'react-native-config';
import AddMemberButton from './AddMemberButton';

interface ChatRoom {
  id: string;
  chatTime: string;
  chatters: string[];
  unread: number;
  type: string;
}

export default function ChatRoomList({navigation}: any) {
  const debug = false;
  const log = (message?: any, ...optionalParams: any[]) => {
    if (debug) console.log(message, ...optionalParams);
  };
  const [rooms, setRooms] = useState<ChatRoom[]>([]);
  const [selected, setPrivateSelected] = useState<String>('public');
  const [userSeq, setUserSeq] = useState<number>(0);
  const setSelected = (type: string) => {
    setPrivateSelected(type);
  };
  const updateRooms = (newRooms: ChatRoom[]) => {
    setRooms(prevRooms => {
      const roomIds: string[] = newRooms.map(room => room.id);

      const updatedRooms = newRooms.concat(
        prevRooms.filter(room => !roomIds.includes(room.id)),
      );

      updatedRooms.sort((comp1, comp2) => {
        const date1 = new Date(comp1.chatTime).getTime();
        const date2 = new Date(comp2.chatTime).getTime();

        return date2 - date1;
      });

      return updatedRooms;
    });
  };
  const wsurl = Config.CHAT_URL;
  const client = useRef<WebSocket>();
  useEffect(() => {
    connect();
    return () => {
      disconnect();
    };
  }, [selected]);

  const connect = async () => {
    if (wsurl) {
      const accessToken = await AsyncStorage.getItem('AccessToken');
      const accessTokenData: any = jwtDecode(accessToken!);
      const refreshToken = await AsyncStorage.getItem('RefreshToken');
      setUserSeq(accessTokenData.userSeq);
      client.current = new WebSocket(
        wsurl +
          `?type=chatRoomList&userSeq=${accessTokenData.userSeq}&roomType=${selected}`,
        null,
        {
          headers: {
            AccessToken: `Bearer ${accessToken}`,
            RefreshToken: `${refreshToken}`,
          },
        },
      );
      log(client.current);
      client.current.onopen = () => {
        log('소켓 통신 활성화');
      };
      client.current.onmessage = e => {
        log('받은 데이터 : ' + e.data);
        const jsondata = JSON.parse(e.data);
        switch (jsondata.type) {
          case 'rooms':
            onRooms(jsondata.payload);
            break;
          case 'message':
            onRooms(jsondata.payload);
            break;
          case 'read':
            onRead(jsondata.payload);
            break;
        }
        log('받은 데이터 등록 완료');
      };
      client.current.onerror = e => {
        log('에러 발생 : ' + e.message);
      };
      client.current.onclose = e => {
        log('소켓 통신 해제');
      };
    }
  };

  const onRooms = (datas: any[]) => {
    if (datas.length > 0) {
      log('list 출력');
      const newRooms: ChatRoom[] = [];
      datas.forEach((data: any) => {
        const newRoom: any = {
          id: data.id,
          chatTime: data.chatTime,
          content: data.content,
          chatters: data.chatters,
          commuSeq: data.commuSeq,
          unread: data.unreadMessages,
          type: data.type,
        };
        newRooms.push(newRoom);
      });
      updateRooms(newRooms);
    }
  };

  const onRead = (roomId: string) => {
    setRooms(prev => {
      let next = [...prev];
      const index = next.findIndex(item => item.id === roomId);
      next[index].unread = 0;
      return next;
    });
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
            color={selected == 'public' ? '' : '#f194ff'}
            onPress={() => {
              setSelected('public');
            }}
          />
        </View>
        <View style={{flex: 1}}>
          <Button
            title="private"
            color={selected == 'private' ? '' : '#f194ff'}
            onPress={() => {
              setSelected('private');
            }}
          />
        </View>
      </View>
      <FlatList
        data={rooms}
        keyExtractor={item => item.id}
        renderItem={({item}) => (
          <View style={{marginBottom: 5}}>
            <Button
              title={`roomId : ${item.id} \n\n chatters : ${item.chatters} \n\n chatTime : ${item.chatTime} \n\n type : ${item.type} \n\n unread : ${item.unread}`}
              onPress={() => {
                navigation.navigate('ChatScreen', {
                  roomId: item.id,
                  totalChatters: item.chatters.length,
                  chatter: userSeq,
                });
              }}
            />
          </View>
        )}
      />
      <AddChatButton commuSeq={1} />
      <AddMemberButton commuSeq={1} />
      <AddChatButton navigation={navigation} userSeq={2} commuSeq={1} />
      <AddChatButton navigation={navigation} userSeq={2} />
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
