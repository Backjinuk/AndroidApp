import React, {useState} from 'react';
import { TouchableOpacity, Image } from 'react-native';
import Config from "react-native-config";
import NaverLogin, {GetProfileResponse, NaverLoginResponse} from "@react-native-seoul/naver-login";


// @ts-ignore
export default function NaverLoginButton({ styles }){
    const [success, setSuccessResponse]  = useState<NaverLoginResponse['successResponse']>();
    const [failure, setFailureResponse]  = useState<NaverLoginResponse['failureResponse']>();
    const [getProfileRes, setGetProfileRes] = useState<GetProfileResponse>();

    const consumerKey = Config.NAVER_KEY as string;
    const consumerSecret = Config.NAVER_SECRET_KEY as string;
    const appName = 'MoGakCo';
    const serviceUrlSchemeIOS  = 'navertest' as string;


    // 초기화 함수
    NaverLogin.initialize({
        appName,
        consumerKey,
        consumerSecret,
        serviceUrlSchemeIOS, // 수정된 부분
    });

    const login = async () => {
        try {
            const response = await NaverLogin.login();
            if (response.isSuccess && response.successResponse) {
                setSuccessResponse(response.successResponse);
                const profileResult = await NaverLogin.getProfile(response.successResponse.accessToken);
                setGetProfileRes(profileResult);

                console.log("response.successResponse : " + response.successResponse.accessToken)
                console.log("profileResult : " + profileResult)
            } else if (response.failureResponse) {
                // @ts-ignore
                const { lastErrorCodeFromNaverSDK, lastErrorDescriptionFromNaverSDK } = response;
                if (lastErrorCodeFromNaverSDK === 'user_cancel') {
                    console.log('User cancelled the login process.');
                    // 사용자가 취소했을 때 처리할 로직 추가
                } else {
                    console.error('Login failed:', lastErrorDescriptionFromNaverSDK);
                    // 로그인 실패 시 처리할 로직 추가
                }
                setFailureResponse(response.failureResponse);
            }
        } catch (error) {
            console.error('Login error', error);
        }
    };



    return (
        <TouchableOpacity style={styles.socialButton} onPress={() => login()}>
            <Image source={require('../assets/naver.png')} style={styles.icon} />
        </TouchableOpacity>
    );
};


