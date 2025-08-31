import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
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
        artifactId = "tidy-data-storage",
        version = "1.0.1"
    )

    pom {
        name.set("Tidy Data Storage")
        description.set("A simple and efficient key-value storage library for Android using DataStore.")
        url.set("https://github.com/Sami00777/tiday-data-storage")
        inceptionYear.set("2025")

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/license/mit/")
            }
        }

        developers {
            developer {
                id.set("sami00777")
                name.set("Sam Shamshiri")
                email.set("sam.sh00777@gmail.com")
            }
        }

        scm {
            url.set("https://github.com/Sami00777/tiday-data-storage")
            connection.set("scm:git:git://github.com/Sami00777/tiday-data-storage.git")
            developerConnection.set("scm:git:ssh://git@github.com/Sami00777/tiday-data-storage.git")
        }
    }

    // Configure publishing to Maven Central
    publishToMavenCentral(automaticRelease = true)

    // Enable signing for all publications
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
