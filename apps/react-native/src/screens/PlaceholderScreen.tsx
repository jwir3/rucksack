import React from 'react';
import { View, Text } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

type Props = { title: string };

export default function PlaceholderScreen({ title }: Props) {
  return (
    <SafeAreaView className="flex-1 bg-white items-center justify-center">
      <Text className="text-lg font-medium text-gray-600">{title}</Text>
      <Text className="text-sm text-gray-400 mt-1">Coming soon</Text>
    </SafeAreaView>
  );
}
