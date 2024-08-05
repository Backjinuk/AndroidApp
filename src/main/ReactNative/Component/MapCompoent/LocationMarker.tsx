import React from 'react';
import {NaverMapMarkerOverlay} from '@mj-studio/react-native-naver-map';

export default function LocationMarker(props: any) {
  const location: Location = props.location;
  const setPosition = (location: Location) => {
    props.setPosition(location);
  };
  return (
    <NaverMapMarkerOverlay
      key={location.latitude + location.longitude + location.title}
      latitude={location.latitude}
      longitude={location.longitude}
      onTap={() => {
        setPosition(location);
      }}
      anchor={{x: 0.5, y: 1}}
    />
  );
}
