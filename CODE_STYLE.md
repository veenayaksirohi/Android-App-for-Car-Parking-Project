# Code Style Guide

This document outlines the coding standards and best practices for the Car Parking Android project. For project setup and general information, please refer to the [README.md](README.md).

## Kotlin Style Guide

### Naming Conventions

1. **Files**
   - Use PascalCase for file names
   - Match file name with primary class name
   - Use descriptive names that reflect the content
   - Feature-specific files should be in their respective feature packages

2. **Classes and Objects**
   - Use PascalCase
   - Use nouns or noun phrases
   - Avoid abbreviations unless widely known
   - Suffix interfaces with 'Interface' (e.g., `RepositoryInterface`)
   - Suffix abstract classes with 'Base' (e.g., `BaseViewModel`)

3. **Functions**
   - Use camelCase
   - Use verbs or verb phrases
   - Start with a verb for actions
   - Use descriptive names that indicate purpose
   - Use suspend modifier for coroutine functions

4. **Variables and Properties**
   - Use camelCase
   - Use nouns or noun phrases
   - Make names descriptive and meaningful
   - Avoid single-letter names except for loops
   - Use val instead of var when possible

### Code Formatting

1. **Indentation**
   - Use 4 spaces for indentation
   - Don't use tabs

2. **Line Length**
   - Maximum line length: 100 characters
   - Break lines at logical points

3. **Spacing**
   - Use spaces around operators
   - Use spaces after commas
   - No spaces before commas
   - No spaces inside parentheses

4. **Braces**
   - Use braces for all control structures
   - Place opening brace on the same line
   - Place closing brace on a new line

### Code Organization

1. **File Structure**
   ```kotlin
   // License header
   package com.example.carparkingapp.features.featurename

   // Imports
   import android.os.Bundle
   import androidx.appcompat.app.AppCompatActivity
   import dagger.hilt.android.AndroidEntryPoint

   // Class documentation
   /**
    * Description of the class
    */
   @AndroidEntryPoint
   class FeatureClass {
       // Constants
       companion object {
           private const val TAG = "FeatureClass"
       }

       // Properties
       private var myProperty: String? = null

       // Initialization blocks
       init {
           // Initialization code
       }

       // Public methods
       fun publicMethod() {
           // Method implementation
       }

       // Private methods
       private fun privateMethod() {
           // Method implementation
       }
   }
   ```

2. **Import Order**
   - Android imports
   - Kotlin imports
   - Java imports
   - Third-party imports
   - Project imports

3. **Package Structure**
   ```
   com.example.carparkingapp/
   ├── core/
   │   ├── architecture/
   │   ├── di/
   │   ├── network/
   │   └── utils/
   └── features/
       ├── auth/
       ├── parking/
       └── profile/
   ```

### Documentation

1. **KDoc Comments**
   - Use KDoc for all public APIs
   - Include parameter descriptions
   - Include return value descriptions
   - Include exceptions if applicable
   - Use @see for related classes

2. **Code Comments**
   - Use comments to explain why, not what
   - Keep comments up to date
   - Remove commented-out code
   - Use TODO comments for future improvements

### Best Practices

1. **Null Safety**
   - Use nullable types (?) when appropriate
   - Use safe calls (?.) when possible
   - Use Elvis operator (?:) for null checks
   - Use lateinit for properties initialized in onCreate
   - Use by lazy for properties initialized on first use

2. **Coroutines**
   - Use structured concurrency
   - Handle exceptions properly
   - Use appropriate dispatchers
   - Avoid GlobalScope
   - Use viewModelScope in ViewModels

3. **Resource Management**
   - Use string resources for all text
   - Use dimension resources for sizes
   - Use color resources for colors
   - Use style resources for themes
   - Use vector drawables when possible

4. **Error Handling**
   - Use try-catch blocks appropriately
   - Log errors with proper context
   - Handle errors gracefully
   - Provide user-friendly error messages
   - Use sealed classes for error types

## Tools

The project uses the following tools to enforce code style:

1. **ktlint**
   - Enforces Kotlin code style
   - Runs automatically on build
   - Can be run manually with `./gradlew ktlintCheck`
   - Configuration in `ktlint.gradle.kts`

2. **detekt**
   - Performs static code analysis
   - Enforces additional rules
   - Can be run manually with `./gradlew detekt`
   - Configuration in `detekt.yml`

3. **Pre-commit Hook**
   - Runs ktlint and detekt before each commit
   - Prevents commits with style violations
   - Can be bypassed with `git commit --no-verify` (not recommended)

## IDE Setup

1. **Android Studio**
   - Enable "Show whitespaces"
   - Enable "Strip trailing spaces on Save"
   - Enable "Ensure line feed at file end on Save"
   - Use the provided code style settings
   - Install the following plugins:
     - Kotlin
     - ktlint
     - detekt

2. **Code Formatting**
   - Use "Reformat Code" (Ctrl+Alt+L) before committing
   - Use "Optimize Imports" (Ctrl+Alt+O) before committing
   - Use "Clean Project" before building

## Continuous Integration

The project's CI pipeline includes:
- ktlint checks
- detekt analysis
- Unit tests
- UI tests
- Build verification

All checks must pass before merging pull requests. For more details about CI/CD setup, see the [README.md](README.md).

## Version Control

1. **Branch Naming**
   - feature/feature-name
   - bugfix/bug-name
   - hotfix/issue-name
   - release/version

2. **Commit Messages**
   - Use present tense
   - Start with a verb
   - Keep it concise
   - Reference issue numbers

3. **Pull Requests**
   - Include issue reference
   - Add description of changes
   - Add screenshots if UI changes
   - Request review from team members

## Related Documentation

- [README.md](README.md) - Project overview, setup instructions, and general information
- [LICENSE](LICENSE) - Project license information
- `.github/workflows/android-test.yml` - CI/CD workflow configuration 