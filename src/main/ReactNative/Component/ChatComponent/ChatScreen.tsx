import AsyncStorage from '@react-native-async-storage/async-storage';
import React, {useEffect, useState, useRef} from 'react';
import {
  View,
  TextInput,
  Button,
  FlatList,
  Text,
  StyleSheet,
} from 'react-native';
import Config from 'react-native-config';
import generateRandomString, {
  randomUUID,
} from '../../Util/generateRandomString';
import {TouchableOpacity} from 'react-native-gesture-handler';
import Icon from 'react-native-vector-icons/FontAwesome';
import {jwtDecode} from 'jwt-decode';
interface Message {
  id: string;
  chatter: number;
  chatterId: string;
  content: string;
  roomId: string;
}
interface MessageState {
  ids: string[];
  datas: {id: string; data: Message}[];
}

const emptyMessageState = {
  ids: [],
  datas: [],
};

const ChatScreen: React.FC = ({route, navigation}: any) => {
  const debug = true;
  const log = (message?: any, ...optionalParams: any[]) => {
    if (debug) console.log(message, ...optionalParams);
  };
  const {roomId, totalChatters, chatter} = route.params;
  const [userSeq, setUserSeq] = useState<number>(chatter);
  const [userId, setUserId] = useState<string>('');
  const [messages, setMessages] = useState<MessageState>(emptyMessageState);
  const [inputHeight, setInputHeight] = useState(40);
  const [input, setInput] = useState('1');
  const wsurl = Config.CHAT_URL;
  const client = useRef<WebSocket>();
  const flatListRef = useRef<FlatList>(null);

  useEffect(() => {
    getUserInfo();
    setInput(generateRandomString(10));
    connect();
    return () => {
      disconnect();
    };
  }, []);

  const getUserInfo = async () => {
    const accessToken = await AsyncStorage.getItem('AccessToken');
    const data: any = jwtDecode(accessToken!);
    setUserSeq(data.userSeq);
    setUserId(data.userId);
  };

  const updateMessage = (messages: Message[]) => {
    setMessages(prevMessages => {
      let nextMessageState = {...prevMessages};
      messages.forEach(message => {
        if (nextMessageState.ids.includes(message.id)) {
          const index = nextMessageState.datas.findIndex(
            data => data.id === message.id,
          );
          nextMessageState.datas[index].data = message;
        } else {
          nextMessageState.ids.push(message.id);
          nextMessageState.datas.push({
            id: message.id,
            data: message,
          });
        }
      });
      return nextMessageState;
    });
  };

  const sendMessage = () => {
    if (input.trim() && client.current) {
      const newMessage = {
        id: randomUUID(),
        chatter: userSeq,
        chatterId: userId,
        content: input,
        roomId: roomId,
        unread: totalChatters - 1,
      };
      setMessages(prevMessages => {
        let nextMessageState = {...prevMessages};
        nextMessageState.ids.push(newMessage.id);
        nextMessageState.datas.push({
          id: newMessage.id,
          data: newMessage,
        });
        return nextMessageState;
      });
      client.current.send(
        JSON.stringify({type: 'message', payload: JSON.stringify(newMessage)}),
      );
      setInput(generateRandomString(10));
    }
  };

  const readMessage = () => {
    if (client.current) {
      const readInfo = {
        reader: userSeq,
        roomId: roomId,
      };
      client.current.send(
        JSON.stringify({
          type: 'read',
          payload: JSON.stringify(readInfo),
        }),
      );
    }
  };

  const onMessage = (payload: any) => {
    let readCheck = false;
    if (payload.length > 0) {
      log('list 출력');
      const messages: Message[] = [];
      payload.forEach((data: any) => {
        let unread = data.unread;
        if (data.unread.includes(userSeq)) {
          readCheck = true;
          unread = data.unread.filter((user: number) => user !== userSeq);
        }
        const newMessage = {
          id: data.id,
          chatter: data.chatter,
          chatterId: data.chatterId,
          content: data.content,
          roomId: data.roomId,
          unread: unread.length,
        };
        messages.push(newMessage);
      });
      updateMessage(messages);
    }
    if (readCheck) readMessage();
  };

  const connect = async () => {
    if (wsurl) {
      const accessToken = await AsyncStorage.getItem('AccessToken');
      const refreshToken = await AsyncStorage.getItem('RefreshToken');
      client.current = new WebSocket(
        wsurl + `?type=chatRoom&roomId=${roomId}`,
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
        const data = JSON.parse(e.data);
        switch (data.type) {
          case 'message':
            onMessage(data.payload);
            break;
          case 'read':
            onMessage(data.payload);
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

  const disconnect = () => {
    client.current?.close();
    setMessages(emptyMessageState);
  };

  return (
    <>
      <View
        style={{
          justifyContent: 'space-between',
          flexDirection: 'row',
          backgroundColor: 'white',
          alignItems: 'center',
        }}>
        <TouchableOpacity
          style={{
            justifyContent: 'center',
            alignItems: 'center',
            alignSelf: 'center',
            width: 40,
            height: 40,
            backgroundColor: '#2196F3',
          }}
          onPress={() => {
            navigation.pop();
          }}>
          <Icon name="arrow-left" size={25} color="white" />
        </TouchableOpacity>
        <Text>뭐 넣지</Text>
      </View>
      <View style={styles.container}>
        <FlatList
          data={messages.datas}
          keyExtractor={item => item.id}
          ref={flatListRef}
          onContentSizeChange={() => {
            flatListRef.current?.scrollToEnd({animated: false});
          }}
          renderItem={({item}) => {
            const message = item.data;
            return (
              <Text
                style={{
                  textAlign: message.chatter === userSeq ? 'right' : 'left',
                }}>
                {message.unread} : {message.chatterId} : {message.content}
              </Text>
            );
          }}
        />
        <View
          style={{
            flexDirection: 'row',
            alignItems: 'center',
            marginBottom: 10,
          }}>
          <TextInput
            style={{...styles.input, flex: 8, marginBottom: 0}}
            onChangeText={text => setInput(text)}
            onLayout={event => {
              const {height} = event.nativeEvent.layout;
              log(height);
              setInputHeight(height || 40); // 입력창의 높이 설정
            }}
            value={input}
          />
          <TouchableOpacity
            style={{
              flex: 2,
              justifyContent: 'center',
              alignItems: 'center',
              alignSelf: 'center',
              height: inputHeight,
              width: inputHeight,
              backgroundColor: '#2196F3',
            }}
            onPress={sendMessage}>
            <Icon name="send" color="white" size={inputHeight * 0.5} />
          </TouchableOpacity>
        </View>
      </View>
    </>
  );
};

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

export default ChatScreen;
