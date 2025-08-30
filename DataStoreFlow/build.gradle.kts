plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.vanniktech.maven.publish)
    id("signing")
}

android {
    namespace = "io.github.sami00777.datastoreflow"
    compileSdk = 36

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.datastore.preferences)
}

mavenPublishing {
    coordinates(
        groupId = "io.github.sami00777",
        artifactId = "datastoreflow",
        version = "1.0.0"
    )
    pom {
        name.set("DataStoreFlow")
        description.set("A Kotlin library that simplifies DataStore operations using Kotlin Flow.")
        url.set("https://github.com/Sami00777/android-datastore-flow")
        inceptionYear.set("2025")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/license/mit/")
            }
        }
        developers {
            developer {
                id.set("Sami00777")
                name.set("Sam Shamshiri")
                email.set("sam.sh00777@gmail.com")
            }

            scm {
                url.set("https://github.com/Sami00777/android-datastore-flow")
                connection.set("scm:git:git://github.com/Sami00777/android-connectivity-monitoring.git")
                developerConnection.set("scm:git:ssh://git@github.com/Sami00777/android-connectivity-monitoring.git")
            }
        }
    }
    publishToMavenCentral(automaticRelease = true)
    signAllPublications()
}

// Configure signing properly
signing {
    val signingKey = System.getenv("SIGNING_KEY")
    val signingPassword = System.getenv("SIGNING_PASSWORD")

    if (!signingKey.isNullOrBlank() && !signingPassword.isNullOrBlank()) {
        useInMemoryPgpKeys(signingKey, signingPassword)
    }
}

// Configure publications after evaluation
afterEvaluate {
    val signingKey = System.getenv("SIGNING_KEY")
    val signingPassword = System.getenv("SIGNING_PASSWORD")

    if (!signingKey.isNullOrBlank() && !signingPassword.isNullOrBlank()) {
        signing {
            sign(publishing.publications)
        }
    }
}

// Configure signing properly
//signing {
//    val signingKey = System.getenv("SIGNING_KEY")
//    val signingPassword = System.getenv("SIGNING_PASSWORD")
//
//    if (!signingKey.isNullOrBlank() && !signingPassword.isNullOrBlank()) {
//        useInMemoryPgpKeys(signingKey, signingPassword)
//    }
//}

// Debug task to check environment variables
//tasks.register("checkEnvVars") {
//    doLast {
//        val signingKey = System.getenv("SIGNING_KEY")
//        val signingPassword = System.getenv("SIGNING_PASSWORD")
//
//        println("SIGNING_KEY is ${if (signingKey.isNullOrBlank()) "NOT SET" else "SET (${signingKey.length} characters)"}")
//        println("SIGNING_PASSWORD is ${if (signingPassword.isNullOrBlank()) "NOT SET" else "SET"}")
//    }
//}

// Task to print actual environment variable values (for debugging purposes)
//tasks.register("printEnvVars") {
//    doLast {
//        val signingKey = System.getenv("SIGNING_KEY")
//        val signingPassword = System.getenv("SIGNING_PASSWORD")
//
//        println("SIGNING_KEY: $signingKey")
//        println("SIGNING_PASSWORD: $signingPassword")
//    }
//}