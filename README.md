# ChicagoCTABusTracker

This app allows users to track CTA buses in real-time, providing information about routes, stops, and bus arrival predictions. It integrates the CTA Bus Tracker API to provide up-to-date bus data and utilizes several modern Android features for enhanced functionality.

## App Highlights

- **SplashScreen API**: Displays a splash screen while data is being loaded.
- **Location Services**: Determines the user's location and displays nearby bus stops within 1000m.
- **Ad Integration**: Displays banner ads in each activity using **AdMob** or **Unity Ads**.
- **Data Caching**: Caches route, direction, and stop data for 24 hours for performance optimization.
- **Real-Time Bus Predictions**: Shows predicted bus arrival times for selected stops.
- **Custom Alerts**: Displays customer service alerts for routes and directions (Extra Credit feature).

## Features

### MainActivity
- Displays all CTA bus routes.
- A **search field** filters the route list based on entered text.
- Tapping on a route shows available directions and stops within 1000m of the user’s location.

### StopsActivity
- Displays stops for the selected route and direction within 1000m of the user's location.
- Selecting a stop opens **PredictionsActivity**, showing predicted bus arrivals.

### PredictionsActivity
- Displays predicted bus arrival times for the selected stop.
- Swiping down in the PredictionsActivity refreshes bus arrival data.
- Tapping on a prediction shows the distance and time from the selected stop.

## API Integration

The app retrieves data from the [CTA Bus Tracker API](https://www.transitchicago.com/developers/bustracker/). The following endpoints are used:

- **Routes**: [Retrieve routes](http://www.ctabustracker.com/bustime/api/v2/getroutes)
- **Route Directions**: [Retrieve directions](http://www.ctabustracker.com/bustime/api/v2/getdirections)
- **Stops**: [Retrieve stops](http://www.ctabustracker.com/bustime/api/v2/getstops)
- **Predictions**: [Retrieve predictions](http://www.ctabustracker.com/bustime/api/v2/getpredictions)
- **Vehicles**: [Retrieve vehicle info](http://www.ctabustracker.com/bustime/api/v2/getvehicles)

### API Endpoints:
- **`getroutes`**: Retrieves a list of routes in the CTA system.
- **`getdirections`**: Retrieves available directions for a specific route.
- **`getstops`**: Retrieves bus stops for a specific route and direction.
- **`getpredictions`**: Retrieves predictions for a specific stop or vehicle.
- **`getvehicles`**: Retrieves current vehicle information for a specific bus.

## Distance Calculation

The app uses the **location services** to get the user's current location and calculate the distance to each bus stop. Only stops within **1000 meters (1 km)** of the user’s location are displayed. The distance is calculated using the **Haversine formula** or the `Location.distanceTo()` method.

## Setup
- Obtain an API key from [CTA Bus](https://www.ctabustracker.com/account)  Tracker.
- Add your API key to the app's [configuration](app/build.gradle.kts).
  
```java
....
 buildTypes {
        debug {
            buildConfigField("String", "API_KEY", "\"Your API KEY\"")
        }
....
```

## Download APK for Testing
You can download the APK file for testing from the release below:
- [Download v1.0 Testing APK](https://github.com/kartiknagar333/VisualCrossingWeatherApp/releases/tag/v1.0)
<br>  

## Screenshots
- ****
