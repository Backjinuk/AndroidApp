

/**
 * Metro configuration
 * https://reactnative.dev/docs/metro
 *
 * @type {import('metro-config').MetroConfig}
 */
const { getDefaultConfig, mergeConfig } = require('@react-native/metro-config');

const config = {};
// transformer: {
//     babelTransformerPath: require.resolve('react-native-reanimated/plugin'),
// },
// resolver: {
//     sourceExts: ['js', 'json', 'ts', 'tsx', 'cjs'], // 확장자 설정
// },
module.exports = mergeConfig(getDefaultConfig(__dirname), config);


