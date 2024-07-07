import React, { useState } from "react";
import { Alert, Image, TouchableOpacity } from "react-native";
import {AccessToken, LoginManager} from "react-native-fbsdk-next";

// @ts-ignore
export default function FaceBookLoginButton({ styles }) {
    const [token, setToken] = useState('');

    const facebookLogin = async () => {
        try {
            const result = await LoginManager.logInWithPermissions(['public_profile']);

            if (result.isCancelled) {
                console.log('Login canceled');
            } else {
                const tokenData = await AccessToken.getCurrentAccessToken();
                if (tokenData) {
                    const accessToken = tokenData.accessToken.toString();
                    console.log('Access Token:', accessToken);
                    // 여기서 accessToken을 이용하여 추가적인 작업 수행 가능
                } else {
                    Alert.alert('Login failed', 'Unable to get access token');
                }
            }
        } catch (error) {
            // @ts-ignore
            Alert.alert('Login error', error.message);
        }
    };

    return (
        <TouchableOpacity style={styles.socialButton} onPress={() => facebookLogin()}>
            <Image source={require('../assets/facebook.png')} style={styles.icon} />
        </TouchableOpacity>
    );
}
