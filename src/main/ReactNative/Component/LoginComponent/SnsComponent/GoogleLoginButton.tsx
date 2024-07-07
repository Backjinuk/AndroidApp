import {Image, TouchableOpacity} from "react-native";
import React, {useEffect, useState} from "react";
import {GoogleSignin} from '@react-native-google-signin/google-signin';
import auth from '@react-native-firebase/auth';
import Config from "react-native-config";

// @ts-ignore
export default function GoogleLoginButton({styles}) {
    const [token, setIdToken] = useState('');

    useEffect(() => {
        googleSigninConfigure();
    }, []);


    const googleSigninConfigure = () => {
        console.log(Config.GOOGLE_CLIENT_ID)
        GoogleSignin.configure({
            webClientId: Config.GOOGLE_CLIENT_ID, // Firebase 콘솔에서 가져온 웹 클라이언트 ID
            offlineAccess: true, // 필요한 경우 오프라인 액세스 설정
            forceCodeForRefreshToken: true, // 필요한 경우 리프레시 토큰 강제 설정
            hostedDomain: '',
        });

    }

    const onGoogleButtonPress = async () => {
        try {
            console.log("Checking for Google Play Services...");
            await GoogleSignin.hasPlayServices({ showPlayServicesUpdateDialog: true });
            console.log("Google Play Services available.");
            console.log("Attempting Google Sign-In...");
            const { idToken } = await GoogleSignin.signIn();
            console.log("Google Sign-In successful. ID Token: ", idToken);
            const googleCredential = auth.GoogleAuthProvider.credential(idToken);
            console.log("Google Credential created.");
            return auth().signInWithCredential(googleCredential);
        } catch (e) {
            console.error("Error during Google Sign-In process: ", e);
        }
    }




    return (
        <TouchableOpacity style={styles.socialButton} onPress={() => onGoogleButtonPress()}>
            <Image source={require('../assets/google.png')} style={styles.icon} />
        </TouchableOpacity>
    )
}