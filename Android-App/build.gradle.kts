plugins {
    // Update versions to 8.9.1 or higher to match your AndroidX dependencies
    id("com.android.application") version "8.9.1" apply false
    id("com.android.library") version "8.9.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false

    // Ensure the Google Services plugin is also defined
    id("com.google.gms.google-services") version "4.4.1" apply false
}