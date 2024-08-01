import AsyncStorage from "@react-native-async-storage/async-storage";

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

const setToken = (res : any) =>{

    console.log('AccessToken :' + res["AccessToken"] )
    console.log('RefreshToken : ' + res["RefreshToken"])

    AsyncStorage.setItem("AccessToken", res["AccessToken"]);
    AsyncStorage.setItem("RefreshToken", res["RefreshToken"]);
}


export { getToken, setToken };