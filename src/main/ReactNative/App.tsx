import React, {useState} from 'react';
import type {PropsWithChildren} from 'react';
import {
  Button,
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text, TextInput,
  useColorScheme,
  View,
} from 'react-native';

import {
  Colors,
  DebugInstructions,
  Header,
  LearnMoreLinks,
  ReloadInstructions,
} from 'react-native/Libraries/NewAppScreen';
import axios from "axios";
import Config from "react-native-config";


function App(): React.JSX.Element {
  const [text ,setText] = useState('');
  const api = Config.API_BASE_URL;

  const send = () => {

      axios.post(`${api}/user/info`, JSON.stringify({
          userName : text
      }), {
          headers: {
              'Content-Type': 'application/json'
          }
      })
          .then((res) => {
              console.log(res.data);
          })
          .catch((e) => {
              console.log(e);
          });
  }

  return (
    <SafeAreaView>

      <TextInput value={text} onChangeText={(newText) => setText(newText)}  style={{backgroundColor : "green"}}/>
      <Button title={"보내기"} onPress={() => send()}/>

    </SafeAreaView>
  );
}


export default App;
