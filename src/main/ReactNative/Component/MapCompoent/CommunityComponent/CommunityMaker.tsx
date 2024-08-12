import React, {useEffect, useState} from 'react';
import {NaverMapMarkerOverlay} from '@mj-studio/react-native-naver-map';

export default function CommunityMaker(props: any) {

    const [marker, setMarker] = useState<Community | null>(null);

    useEffect(() => {
        setMarker(props.marker);
    }, [props.marker]);

    if (!marker) {
        return null; // marker가 없으면 아무 것도 렌더링하지 않음
    }

    const setPosition = (marker: Community) => {
        props.setPosition(marker);
    };

    return (
        <>
            <NaverMapMarkerOverlay
                key={marker.commuSeq} // 고유한 key 값 사용
                latitude={marker.latitude}
                longitude={marker.longitude}
                onTap={() => {
                    setPosition(marker);
                    props.setMaker(marker)
                    props.setOpenModal()
                    //Alert.alert(marker.commuTitle);
                }}
                anchor={{ x: 0.5, y: 1 }}
            />
        </>
    );
}
