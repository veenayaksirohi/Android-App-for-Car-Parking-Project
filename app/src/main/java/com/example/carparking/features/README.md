# Features

This package contains all the feature modules of the application. Each feature follows clean architecture principles and is organized into three layers: data, domain, and presentation.

## Feature Structure

Each feature follows this structure:
```
feature/
├── data/              # Data layer
│   ├── repository/    # Repository implementations
│   ├── source/        # Data sources (local/remote)
│   └── model/         # Data models
│
├── domain/            # Domain layer
│   ├── model/         # Domain models
│   ├── repository/    # Repository interfaces
│   └── usecase/       # Use cases
│
└── presentation/      # Presentation layer
    ├── ui/           # UI components
    ├── viewmodel/    # ViewModels
    └── state/        # UI state classes
```

## Available Features

### Authentication (`auth/`)
Handles user authentication and authorization:
- Login
- Registration
- Password reset
- Session management

### Parking Management (`parking/`)
Manages parking spaces and bookings:
- View available spaces
- Book parking space
- Manage bookings
- Payment processing

### User Profile (`profile/`)
Manages user profile and settings:
- View/edit profile
- Manage vehicles
- View booking history
- Notification preferences

## Feature Development Guidelines

1. **Data Layer**
   - Implement repository interfaces from domain layer
   - Handle data mapping between data and domain models
   - Implement local and remote data sources
   - Handle caching strategy

2. **Domain Layer**
   - Define use cases for business logic
   - Create domain models
   - Define repository interfaces
   - Keep business logic independent of Android framework

3. **Presentation Layer**
   - Implement UI using Jetpack Compose
   - Use ViewModel for state management
   - Handle user events
   - Implement navigation

## State Management

Each feature should:
- Use sealed classes for UI state
- Handle loading, error, and success states
- Implement proper error handling
- Follow unidirectional data flow

Example:
```kotlin
sealed class FeatureState {
    object Loading : FeatureState()
    data class Success(val data: Data) : FeatureState()
    data class Error(val message: String) : FeatureState()
}
```

## Navigation

Features should:
- Define their own navigation graphs
- Use type-safe arguments
- Handle deep links
- Implement proper back stack management

## Testing

Each feature should include:
- Unit tests for use cases
- Integration tests for repositories
- UI tests for screens
- Navigation tests

## Contributing

When adding a new feature:
1. Follow the established architecture
2. Implement all three layers
3. Add proper documentation
4. Include tests
5. Update this documentation 