import createStyles from "./styles.ts";
import {Alert, Image, Text, TextInput, TouchableOpacity, View} from "react-native";
import React, {useEffect, useState} from "react";
import {useNavigation, useNavigationState} from "@react-navigation/native";
import {NativeStackNavigationProp} from "@react-navigation/native-stack";
import {RootStackParamList} from "../../CommonTypes/RootStackParamList.ts";
import Config from "react-native-config";
import axios from "axios";
import AsyncStorage from '@react-native-async-storage/async-storage';
import axiosPost from "../../Util/AxiosUtil.ts";


export default function LoginForm() {
    const styles = createStyles()
    const navigation = useNavigation<NativeStackNavigationProp<RootStackParamList>>();
    const api = Config.API_BASE_URL;
    const [userId ,setUserId] = useState('');
    const [passwd, setPasswd] = useState('');

    useEffect(() => {
        const fetchToken = async () => {
            const token = await getToken();
            if (token) {
                console.log("AccessToken : " + token.AccessToken);
                console.log("RefreshToken : " + token.RefreshToken);


                axiosPost.post("/user/userLogin", JSON.stringify({"userId" : "1", "passwd" : "2"}))
            }



        };

        fetchToken();
    }, []);

    const getToken = async () => {
        try {
            const accessToken = await AsyncStorage.getItem('AccessToken');
            const refreshToken = await AsyncStorage.getItem('RefreshToken');

            const token = {
                AccessToken: accessToken,
                RefreshToken: refreshToken
            };

            return token;
        } catch (error) {
            console.error('Error reading token', error);
        }
    };

    const userLogin = () => {
        axios.post(api+'/user/userLogin', JSON.stringify({
            userId,
            passwd
        }), {
            headers : {
                "Content-Type" : "application/json"
            }
        }).then(async (res) => {
            if (res.data != null) {
                Alert.alert("로그인 되었습니다.")
            console.log("token :" + res.data)
                await AsyncStorage.setItem("AccessToken", res.data["AccessToken"]);
                await AsyncStorage.setItem("RefreshToken", res.data["RefreshToken"]);

                setTimeout(() => {
                    navigation.navigate('MapMain')
                }, 1000)

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