pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://sdk.uxcam.com/android/")
        maven {
            url = uri("http://maven.usehover.com/releases")
            isAllowInsecureProtocol = true
        }
        maven {
            url = uri("http://maven.usehover.com/snapshots")
            isAllowInsecureProtocol = true
        }
        maven {
            url = uri("https://pro.maven.usehover.com/snapshots")
            credentials {
                username = providers.gradleProperty("HoverUsername").orNull
                password = providers.gradleProperty("HoverPassword").orNull
            }
            authentication { create<BasicAuthentication>("basic") }
            content { includeGroup("com.hover") }
        }
    }
}

include(":app")
include(":hover.sdk")

rootProject.name = "Stax"