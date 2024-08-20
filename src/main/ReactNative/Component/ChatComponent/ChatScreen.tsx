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
import generateRandomString from '../../Util/generateRandomString';
interface Message {
  id: string;
  chatter: string;
  content: string;
  roomId: String;
}

const ChatScreen: React.FC = ({route}: any) => {
  const {roomId, userId} = route.params;
  const [messages, setMessages] = useState<Message[]>([]);
  const [input, setInput] = useState('1');
  const wsurl = Config.CHAT_URL;
  const client = useRef<WebSocket>();
  const flatListRef = useRef<FlatList>(null);

  useEffect(() => {
    setInput(generateRandomString(10));
    connect();
    return () => {
      disconnect();
    };
  }, []);

  const sendMessage = () => {
    if (input.trim() && client.current) {
      const newMessage: Message = {
        id: generateRandomString(25),
        chatter: userId,
        content: input,
        roomId: roomId,
      };
      setMessages(prevMessages => [...prevMessages, newMessage]);
      client.current.send(JSON.stringify(newMessage));
      setInput(generateRandomString(10));
    }
  };

  const connect = async () => {
    if (wsurl) {
      const accessToken = await AsyncStorage.getItem('AccessToken');
      const refreshToken = await AsyncStorage.getItem('RefreshToken');
      client.current = new WebSocket(wsurl + `?roomId=${roomId}`, null, {
        headers: {
          AccessToken: `Bearer ${accessToken}`,
          RefreshToken: `${refreshToken}`,
        },
      });
      console.log(client.current);
      client.current.onopen = () => {
        console.log('소켓 통신 활성화');
      };
      client.current.onmessage = e => {
        console.log('받은 데이터 : ' + e.data);
        const jsondata = JSON.parse(e.data);
        if (jsondata.type === 'list') {
          console.log(jsondata.payload.length > 0);
          if (jsondata.payload.length > 0) {
            console.log('list 출력');
            const messages: Message[] = [];
            jsondata.payload.forEach((data: any) => {
              const newMessage = {
                id: data.id,
                chatter: data.chatter,
                content: data.content,
                roomId: data.roomId,
              };
              messages.push(newMessage);
            });
            setMessages(prevMessages => [...prevMessages, ...messages]);
          }
        } else {
          const data = jsondata.payload;
          const newMessage = {
            id: data.id,
            chatter: data.chatter,
            content: data.content,
            roomId: data.roomId,
          };
          setMessages(prevMessages => [...prevMessages, newMessage]);
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
    setMessages([]);
  };

  const reset = () => {
    disconnect();
    connect();
  };

  return (
    <View style={styles.container}>
      <FlatList
        data={messages}
        keyExtractor={item => item.id}
        ref={flatListRef}
        onContentSizeChange={() => {
          flatListRef.current?.scrollToEnd({animated: false});
        }}
        renderItem={({item}) => (
          <Text style={{textAlign: item.chatter === userId ? 'right' : 'left'}}>
            {item.chatter} : {item.content}
          </Text>
        )}
      />
      <TextInput
        style={styles.input}
        onChangeText={text => setInput(text)}
        value={input}
      />
      <Button title="Send" onPress={sendMessage} />
      {/* <Button title="Connect" onPress={connect} /> */}
      {/* <Button title="Disconnect" onPress={disconnect} /> */}
      <Button title="Reset" onPress={reset} />
    </View>
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
