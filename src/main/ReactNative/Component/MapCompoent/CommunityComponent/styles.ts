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
    searchBox: {
        flexDirection: 'row',
        alignItems: 'center',
        backgroundColor: '#fff',
        borderRadius: 25,
        paddingVertical: 5,
        paddingHorizontal: 10,
        height: 50,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 6,
        elevation : 20
    },
    input2: {
        flex: 1,
        fontSize: 16,
        paddingHorizontal: 10,
        color: '#333',
    },
    iconButton: {
        padding: 5,
    },

    InfoViewContainer: {
        height: '15%',
        width: '100%',
        borderTopRightRadius: 10,
        borderTopLeftRadius: 10,
        backgroundColor: "#fff",
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.1,
        shadowRadius: 6,
        elevation: 20,
        opacity: 1, // Opacity 값은 0부터 1까지 설정해야 함
    },

    infoCard: {
        display : "flex",
        position: 'absolute',
        top: 10, // 상단에 위치하도록 설정
        width: '100%',
        padding: 20,
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
        left : "390%",
        width: '23%',
        height : "63%",
        padding: 10,
        top : "19%",
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
    searchContainer: {
        position: 'absolute',
        top: 20,
        left: 10,
        right: 10,
        zIndex: 1, // 지도보다 약간 낮은 값
    },


});

export default styles;
