import React from 'react';
import { View, Text, Image, StyleSheet, TouchableOpacity } from 'react-native';
import styles from "./styles.ts";

export default function SubscribeInfoCard(props : any) {
    return (
        <View style={styles.card}>
            <Image
                source={{ uri: 'https://your-image-url.com' }} // 사용자의 프로필 이미지 URL로 교체
                style={styles.profileImage}
            />
            <View style={styles.infoContainer}>
                <Text style={styles.name}>Danny McLoan</Text>
                <Text style={styles.role}>Senior Journalist</Text>
               {/* <View style={styles.statsContainer}>
                    <View style={styles.statItem}>
                        <Text style={styles.statValue}>41</Text>
                        <Text style={styles.statLabel}>Articles</Text>
                    </View>
                    <View style={styles.statItem}>
                        <Text style={styles.statValue}>976</Text>
                        <Text style={styles.statLabel}>Followers</Text>
                    </View>
                    <View style={styles.statItem}>
                        <Text style={styles.statValue}>8.5</Text>
                        <Text style={styles.statLabel}>Rating</Text>
                    </View>
                </View>*/}
                <View style={styles.buttonContainer}>
                    <TouchableOpacity style={styles.chatButton}>
                        <Text style={styles.chatButtonText}>CHAT</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.followButton}>
                        <Text style={styles.followButtonText}>FOLLOW</Text>
                    </TouchableOpacity>
                </View>
            </View>
        </View>
    );
}

