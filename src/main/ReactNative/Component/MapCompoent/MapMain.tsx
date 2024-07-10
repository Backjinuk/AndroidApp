import React, {useState} from 'react';
import {Alert, Button, Linking, Text, TextInput, View} from 'react-native';
import {
  Camera,
  NaverMapMarkerOverlay,
  NaverMapView,
  Region,
} from '@mj-studio/react-native-naver-map';
import axios, {AxiosError} from 'axios';
import Config from 'react-native-config';
import Geolocation from '@react-native-community/geolocation';

interface Position {
  title?: string;
  address?: string;
  latitude: number;
  longitude: number;
}
interface Location {
  address: string;
  latitude: number;
  longitude: number;
  title: string;
}
export default function MapMain() {
  const debug = true;
  const log = (message?: any, ...optionalParams: any[]) => {
    if (debug) console.log(message, ...optionalParams);
  };
  const [lat, setLat] = useState(0);
  const [lon, setLon] = useState(0);
  const [camera, setCamera] = useState<Camera>();
  const [keyword, setKeyword] = useState('');
  const [locations, privateSetLocations] = useState<Location[]>([]);
  const [region, setRegion] = useState<Region>();
  const setLocations = (locations: any[]) => {
    log(locations);
    const {array, region} = analyzeLocations(locations);
    setRegion(undefined);
    setRegion(region);
    log(region);
    privateSetLocations(array);
  };
  const [position, privateSetPosition] = useState<Position | undefined>(
    undefined,
  );
  const setPosition = async (position: Position) => {
    const info = await reverseGeocoding(position);
    log(info.results[1]);
    //도로명 주소가 없을 경우
    if (info.results[1] === undefined) {
      //도로명 주소가 있을 경우
    } else {
      // position.address  =
    }
    // privateSetPosition(info)
  };
  const naver_search_api_client_id = Config.NAVER_SEARCH_API_CLIENT_ID;
  const naver_search_api_client_secret = Config.NAVER_SEARCH_API_CLIENT_SECRET;
  const naver_map_api_client_id = Config.NAVER_MAP_API_CLIENT_ID;
  const naver_map_api_client_secret = Config.NAVER_MAP_API_CLIENT_SECRET;

  // 지역 검색 결과 분석
  const analyzeLocations = (locations: any[]) => {
    const array: Location[] = [];
    const indexSet: string[] = [];
    let minLat = 1000;
    let maxLat = 0;
    let minLon = 1000;
    let maxLon = 0;
    locations.forEach(e => {
      const addressArray = e.address.split(' ');
      let address = addressArray[0];
      for (let index = 1; index < 4; index++) {
        address += ' ' + addressArray[index];
      }
      const lon = e.mapx / 10000000;
      const lat = e.mapy / 10000000;
      log(e.address);
      log({address, latitude: lat, longitude: lon, title: e.title});
      if (!indexSet.includes(address)) {
        array.push({address, latitude: lat, longitude: lon, title: e.title});
        minLat = Math.min(minLat, lat);
        maxLat = Math.max(maxLat, lat);
        minLon = Math.min(minLon, lon);
        maxLon = Math.max(maxLon, lon);
        indexSet.push(address);
      }
    });
    const delLat = maxLat - minLat;
    const delLon = maxLon - minLon;
    const del = Math.min(delLat, delLon);
    const region: Region = {
      latitude: minLat - del,
      longitude: minLon - del,
      latitudeDelta: 2 * del + delLat,
      longitudeDelta: 2 * del + delLon,
    };
    return {array, region};
  };

  // 주소 문자열로 치환
  const analyzeAddress = () => {};

  // 키워드로 검색
  const searchLocation = async (keyword: string, dongname: string) => {
    const locationKeyword = keyword + ' ' + dongname;
    const aroundLocations = await getLocationData(locationKeyword);
    if (aroundLocations.length === 0) {
      const locations = await getLocationData(keyword);
      setLocations(locations);
    } else {
      setLocations(aroundLocations);
    }
  };

  const getLocationData = async (str: string) => {
    const data = await axios.get(
      'https://openapi.naver.com/v1/search/local.json',
      {
        params: {
          query: str,
          display: 5,
        },
        headers: {
          'X-Naver-Client-Id': naver_search_api_client_id, // 여기에 네이버 개발자 센터에서 발급받은 Client ID를 입력하세요
          'X-Naver-Client-Secret': naver_search_api_client_secret, // 여기에 네이버 개발자 센터에서 발급받은 Client Secret을 입력하세요
        },
      },
    );
    const items = data.data.items;
    return items;
  };

  const reverseGeocoding = async (position: Camera | Position) => {
    if (position === undefined) return;
    const data = await axios.get(
      'https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc',
      {
        params: {
          coords: position.longitude + ',' + position.latitude,
          orders: 'admcode,roadaddr',
          output: 'json',
        },
        headers: {
          'X-NCP-APIGW-API-KEY-ID': naver_map_api_client_id, // 여기에 네이버 개발자 센터에서 발급받은 Client ID를 입력하세요
          'X-NCP-APIGW-API-KEY': naver_map_api_client_secret, // 여기에 네이버 개발자 센터에서 발급받은 Client Secret을 입력하세요
        },
      },
    );
    return data.data;
  };

  const getDongName = async () => {
    if (camera === undefined) return;
    const data = await reverseGeocoding(camera);
    const dongname = data.results[0].region.area3.name;
    log(dongname);
    return dongname;
  };

  const getLocations = async () => {
    const dongname = await getDongName();
    await searchLocation(keyword, dongname);
  };

  const NAVER_MAP_INSTALL_LINK = 'market://details?id=com.nhn.android.nmap';
  const findRoute = async (location: Position) => {
    const title = getLocationTitle(location);
    const url = `nmap://route/walk?dlat=${location.latitude}&dlng=${location.longitude}&dname=${title}&appname=com.reactnative`;
    log(url);

    if (await Linking.canOpenURL(url)) {
      await Linking.openURL(url);
    } else {
      await Linking.openURL(NAVER_MAP_INSTALL_LINK);
    }
  };

  const getLocationTitle = (location: Position) => {
    const title = location?.title;
    if (title === undefined) {
      const data = reverseGeocoding(location);
      console.log(data);
      // return encodeURI(
      // title.replace(/<[^>]+>/g, ' ').replace(/\s+/g, ' '),
      // );
      return '';
    } else {
      return encodeURI(title.replace(/<[^>]+>/g, ' ').replace(/\s+/g, ' '));
    }
  };

  const tapMap = () => {};

  return (
    <>
      <TextInput
        value={keyword}
        onChangeText={e => {
          setKeyword(e);
        }}
      />
      <Button title="search" onPress={getLocations} />
      <View style={{flex: 1}}>
        <NaverMapView
          maxZoom={18}
          minZoom={9}
          style={{flex: 1}}
          onCameraChanged={camera => {
            setCamera(camera);
          }}
          animationDuration={500}
          region={region}
          onTapMap={params => {
            setPosition(params);
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
          {locations.length !== 0 &&
            locations.map(location => (
              <NaverMapMarkerOverlay
                key={location.latitude + ' ' + location.longitude}
                latitude={location.latitude}
                longitude={location.longitude}
                onTap={() => {
                  setPosition(location);
                }}
                anchor={{x: 0.5, y: 1}}
              />
            ))}
        </NaverMapView>
      </View>
      <View>
        <Text>{position?.title}</Text>
        <Button
          title="길찾기"
          onPress={() => (position ? findRoute(position) : () => {})}
        />
      </View>
    </>
  );
}
