import {Image, Text, TouchableOpacity, View} from "react-native";
import React from "react";
import createStyles from "./styles.ts";

export default function LoginMain(){
    const styles = createStyles();
    return (
        <View style={styles.container}>
            <Text style={styles.title}>The JoongAng</Text>
            <Text style={styles.subtitle}>회원가입</Text>
            <Text style={styles.description}>회원가입 방식을 선택해 주세요.</Text>

            <View style={styles.socialButtonsContainer}>
                <TouchableOpacity style={styles.socialButton}>
                    <Image source={require('./assets/naver.png')} style={styles.icon} />
                </TouchableOpacity>
                <TouchableOpacity style={styles.socialButton}>
                    <Image source={require('./assets/kakao.png')} style={styles.icon} />
                </TouchableOpacity>
                <TouchableOpacity style={styles.socialButton}>
                    <Image source={require('./assets/google.png')} style={styles.icon} />
                </TouchableOpacity>
                <TouchableOpacity style={styles.socialButton}>
                    <Image source={require('./assets/apple.png')} style={styles.icon} />
                </TouchableOpacity>
                <TouchableOpacity style={styles.socialButton}>
                    <Image source={require('./assets/facebook.png')} style={styles.icon} />
                </TouchableOpacity>
            </View>

            <Text style={styles.orText}>또는</Text>

            <TouchableOpacity style={styles.signupButton}>
                <Text style={styles.signupButtonText}>조인스로 회원가입</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.signupButton}>
                <Text style={styles.signupButtonText}>이메일로 회원가입</Text>
            </TouchableOpacity>

            {/*<TouchableOpacity onPress={() => navigation.navigate('Login')}>*/}
            <TouchableOpacity>
                <Text style={styles.loginText}>이미 회원이신가요? 로그인</Text>
            </TouchableOpacity>
        </View>
    );
}