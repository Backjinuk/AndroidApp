import React from 'react';
import { TouchableOpacity, Image } from 'react-native';

// @ts-ignore
export default function KakaoLogin({ styles }){
    return (
        <TouchableOpacity style={styles.socialButton}>
            <Image source={require('../assets/kakao.png')} style={styles.icon} />
        </TouchableOpacity>
    );
};
