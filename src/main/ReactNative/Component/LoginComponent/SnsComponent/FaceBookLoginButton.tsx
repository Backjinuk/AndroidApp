import {Image, TouchableOpacity} from "react-native";
import React from "react";

// @ts-ignore
export default function FaceBookLoginButton({styles}) {
    return (
        <TouchableOpacity style={styles.socialButton}>
            <Image source={require('../assets/facebook.png')} style={styles.icon} />
        </TouchableOpacity>
    )
}