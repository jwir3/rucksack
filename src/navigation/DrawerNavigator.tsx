import React from 'react';
import { View, Text, ScrollView } from 'react-native';
import {
  createDrawerNavigator,
  DrawerItemList,
  DrawerContentComponentProps,
} from '@react-navigation/drawer';
import { useSafeAreaInsets } from 'react-native-safe-area-context';
import { TicketsPlane, ListChecks, Volleyball } from 'lucide-react-native';
import DashboardScreen from '../screens/DashboardScreen';
import PlaceholderScreen from '../screens/PlaceholderScreen';

export type DrawerParamList = {
  Dashboard: undefined;
  MasterInventory: undefined;
  Pastimes: undefined;
};

const Drawer = createDrawerNavigator<DrawerParamList>();

function DrawerContent(props: DrawerContentComponentProps) {
  const insets = useSafeAreaInsets();

  return (
    <ScrollView style={{ flex: 1 }} bounces={false}>
      {/* Orange header — full bleed to all edges with manual status bar padding */}
      <View
        className="bg-primary"
        style={{ paddingTop: insets.top + 16, paddingBottom: 24, paddingHorizontal: 20 }}
      >
        <Text className="text-white text-xl font-bold">Rucksack</Text>
        <Text className="text-white/70 text-xs mt-1">Travel, simplified.</Text>
      </View>

      {/* Nav items */}
      <View className="pt-2">
        <DrawerItemList {...props} />
      </View>
    </ScrollView>
  );
}

export default function DrawerNavigator() {
  return (
    <Drawer.Navigator
      drawerContent={(props) => <DrawerContent {...props} />}
      screenOptions={{
        headerShown: false,
        drawerActiveTintColor: '#FFAD1C',
        drawerInactiveTintColor: '#374151',
        drawerActiveBackgroundColor: '#FFF3DC',
        drawerItemStyle: { borderRadius: 8 },
        drawerLabelStyle: { fontSize: 15 },
      }}
    >
      <Drawer.Screen
        name="Dashboard"
        component={DashboardScreen}
        options={{
          title: 'My Trips',
          drawerIcon: ({ color, size }) => (
            <TicketsPlane color={color} size={size} />
          ),
        }}
      />
      <Drawer.Screen
        name="MasterInventory"
        options={{
          title: 'Master Inventory',
          drawerIcon: ({ color, size }) => (
            <ListChecks color={color} size={size} />
          ),
        }}
      >
        {() => <PlaceholderScreen title="Master Inventory" />}
      </Drawer.Screen>
      <Drawer.Screen
        name="Pastimes"
        options={{
          title: 'Pastimes',
          drawerIcon: ({ color, size }) => (
            <Volleyball color={color} size={size} />
          ),
        }}
      >
        {() => <PlaceholderScreen title="Pastimes" />}
      </Drawer.Screen>
    </Drawer.Navigator>
  );
}
