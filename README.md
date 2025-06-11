# Car Parking App

A modern Android application for managing car parking spaces with real-time availability tracking and Google Maps integration.

## Features

- Real-time parking space availability
- Google Maps integration for location-based parking
- User authentication and profile management
- Parking history and payment tracking
- Push notifications for parking status updates

## Tech Stack

- **Language**: Kotlin
- **Architecture**: MVVM
- **Dependency Injection**: Hilt
- **Networking**: Retrofit + OkHttp
- **Database**: Room
- **Image Loading**: Coil
- **Testing**: JUnit, Espresso, Mockito
- **CI/CD**: GitHub Actions

## Prerequisites

- Android Studio Hedgehog | 2023.1.1 or later
- JDK 17 or later
- Android SDK 34 or later
- Google Maps API Key
- Git

## Local Development Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/car-parking-app.git
   cd car-parking-app
   ```

2. Create a `local.properties` file in the root directory:
   ```properties
   sdk.dir=/path/to/your/Android/Sdk
   MAPS_API_KEY=your_google_maps_api_key
   ```

3. Open the project in Android Studio and let it sync

4. Run the app on an emulator or physical device

## CI/CD Setup

The project uses GitHub Actions for continuous integration and deployment. The workflow is defined in `.github/workflows/android-test.yml`.

### Required Secrets

Add the following secrets in your GitHub repository settings (Settings → Secrets and variables → Actions):

- `MAPS_API_KEY`: Your Google Maps API key

### Artifacts

After a successful CI run, the following artifacts are generated:
- Debug APK
- Test reports
- Screenshots (for UI tests)

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/carparkingapp/
│   │   │   ├── core/
│   │   │   │   ├── architecture/
│   │   │   │   ├── di/
│   │   │   │   ├── network/
│   │   │   │   └── utils/
│   │   │   └── features/
│   │   │       ├── auth/
│   │   │       ├── parking/
│   │   │       └── profile/
│   │   └── res/
│   └── test/
└── build.gradle.kts
```

## Testing

1. Unit Tests:
   ```bash
   ./gradlew test
   ```

2. Instrumented Tests:
   ```bash
   ./gradlew connectedAndroidTest
   ```

3. UI Tests:
   ```bash
   ./gradlew connectedAndroidTest -PtestType=ui
   ```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## Code Style

Please refer to [CODE_STYLE.md](CODE_STYLE.md) for detailed coding standards and best practices.

## Troubleshooting

### Common Issues

1. **Build Failures**
   - Clean and rebuild the project
   - Invalidate caches and restart Android Studio
   - Check Gradle version compatibility

2. **API Key Issues**
   - Verify `local.properties` has correct API key
   - Check API key restrictions in Google Cloud Console
   - Ensure API key has required permissions

3. **Device Connection**
   - Enable USB debugging
   - Install proper USB drivers
   - Try different USB ports/cables

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Google Maps Platform
- Android Jetpack
- Kotlin Coroutines
- Hilt
- Retrofit
