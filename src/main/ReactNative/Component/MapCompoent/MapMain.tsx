import React, {useState} from 'react';
import {Button, View} from 'react-native';
import {
  Camera,
  NaverMapMarkerOverlay,
  NaverMapView,
} from '@mj-studio/react-native-naver-map';
import axios, {AxiosError} from 'axios';
import Config from 'react-native-config';

export default function MapMain() {
  const [camera, setCamera] = useState<Camera>({
    latitude: 37.50497126,
    longitude: 127.04905021,
    zoom: 15,
  });
  const naver_search_api_client_id = Config.NAVER_SEARCH_API_CLIENT_ID;
  const naver_search_api_client_secret = Config.NAVER_SEARCH_API_CLIENT_SECRET;
  const [lat, setLat] = useState(0);
  const [lon, setLon] = useState(0);
  const test = async () => {
    console.log('start');
    console.log(camera);
    // try {
    //   const data = await axios.get(
    //     'https://openapi.naver.com/v1/search/local.json',
    //     {
    //       params: {
    //         query: '다이소 신림동',
    //         display: 5,
    //       },
    //       headers: {
    //         'X-Naver-Client-Id': naver_search_api_client_id, // 여기에 네이버 개발자 센터에서 발급받은 Client ID를 입력하세요
    //         'X-Naver-Client-Secret': naver_search_api_client_secret, // 여기에 네이버 개발자 센터에서 발급받은 Client Secret을 입력하세요
    //       },
    //     },
    //   );
    //   console.log(data.data);
    //   console.log();
    //   data.data.items.forEach((element: any) => {
    //     console.log(element);
    //     console.log(element.address);
    //     console.log(element.title);
    //     console.log();
    //   });
    // } catch (error) {
    //   if (error instanceof AxiosError) console.log(error.response);
    // }
    console.log('end');
  };
  return (
    <>
      <Button title="search" onPress={test} />
      <View style={{flex: 1}}>
        <NaverMapView
          style={{flex: 1}}
          initialCamera={camera}
          onCameraChanged={c => {
            setCamera(c);
          }}
          onTapMap={params => {
            setLat(params.latitude);
            setLon(params.longitude);
          }}>
          {lat !== 0 && (
            <NaverMapMarkerOverlay
              latitude={lat}
              longitude={lon}
              onTap={() => {
                setLat(0);
              }}
              anchor={{x: 0.5, y: 1}}
            />
          )}
        </NaverMapView>
      </View>
    </>
  );
}
