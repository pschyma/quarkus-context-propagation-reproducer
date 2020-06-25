rootProject.name = "reprodcuer"

pluginManagement {
	repositories {
		mavenLocal()
		mavenCentral()
		gradlePluginPortal()
	}

	val kotlinVersion: String by settings
	val quarkusPluginVersion: String by settings

	plugins {
		id("org.jetbrains.kotlin.jvm") version (kotlinVersion)
		id("org.jetbrains.kotlin.plugin.allopen") version (kotlinVersion)
		id("org.jetbrains.kotlin.plugin.noarg") version (kotlinVersion)
		id("org.jetbrains.kotlin.plugin.jpa") version (kotlinVersion)
		id("com.github.ben-manes.versions") version ("0.28.0")

		id("io.quarkus") version (quarkusPluginVersion)
	}
}
