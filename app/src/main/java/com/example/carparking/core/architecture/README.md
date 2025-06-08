# MVVM Architecture Implementation

This document outlines the MVVM (Model-View-ViewModel) architecture implementation in the Car Parking Android application.

## Architecture Components

### 1. BaseViewModel
- Abstract base class for all ViewModels
- Handles state management using StateFlow
- Provides error and loading state management
- Implements event handling pattern

### 2. UiState
- Sealed class representing different UI states
- States: Loading, Success, Error, Empty
- Type-safe state handling

### 3. BaseRepository
- Interface defining common repository operations
- Generic type parameters for data and ID types
- CRUD operations with Flow support

## Implementation Guidelines

### ViewModels
```kotlin
class FeatureViewModel : BaseViewModel<FeatureState, FeatureEvent>() {
    override fun handleEvent(event: FeatureEvent) {
        when (event) {
            is FeatureEvent.LoadData -> loadData()
            is FeatureEvent.UpdateData -> updateData(event.data)
        }
    }
}
```

### UI States
```kotlin
data class FeatureState(
    val data: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
```

### Repositories
```kotlin
class FeatureRepository : BaseRepository<Item, String> {
    override fun getAll(): Flow<List<Item>> {
        // Implementation
    }
    
    override suspend fun getById(id: String): Item? {
        // Implementation
    }
    
    // Other implementations
}
```

## Best Practices

1. **State Management**
   - Use StateFlow for state management
   - Keep state immutable
   - Handle all possible states

2. **Error Handling**
   - Use sealed classes for errors
   - Provide meaningful error messages
   - Handle errors at appropriate levels

3. **Coroutines**
   - Use viewModelScope for coroutines
   - Handle cancellation properly
   - Use appropriate dispatchers

4. **Testing**
   - Test ViewModels in isolation
   - Mock repositories
   - Test state changes
   - Test error scenarios

## Directory Structure

```
core/
  architecture/
    BaseViewModel.kt
    UiState.kt
    BaseRepository.kt
    README.md
features/
  feature1/
    data/
      repository/
    domain/
      model/
    presentation/
      viewmodel/
      ui/
```

## Usage Example

```kotlin
// ViewModel
class ParkingSpotViewModel(
    private val repository: ParkingSpotRepository
) : BaseViewModel<ParkingSpotState, ParkingSpotEvent>() {
    
    init {
        loadParkingSpots()
    }
    
    private fun loadParkingSpots() {
        viewModelScope.launch {
            setLoading(true)
            try {
                repository.getAll().collect { spots ->
                    setState(ParkingSpotState(spots = spots))
                }
            } catch (e: Exception) {
                setError(e.message ?: "Unknown error")
            } finally {
                setLoading(false)
            }
        }
    }
    
    override fun handleEvent(event: ParkingSpotEvent) {
        when (event) {
            is ParkingSpotEvent.Refresh -> loadParkingSpots()
            is ParkingSpotEvent.SelectSpot -> selectSpot(event.spotId)
        }
    }
}

// UI State
data class ParkingSpotState(
    val spots: List<ParkingSpot> = emptyList(),
    val selectedSpot: ParkingSpot? = null
)

// Events
sealed class ParkingSpotEvent {
    object Refresh : ParkingSpotEvent()
    data class SelectSpot(val spotId: String) : ParkingSpotEvent()
}
```

## Benefits

1. **Separation of Concerns**
   - Clear separation between UI and business logic
   - Easy to maintain and test
   - Modular and reusable components

2. **State Management**
   - Predictable state updates
   - Easy to debug
   - Reactive UI updates

3. **Testability**
   - Easy to unit test
   - Can test business logic in isolation
   - Can mock dependencies

4. **Maintainability**
   - Clear architecture
   - Easy to add new features
   - Easy to modify existing features 