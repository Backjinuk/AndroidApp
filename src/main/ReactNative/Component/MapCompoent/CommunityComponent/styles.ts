import { StyleSheet } from 'react-native';

const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 16,
    },
    header: {
        marginBottom: 16,
    },
    headerTitle: {
        fontSize: 24,
        fontWeight: 'bold',
    },
    formGroup: {
        marginBottom: 16,
    },
    inputContainer: {
        borderBottomWidth: 1,
        borderBottomColor: '#ccc',
        paddingVertical: 8,
    },
    textAreaContainer: {
        borderBottomWidth: 1,
        borderBottomColor: '#ccc',
        paddingVertical: 8,
        height: 100,
    },
    map: {
        height: 200,
        marginTop: 8,
        marginBottom: 8,
    },
    textArea: {
        height: 100,
        textAlignVertical: 'top',
    },
    input: {
        borderWidth: 1,
        borderColor: '#ccc',
        borderRadius: 4,
        padding: 8,
        fontSize: 16,
    },


    container2: {
        display : "flex",
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        // 추가적인 스타일링이 필요할 수 있음
        height: '20%',
    },
    infoCard: {
        display : "flex",
        position: 'absolute',
        top: 10, // 상단에 위치하도록 설정
        width: '90%',
        backgroundColor: 'white',
        borderRadius: 10,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.8,
        shadowRadius: 2,
        elevation: 5,
        padding: 15,
        flexDirection: 'row',
    },
    cardHeader: {
        flex: 3,
    },
    title: {
        fontSize: 16,
        fontWeight: 'bold',
        marginBottom: 5,
    },
    subInfo: {
        fontSize: 14,
        color: '#555',
    },
    image: {
        flex: 1,
        height: 50,
        borderRadius: 10,
        marginLeft: 10,
    },

    buttonContainer: {
        flexDirection : 'row',
        left : 110,
        width: '20%',
        height : 45,
        padding: 10,
        top : 80,
    },

    button: {
        backgroundColor: '#007BFF', // 버튼 배경 색상
        padding: 5,
        borderRadius: 5,
        marginLeft : 5,

    },

    buttonText: {
        color: '#FFFFFF', // 글자 색상
        fontSize: 13, // 글자 크기
        fontWeight : "bold",
        textAlign: 'center',
    },
});

export default styles;
