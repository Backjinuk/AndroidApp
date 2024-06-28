import React, {useState} from 'react';
import {TouchableOpacity, Image, Alert} from 'react-native';
import {login, logout, unlink} from "@react-native-seoul/kakao-login";

// @ts-ignore
export default function KakaoLoginButton({ styles }){
    const [result, setResult] = useState<string>("");

    const signInWithKakao = async (): Promise<void> => {
        try {
            const token = await login();
            setResult(JSON.stringify(token));
            console.log('Token:', token);
        } catch (err) {
            console.error("login err", err);
        }
    };

    const signOutWithKakao = async (): Promise<void> => {
        try {
            const message = await logout();
            setResult(message);
            console.log(message);
        } catch (err) {
            console.error("signOut error", err);
        }
    };

    const unlinkKakao = async (): Promise<void> => {
        try {
            const message = await unlink();
            setResult(message);
            console.log(message);
        } catch (err) {
            console.error("unlink error", err);
        }
    };

    return (
        <TouchableOpacity style={styles.socialButton} onPress={() => signInWithKakao()}>
            <Image source={require('../assets/kakao.png')} style={styles.icon} />
        </TouchableOpacity>
    );
};
