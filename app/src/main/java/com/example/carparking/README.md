# Car Parking App - Package Structure

This project follows a feature-based architecture with clean architecture principles. The package structure is organized as follows:

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
│   │   ├── data/         # Data layer
│   │   ├── domain/       # Domain layer
│   │   └── presentation/ # UI layer
│   │
│   ├── parking/          # Parking management feature
│   │   ├── data/         # Data layer
│   │   ├── domain/       # Domain layer
│   │   └── presentation/ # UI layer
│   │
│   └── profile/          # User profile feature
│       ├── data/         # Data layer
│       ├── domain/       # Domain layer
│       └── presentation/ # UI layer
│
└── CarParkingApplication.kt  # Application class
```

## Architecture Layers

Each feature follows clean architecture principles with three main layers:

1. **Data Layer**
   - Repositories
   - Data sources (local and remote)
   - Data models
   - Mappers

2. **Domain Layer**
   - Use cases
   - Domain models
   - Repository interfaces

3. **Presentation Layer**
   - ViewModels
   - UI components
   - State management

## Best Practices

- Each feature is self-contained and can be developed independently
- Core functionality is shared across features
- Dependencies flow from outer layers to inner layers
- Each layer has its own models to prevent data leakage
- Use dependency injection for better testability 