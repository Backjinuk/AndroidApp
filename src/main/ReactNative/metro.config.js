const { getDefaultConfig, mergeConfig } = require('@react-native/metro-config');
const path = require('path');

module.exports = (async () => {
    const defaultConfig = await getDefaultConfig(__dirname);

    const config = {
        resolver: {
            assetExts: defaultConfig.resolver.assetExts.filter(ext => ext !== 'svg'),
            sourceExts: [...defaultConfig.resolver.sourceExts, 'jsx', 'js', 'ts', 'tsx'],
        },
        transformer: {
            // 필요 시 추가 설정
        },
    };

    return mergeConfig(defaultConfig, config);
})();