module.exports = {
    presets: ['module:metro-react-native-babel-preset'],
    plugins: [
        // Pluggin to transform private methods in classes
        ['@babel/plugin-transform-private-methods', { loose: true }],
        // Required plugin for React Native Reanimated
        'react-native-reanimated/plugin'
    ],
};
