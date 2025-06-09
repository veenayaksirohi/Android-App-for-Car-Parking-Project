# Car Parking Android App

A modern Android application for managing car parking spaces, built with Kotlin and following clean architecture principles.

## Features

- User authentication and profile management
- Real-time parking space availability
- Parking space booking and management
- Payment integration
- Offline support
- Push notifications

## Tech Stack

- **Language**: Kotlin
- **Architecture**: Clean Architecture with MVVM
- **Dependency Injection**: Hilt
- **Asynchronous**: Kotlin Coroutines & Flow
- **Database**: Room
- **Networking**: Retrofit & OkHttp
- **Image Loading**: Coil
- **UI**: Jetpack Compose
- **Testing**: JUnit, Mockito, Espresso

## Project Structure

The project follows a feature-based architecture with clean architecture principles:

```
com.example.carparking/
├── core/                    # Core functionality and utilities
│   ├── di/                 # Dependency injection modules
│   ├── network/            # Network related code
│   ├── database/           # Local database
│   └── utils/              # Utility classes
│
├── features/               # Feature modules
│   ├── auth/              # Authentication feature
│   ├── parking/          # Parking management feature
│   └── profile/          # User profile feature
```

## Getting Started

### Prerequisites

- Android Studio Arctic Fox or newer
- JDK 11 or newer
- Android SDK 21 or newer

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/car-parking-android.git
   ```

2. Open the project in Android Studio

3. Sync the project with Gradle files

4. Run the app on an emulator or physical device

## Architecture

The app follows Clean Architecture principles with three main layers:

1. **Presentation Layer**
   - UI components (Activities, Fragments, Composables)
   - ViewModels
   - UI State management

2. **Domain Layer**
   - Use cases
   - Domain models
   - Repository interfaces

3. **Data Layer**
   - Repositories implementation
   - Data sources (local and remote)
   - Data models

## Testing

The project includes different types of tests:

- **Unit Tests**: For business logic and use cases
- **Integration Tests**: For repository and data source testing
- **UI Tests**: For UI components and user flows

Run tests using:
```bash
./gradlew test        # Unit tests
./gradlew connectedAndroidTest  # Instrumented tests
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

Your Name - your.email@example.com

Project Link: [https://github.com/yourusername/car-parking-android](https://github.com/yourusername/car-parking-android)
