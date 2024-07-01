import {Image, TouchableOpacity} from "react-native";
import React from "react";

// @ts-ignore
export default function GoogleLoginButton({styles}) {
    return (
        <TouchableOpacity style={styles.socialButton}>
            <Image source={require('../assets/google.png')} style={styles.icon} />
        </TouchableOpacity>
    )
}