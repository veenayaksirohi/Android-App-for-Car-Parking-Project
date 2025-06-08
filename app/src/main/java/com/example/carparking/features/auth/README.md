# Authentication Feature

This feature handles user authentication in the Car Parking application.

## Package Structure

```
auth/
├── data/
│   ├── repository/
│   │   └── AuthRepositoryImpl.kt
│   └── source/
│       └── AuthRemoteSource.kt
├── domain/
│   ├── model/
│   │   └── User.kt
│   ├── repository/
│   │   └── AuthRepository.kt
│   └── usecase/
│       ├── LoginUseCase.kt
│       └── RegisterUseCase.kt
└── presentation/
    ├── ui/
    │   ├── LoginActivity.kt
    │   └── RegisterActivity.kt
    └── viewmodel/
        └── AuthViewModel.kt
```

## Components

### Domain Layer

1. **Models**
   - `User`: Data class representing a user in the system

2. **Repository Interface**
   - `AuthRepository`: Defines authentication operations
   - Methods:
     - `login(email, password)`: Authenticates a user
     - `register(name, email, password)`: Creates a new user
     - `logout()`: Signs out the current user
     - `getCurrentUser()`: Gets the current user
     - `isUserLoggedIn()`: Checks if a user is logged in

3. **Use Cases**
   - `LoginUseCase`: Handles user login
   - `RegisterUseCase`: Handles user registration

### Presentation Layer

1. **ViewModel**
   - `AuthViewModel`: Manages authentication state and operations
   - Features:
     - Login state management
     - Registration state management
     - Error handling
     - Loading state management

2. **UI**
   - `LoginActivity`: Login screen
   - `RegisterActivity`: Registration screen

## Usage

```kotlin
// In your Activity or Fragment
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Observe auth state
        lifecycleScope.launch {
            viewModel.authState.collect { state ->
                when (state) {
                    is UiState.Loading -> showLoading()
                    is UiState.Success -> navigateToMain()
                    is UiState.Error -> showError(state.message)
                    else -> Unit
                }
            }
        }
        
        // Login
        loginButton.setOnClickListener {
            viewModel.login(emailInput.text.toString(), passwordInput.text.toString())
        }
    }
}
```

## Dependencies

- Hilt for dependency injection
- Coroutines for asynchronous operations
- Flow for reactive state management
- ViewModel for UI state management

## Testing

1. **Unit Tests**
   - Test use cases
   - Test ViewModel
   - Test repository implementation

2. **Integration Tests**
   - Test API integration
   - Test data flow

3. **UI Tests**
   - Test login flow
   - Test registration flow
   - Test error scenarios 