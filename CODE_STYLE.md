# Code Style Guide

This document outlines the coding standards and best practices for the Car Parking Android project.

## Kotlin Style Guide

### Naming Conventions

1. **Files**
   - Use PascalCase for file names
   - Match file name with primary class name
   - Use descriptive names that reflect the content

2. **Classes and Objects**
   - Use PascalCase
   - Use nouns or noun phrases
   - Avoid abbreviations unless widely known

3. **Functions**
   - Use camelCase
   - Use verbs or verb phrases
   - Start with a verb for actions
   - Use descriptive names that indicate purpose

4. **Variables and Properties**
   - Use camelCase
   - Use nouns or noun phrases
   - Make names descriptive and meaningful
   - Avoid single-letter names except for loops

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
   package com.example.carparking

   // Imports
   import android.os.Bundle
   import androidx.appcompat.app.AppCompatActivity

   // Class documentation
   /**
    * Description of the class
    */
   class MyClass {
       // Constants
       companion object {
           private const val TAG = "MyClass"
       }

       // Properties
       private var myProperty: String? = null

       // Initialization blocks
       init {
           // Initialization code
       }

       // Constructors
       constructor() {
           // Constructor code
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

### Documentation

1. **KDoc Comments**
   - Use KDoc for all public APIs
   - Include parameter descriptions
   - Include return value descriptions
   - Include exceptions if applicable

2. **Code Comments**
   - Use comments to explain why, not what
   - Keep comments up to date
   - Remove commented-out code

### Best Practices

1. **Null Safety**
   - Use nullable types (?) when appropriate
   - Use safe calls (?.) when possible
   - Use Elvis operator (?:) for null checks
   - Use lateinit for properties initialized in onCreate

2. **Coroutines**
   - Use structured concurrency
   - Handle exceptions properly
   - Use appropriate dispatchers
   - Avoid GlobalScope

3. **Resource Management**
   - Use string resources for all text
   - Use dimension resources for sizes
   - Use color resources for colors
   - Use style resources for themes

4. **Error Handling**
   - Use try-catch blocks appropriately
   - Log errors with proper context
   - Handle errors gracefully
   - Provide user-friendly error messages

## Tools

The project uses the following tools to enforce code style:

1. **ktlint**
   - Enforces Kotlin code style
   - Runs automatically on build
   - Can be run manually with `./gradlew ktlintCheck`

2. **detekt**
   - Performs static code analysis
   - Enforces additional rules
   - Can be run manually with `./gradlew detekt`

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

2. **Code Formatting**
   - Use "Reformat Code" (Ctrl+Alt+L) before committing
   - Use "Optimize Imports" (Ctrl+Alt+O) before committing

## Continuous Integration

The project's CI pipeline includes:
- ktlint checks
- detekt analysis
- Unit tests
- UI tests

All checks must pass before merging pull requests. 