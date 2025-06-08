# Core Components

This package contains the core functionality and utilities used throughout the application.

## Base Classes

### BaseViewModel
Located at: `core/presentation/BaseViewModel.kt`

A base ViewModel class that provides common functionality for all feature ViewModels:
- State management using StateFlow
- Error handling
- Loading state management
- Event handling

Usage:
```kotlin
class MyFeatureViewModel : BaseViewModel<MyState, MyEvent>() {
    override fun handleEvent(event: MyEvent) {
        when (event) {
            is MyEvent.LoadData -> loadData()
            // Handle other events
        }
    }
}
```

### BaseRepository
Located at: `core/data/BaseRepository.kt`

A base repository interface that defines common CRUD operations:
- Get single item
- Get all items
- Insert item
- Update item
- Delete item
- Delete all items

Usage:
```kotlin
class MyRepository : BaseRepository<MyEntity> {
    override suspend fun get(id: String): MyEntity? {
        // Implementation
    }
    // Implement other methods
}
```

## Utilities

### Constants
Located at: `core/utils/Constants.kt`

Contains application-wide constants:
- Network configuration
- Database configuration
- Shared preferences keys
- Validation rules

## Dependency Injection

### AppModule
Located at: `core/di/AppModule.kt`

Provides application-wide dependencies:
- Network client
- Database
- Shared preferences
- Other singleton dependencies

## Network

### NetworkModule
Located at: `core/network/NetworkModule.kt`

Provides network-related dependencies:
- Retrofit instance
- API services
- Interceptors
- Error handling

## Database

### DatabaseModule
Located at: `core/database/DatabaseModule.kt`

Provides database-related dependencies:
- Room database instance
- DAOs
- Type converters
- Migrations

## Best Practices

1. **State Management**
   - Use StateFlow for state management
   - Keep state immutable
   - Handle state updates in ViewModel

2. **Error Handling**
   - Use sealed classes for error types
   - Handle errors at appropriate levels
   - Provide user-friendly error messages

3. **Dependency Injection**
   - Use constructor injection
   - Keep modules focused and small
   - Use qualifiers for similar dependencies

4. **Testing**
   - Write unit tests for utilities
   - Mock dependencies in tests
   - Test error cases

## Contributing

When adding new core functionality:
1. Follow the existing patterns
2. Add proper documentation
3. Include unit tests
4. Update this documentation 