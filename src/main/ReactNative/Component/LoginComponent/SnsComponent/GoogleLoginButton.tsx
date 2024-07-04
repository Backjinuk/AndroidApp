import {Image, TouchableOpacity} from "react-native";
import React, {useEffect, useState} from "react";
import {
    GoogleSignin,
    GoogleSigninButton,
} from '@react-native-google-signin/google-signin';
import auth from '@react-native-firebase/auth';
import Config from "react-native-config";

// @ts-ignore
export default function GoogleLoginButton({styles}) {
    const [token, setIdToken] = useState('');

    useEffect(() => {
        GoogleSignin.configure({
            webClientId: Config.GOOGLE_CLIENT_ID, // Firebase 콘솔에서 가져온 웹 클라이언트 ID
        });
    }, []);

    const onPressGoogleBtn = async () => {

        try {
            await GoogleSignin.hasPlayServices({showPlayServicesUpdateDialog: true});
            const {idToken} = await GoogleSignin.signIn();
            console.log('idToekn : ', idToken);
            if (idToken) {
                setIdToken(idToken);
            }
            const googleCredential = auth.GoogleAuthProvider.credential(idToken);
            const res = await auth().signInWithCredential(googleCredential);
        }catch(error){
            console.error(error);
        }
    };
    return (
        <TouchableOpacity style={styles.socialButton} onPress={() => onPressGoogleBtn()}>
            <Image source={require('../assets/google.png')} style={styles.icon} />
        </TouchableOpacity>
    )
}