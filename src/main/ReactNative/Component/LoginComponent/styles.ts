import {StyleSheet} from 'react-native';

interface StylesProps {
    BorderBottomColor: string;
}

const createStyles = () => {
    return StyleSheet.create({
        container: {
            flex: 1,
            backgroundColor: '#fff',
            alignItems: 'center',
            justifyContent: 'center',
            paddingHorizontal: 20,
        },
        title: {
            fontSize: 24,
            fontWeight: 'bold',
            marginBottom: 10,
        },
        subtitle: {
            fontSize: 20,
            marginBottom: 5,
        },
        description: {
            fontSize: 16,
            color: '#888',
            marginBottom: 20,
        },
        socialButtonsContainer: {
            flexDirection: 'row',
            marginBottom: 20,
        },
        socialButton: {
            marginHorizontal: 5,
        },
        icon: {
            width: 50,
            height: 50,
        },
        orText: {
            fontSize: 16,
            color: '#888',
            marginVertical: 10,
        },
        signupButton: {
            backgroundColor: '#eee',
            paddingVertical: 10,
            paddingHorizontal: 20,
            borderRadius: 5,
            marginVertical: 5,
            width: '100%',
            alignItems: 'center',
        },
        signupButtonText: {
            fontSize: 16,
        },
        loginText: {
            fontSize: 14,
            color: '#888',
            marginTop: 20,
        },
    });
}

export default createStyles;