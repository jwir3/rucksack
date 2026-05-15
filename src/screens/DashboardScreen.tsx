import React from 'react';
import { View, Text, TouchableOpacity, StatusBar } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';
import { DrawerNavigationProp } from '@react-navigation/drawer';
import { BriefcaseBusiness, Menu, Plus } from 'lucide-react-native';

type Props = {
  navigation: DrawerNavigationProp<Record<string, undefined>>;
};

export default function DashboardScreen({ navigation }: Props) {
  return (
    <SafeAreaView className="flex-1 bg-white" edges={['top', 'left', 'right']}>
      <StatusBar backgroundColor="#FFAD1C" barStyle="dark-content" />

      {/* App bar */}
      <View className="flex-row items-center bg-primary px-4 h-14">
        <TouchableOpacity onPress={() => navigation.openDrawer()} hitSlop={8}>
          <Menu color="#fff" size={24} />
        </TouchableOpacity>
        <Text className="flex-1 text-white text-lg font-semibold ml-4">
          Rucksack
        </Text>
      </View>

      {/* Empty state */}
      <View className="flex-1 items-center justify-center px-8">
        <BriefcaseBusiness color="#FFAD1C" size={80} strokeWidth={1.2} />
        <Text className="mt-6 text-xl font-semibold text-gray-800 text-center">
          No trips yet
        </Text>
        <Text className="mt-2 text-sm text-gray-500 text-center">
          Tap the button below to plan your first trip.
        </Text>
      </View>

      {/* FAB */}
      <TouchableOpacity
        className="absolute bottom-8 right-6 w-14 h-14 rounded-full bg-primary items-center justify-center shadow-lg"
        activeOpacity={0.85}
        onPress={() => {/* TODO: open Trip Creation */}}
      >
        <Plus color="#fff" size={28} />
      </TouchableOpacity>
    </SafeAreaView>
  );
}
