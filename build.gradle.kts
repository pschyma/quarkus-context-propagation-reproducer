import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("com.github.ben-manes.versions")
	id("io.quarkus")
	kotlin("jvm")
	kotlin("plugin.allopen")
	base
	idea
}


group = "de.comline.eis"
version = "1.0.0-SNAPSHOT"

repositories {
	mavenLocal()
	mavenCentral()
	jcenter()
}

val quarkusPlatformGroupId: String by ext
val quarkusPlatformArtifactId: String by ext
val quarkusPlatformVersion: String by ext

dependencies {
	implementation(kotlin("stdlib-jdk8"))

	implementation(platform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
	implementation("io.quarkus:quarkus-kotlin")
	implementation("io.quarkus:quarkus-config-yaml")
	implementation("io.quarkus:quarkus-mutiny")
	implementation("io.quarkus:quarkus-resteasy")
	implementation("io.quarkus:quarkus-resteasy-jackson")
	implementation("io.quarkus:quarkus-resteasy-mutiny")
	//implementation("io.quarkus:quarkus-smallrye-fault-tolerance")
	implementation("io.quarkus:quarkus-smallrye-context-propagation")
	implementation("io.quarkus:quarkus-jdbc-mssql")
		implementation("io.quarkus:quarkus-flyway")
	implementation("io.quarkus:quarkus-hibernate-validator")

	implementation("org.jboss.resteasy:resteasy-multipart-provider")
	implementation("org.apache.poi:poi-ooxml:4.1.2")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	testImplementation("io.quarkus:quarkus-junit5")
	testImplementation("io.quarkus:quarkus-junit5-mockito")
	testImplementation("org.assertj:assertj-core")
	testImplementation("io.rest-assured:rest-assured:4.3.0")
	testImplementation("io.rest-assured:kotlin-extensions:4.3.0")
	testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
}

allOpen {
	annotation("javax.enterprise.context.ApplicationScoped")
	annotation("javax.ws.rs.Path")
	annotation("io.quarkus.test.junit.QuarkusTest")
}

tasks {
	withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs += "-Xemit-jvm-type-annotations"
			javaParameters = true
			jvmTarget = JavaVersion.VERSION_11.toString()
		}
	}

	test {
		useJUnitPlatform()

		jvmArgs("--add-opens", "java.base/java.lang=ALL-UNNAMED")
		jvmArgs("--add-opens", "java.base/java.lang.invoke=ALL-UNNAMED")

		systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")

		reports {
			junitXml.isEnabled = true
			html.isEnabled = true
		}
	}

	quarkusDev {
		setSourceDir("$projectDir/src/main/kotlin")
	}

	withType<DependencyUpdatesTask> {
		fun isNonStable(version: String): Boolean {
			val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
			val regex = "^[0-9,.v-]+(-r)?$".toRegex()
			val isStable = stableKeyword || regex.matches(version)
			return isStable.not()
		}

		resolutionStrategy {
			componentSelection {
				all {
					if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
						reject("Release candidate")
					}
				}
			}
		}
	}

	withType<Javadoc> {
		if (JavaVersion.current().isJava9Compatible) {
			(options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
		}
	}
}

configure<IdeaModel> {
	module {
		isDownloadSources = true
		isDownloadJavadoc = true
	}
}
