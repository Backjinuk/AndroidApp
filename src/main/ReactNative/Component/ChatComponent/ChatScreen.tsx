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
interface Message {
  id: string;
  content: string;
}
import {getUniqueId} from 'react-native-device-info';

const ChatScreen: React.FC = () => {
  const [messages, setMessages] = useState<Message[]>([]);
  const [input, setInput] = useState('1');
  const wsurl = Config.CHAT_URL;
  const client = useRef<WebSocket>();

  const sendMessage = () => {
    if (input.trim() && client.current) {
      const newMessage: Message = {
        id: new Date().toISOString(),
        content: input,
      };
      setMessages(prevMessages => [...prevMessages, newMessage]);
      client.current.send(JSON.stringify(newMessage));
      // setInput('');
    }
  };

  const connect = () => {
    if (wsurl) {
      client.current = new WebSocket(wsurl);
      client.current.onopen = () => {
        console.log('소켓 통신 활성화');
      };
      client.current.onmessage = e => {
        console.log('받은 데이터 : ' + e.data);
        const newMessage = {id: new Date().toISOString(), content: e.data};
        setMessages(prevMessages => [...prevMessages, newMessage]);
        console.log('받은 데이터 등록 완료');
      };
      client.current.onerror = e => {
        console.log('에러 발생 : ' + e);
      };
      client.current.onclose = e => {
        console.log('소켓 통신 해제');
      };
    }
  };

  const disconnect = () => {
    client.current?.close();
  };

  const check = () => {
    console.log(getUniqueId());
  };

  return (
    <View style={styles.container}>
      <FlatList
        data={messages}
        keyExtractor={item => item.id}
        renderItem={({item}) => <Text>{item.content}</Text>}
      />
      <TextInput
        style={styles.input}
        onChangeText={text => setInput(text)}
        value={input}
      />
      <Button title="Send" onPress={sendMessage} />
      <Button title="Connect" onPress={connect} />
      <Button title="Disconnect" onPress={disconnect} />
      <Button title="Check" onPress={check} />
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
