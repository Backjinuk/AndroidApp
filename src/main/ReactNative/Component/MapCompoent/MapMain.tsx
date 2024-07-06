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
  latitude: number;
  longitude: number;
}
interface Location {
  address: string;
  lat: number;
  lon: number;
  title: string;
}
export default function MapMain() {
  const debug = false;
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
  const [position, setPosition] = useState<Position | undefined>(undefined);
  const naver_search_api_client_id = Config.NAVER_SEARCH_API_CLIENT_ID;
  const naver_search_api_client_secret = Config.NAVER_SEARCH_API_CLIENT_SECRET;
  const naver_map_api_client_id = Config.NAVER_MAP_API_CLIENT_ID;
  const naver_map_api_client_secret = Config.NAVER_MAP_API_CLIENT_SECRET;

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
      log({address, lat, lon, title: e.title});
      if (!indexSet.includes(address)) {
        array.push({address, lat, lon, title: e.title});
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

  const reverseGeocoding = async () => {
    if (camera === undefined) return;
    const data = await axios.get(
      'https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc',
      {
        params: {
          coords: camera.longitude + ',' + camera.latitude,
          orders: 'admcode',
          output: 'json',
        },
        headers: {
          'X-NCP-APIGW-API-KEY-ID': naver_map_api_client_id, // 여기에 네이버 개발자 센터에서 발급받은 Client ID를 입력하세요
          'X-NCP-APIGW-API-KEY': naver_map_api_client_secret, // 여기에 네이버 개발자 센터에서 발급받은 Client Secret을 입력하세요
        },
      },
    );
    const dongname = data.data.results[0].region.area3.name;
    log(dongname);
    return dongname;
  };

  const getLocations = async () => {
    const dongname = await reverseGeocoding();
    await searchLocation(keyword, dongname);
  };

  const getMyPosition = () => {
    Geolocation.getCurrentPosition(
      position => {
        const latitude = Number(JSON.stringify(position.coords.latitude));
        const longitude = Number(JSON.stringify(position.coords.longitude));

        setPosition({
          latitude,
          longitude,
        });
      },
      error => {
        console.log(error.code, error.message);
      },
      {enableHighAccuracy: true, timeout: 15000, maximumAge: 10000},
    );
  };

  const NAVER_MAP_INSTALL_LINK = 'market://details?id=com.nhn.android.nmap';
  const findRoute = async (location: Location) => {
    const title = encodeURI(
      location.title.replace(/<[^>]+>/g, ' ').replace(/\s+/g, ' '),
    );
    const url = `nmap://route/walk?dlat=${location.lat}&dlng=${location.lon}&dname=${title}&appname=com.reactnative`;
    log(url);

    if (await Linking.canOpenURL(url)) {
      await Linking.openURL(url);
    } else {
      await Linking.openURL(NAVER_MAP_INSTALL_LINK);
    }
  };

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
                key={location.lat + ' ' + location.lon}
                latitude={location.lat}
                longitude={location.lon}
                onTap={() => {
                  findRoute(location);
                }}
                anchor={{x: 0.5, y: 1}}
              />
            ))}
        </NaverMapView>
      </View>
    </>
  );
}
