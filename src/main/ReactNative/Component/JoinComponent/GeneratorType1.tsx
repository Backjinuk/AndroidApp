import {StyleSheet, Text, TextInput, View} from "react-native";
import React from "react";
import styles from "./styles.ts";
import createStyles from "./styles.ts";

export default function GeneratorType1(props: any) {

    const styles = createStyles(props);

    return (
        <View style={styles.TossTextInputView}>
            <Text style={{
                color: 'lightgray'
            }}>{props.name}</Text>
            <TextInput
                value={props.value}
                style={[styles.TossTextInput, {borderBottomColor: props.BorderBottomColor}]}
                onChangeText={(value) => {
                    props.setState(value);
                }}
                placeholder={props.name}
                onFocus={() => {
                    props.setBoardBottomColor('#0064FF')
                }}
                onEndEditing={() => {
                    props.setBoardBottomColor('lightgray')
                }}
            />
        </View>
    )
};