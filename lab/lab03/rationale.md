# Rationale - Lab 03 Cars - Tianwei Mo

I construct a "Owner" class, which has fields "name" and "address". Because A car has its meaning without the owner, their relationship should be aggregation.

I add two engine types, nuclear and solar. Similar as thermal and electrical, they are a type of engine, so they inherits the engine class. Nuclear engine has nuclear energy as a special field and special propel function.

To achieve flying, I add an interface "Flying". Planes, flying cars and time travelling flying cars are able to fly, so they should implement "Flying" interface. Becasuse flying cars and time travelling flying cars are all a kind of cars, they inherits the car class. Specifically, time travelling flying cars should be a kind of time travelling car. Therefore, they only need to construct field "z", while planes class need to include all three fields. Because the three types of vehicle implement "Flying" interface, they should connect with "Flying" with a dot strings with triangle arrows.