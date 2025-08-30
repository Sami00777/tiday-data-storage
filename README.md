# DataStore Flow

A modern, type-safe Android library that provides a clean and intuitive API for persistent key-value storage using Android DataStore. Built with Kotlin Coroutines and Flow for reactive data handling.

## ✨ Features

- 🚀 **Simple API** - Easy-to-use interface for storing and retrieving data
- 🔄 **Reactive** - Observe data changes with Kotlin Flow
- 🛡️ **Type Safe** - Supports multiple data types with compile-time safety
- ⚡ **Asynchronous** - Built on Coroutines for non-blocking operations
- 🎯 **Result Handling** - Comprehensive result states (Loading, Success, Error)
- 🧹 **Clean Architecture** - Well-structured with separation of concerns

## 📱 Supported Data Types

- `String`
- `Int`
- `Boolean` 
- `Float`
- `Long`
- `Double`
- `Set<String>`

## 🚀 Installation

Add the dependency to your app's `build.gradle` file:

```gradle
dependencies {
    implementation 'io.github.sami00777:tiday-data-storage:1.0.0'
}
```

## 📖 Quick Start

### 1. Initialize DataStore Module

```kotlin
class MyApplication : Application() {
    lateinit var dataStoreModule: DataStoreModule
    
    override fun onCreate() {
        super.onCreate()
        dataStoreModule = DataStoreModule(this)
    }
}
```

### 2. Access TidyStorage

```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var tidyStorage: TidyStorage
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val app = application as MyApplication
        tidyStorage = app.dataStoreModule.tidyStorage
    }
}
```

### 3. Save Data

```kotlin
lifecycleScope.launch {
    // Save different types of data
    tidyStorage.saveValue("username", "John Doe")
    tidyStorage.saveValue("user_id", 12345)
    tidyStorage.saveValue("is_logged_in", true)
    tidyStorage.saveValue("user_score", 98.5f)
}
```

### 4. Retrieve Data

```kotlin
lifecycleScope.launch {
    tidyStorage.getValue("username", "Guest")
        .onSuccess { username ->
            println("Welcome, $username!")
        }
        .onError { error ->
            println("Failed to get username: ${error.message}")
        }
}
```

### 5. Observe Data Changes

```kotlin
class UserViewModel : ViewModel() {
    private val tidyStorage = // ... get instance
    
    val username: StateFlow<StorageResult<String>> = 
        tidyStorage.observeValue("username", "Guest")
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = StorageResult.Loading
            )
}

// In your Activity/Fragment
lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.username.collect { result ->
            when (result) {
                is StorageResult.Loading -> {
                    // Show loading state
                }
                is StorageResult.Success -> {
                    binding.usernameText.text = result.data
                }
                is StorageResult.Error -> {
                    // Handle error
                    Toast.makeText(this@MainActivity, 
                        "Error: ${result.exception.message}", 
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
```

## 🔧 Advanced Usage

### Dependency Injection with Hilt

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object StorageModule {
    
    @Provides
    @Singleton
    fun provideDataStoreModule(@ApplicationContext context: Context): DataStoreModule {
        return DataStoreModule(context)
    }
    
    @Provides
    @Singleton
    fun provideTidyStorage(dataStoreModule: DataStoreModule): TidyStorage {
        return dataStoreModule.tidyStorage
    }
}

// In your ViewModel
@HiltViewModel
class UserViewModel @Inject constructor(
    private val tidyStorage: TidyStorage
) : ViewModel() {
    // Your implementation
}
```

### Data Management Operations

```kotlin
// Remove specific value
tidyStorage.removeValue("old_preference_key")

// Clear all stored data
tidyStorage.clearAll()

// Working with Sets
val favoriteColors = setOf("Red", "Blue", "Green")
tidyStorage.saveValue("favorite_colors", favoriteColors)

tidyStorage.getValue("favorite_colors", emptySet<String>())
    .onSuccess { colors ->
        println("Favorite colors: ${colors.joinToString()}")
    }
```

### Error Handling Patterns

```kotlin
// Chaining operations
tidyStorage.getValue("user_settings", "default")
    .onSuccess { settings ->
        // Process successful result
        updateUI(settings)
    }
    .onError { error ->
        // Handle specific error types
        when (error) {
            is IOException -> showNetworkError()
            is SecurityException -> showPermissionError()
            else -> showGenericError(error.message)
        }
    }
```

## 🏗️ Architecture

The library follows clean architecture principles with clear separation of concerns:

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   TidyStorage   │───▶│ StorageRepository │───▶│ KeyValueStorage │
│   (Domain)      │    │   (Use Cases)    │    │ (Data Layer)    │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                                        │
                                                        ▼
                                                ┌─────────────────┐
                                                │ Android DataStore│
                                                │  (Preferences)  │
                                                └─────────────────┘
```

## 🧪 Testing

The library is designed with testability in mind. You can easily mock the `KeyValueStorage` interface for unit testing:

```kotlin
class MockKeyValueStorage : KeyValueStorage {
    private val storage = mutableMapOf<String, Any?>()
    
    override suspend fun <T> getValue(key: String, defaultValue: T): T {
        @Suppress("UNCHECKED_CAST")
        return storage[key] as? T ?: defaultValue
    }
    
    override suspend fun <T> setValue(key: String, value: T) {
        storage[key] = value
    }
    
    // Implement other methods...
}
```

## 📋 API Reference

### TidyStorage

| Method | Description |
|--------|-------------|
| `saveValue(key, value)` | Saves a value with the specified key |
| `getValue(key, defaultValue)` | Retrieves a value or returns default |
| `observeValue(key, defaultValue)` | Observes value changes as Flow |
| `removeValue(key)` | Removes a specific value |
| `clearAll()` | Clears all stored data |

### StorageResult

| State | Description |
|-------|-------------|
| `Loading` | Operation in progress |
| `Success<T>(data)` | Successful operation with data |
| `Error(exception)` | Failed operation with error details |


## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
