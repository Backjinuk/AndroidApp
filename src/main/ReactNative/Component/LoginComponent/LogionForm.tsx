import createStyles from "./styles.ts";
import {Alert, Image, Text, TextInput, TouchableOpacity, View} from "react-native";
import React, {useState} from "react";
import {useNavigation, useNavigationState} from "@react-navigation/native";
import {NativeStackNavigationProp} from "@react-navigation/native-stack";
import {RootStackParamList} from "../../CommonTypes/RootStackParamList.ts";
import Config from "react-native-config";
import axios from "axios";


export default function LoginForm() {
    const styles = createStyles()
    const navigation = useNavigation<NativeStackNavigationProp<RootStackParamList>>();
    const api = Config.API_BASE_URL;
    const [userId ,setUserId] = useState('');
    const [passwd, setPasswd] = useState('');

    const userLogin = () => {
        axios.post(api+'/user/userLogin', JSON.stringify({
            userId,
            passwd
        }), {
            headers : {
                "Content-Type" : "application/json"
            }
        }).then((res) => {
            if(res.data === 1){
                Alert.alert("로그인 되었습니다.")

                setTimeout(() => {
                    navigation.navigate('MapMain')
                },1000)

            }
        }).catch(e => {
            console.log(e);
        })
    }

    return (
        <View style={styles.container}>
            <Text style={styles.title}>MoGakCo</Text>
            <Text style={styles.subtitle}>로그인</Text>

            <TextInput
                style={styles.input}
                placeholder="이메일을 입력하세요"
                placeholderTextColor="#aaa"
                value={userId}
                onChangeText={(value) => setUserId(value)}
            />

            <TextInput
                style={styles.input}
                placeholder="비밀번호를 입력하세요"
                placeholderTextColor="#aaa"
                secureTextEntry
                value={passwd}
                onChangeText={(value) => setPasswd(value)}
            />

            <View style={styles.checkboxContainer}>
                <Text style={styles.checkboxLabel}>로그인 유지</Text>
            </View>

            <TouchableOpacity style={styles.loginButton} onPress={() => userLogin()}>
                <Text style={styles.loginButtonText} >이메일로 로그인</Text>
            </TouchableOpacity>

            <View style={styles.linkContainer}>
                <Text style={styles.linkText}
                      onPress={ () => {navigation.navigate("Join")}}>
                    회원가입
                </Text>
                <Text style={styles.linkText}>이메일 찾기</Text>
            </View>

            <Text style={styles.simpleLoginText}>간편로그인</Text>
9448
            <View style={styles.socialIconsContainer}>
                <Image source={require('./assets/naver.png')} style={styles.icon} />
                <Image source={require('./assets/kakao.png')} style={styles.icon} />
                <Image source={require('./assets/google.png')} style={styles.icon} />
                <Image source={require('./assets/apple.png')} style={styles.icon} />
                <Image source={require('./assets/facebook.png')} style={styles.icon} />
            </View>
        </View>
    )
}