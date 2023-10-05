import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	id("org.openapi.generator") version "6.6.0"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("plugin.jpa") version "1.8.22"
}

group = "com.agnugroh"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-mustache")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	api("org.openapitools:jackson-databind-nullable:0.2.1")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-security")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test:2.3.1.RELEASE")
	implementation("javax.servlet:servlet-api:2.5")
	implementation("javax.validation:validation-api:2.0.1.Final")
	runtimeOnly("org.apache.activemq:artemis-jakarta-server:2.31.0")
	implementation("org.springframework.boot:spring-boot-starter-websocket")
	implementation("org.springframework.boot:spring-boot-starter-artemis")
	implementation("org.webjars:webjars-locator-core")
	implementation("org.modelmapper:modelmapper:3.1.0")
	implementation("org.webjars:stomp-websocket:2.3.3")
	implementation("org.webjars:bootstrap:3.4.0")
	implementation("org.webjars:jquery:3.6.2")
	implementation("org.webjars:sockjs-client:1.0.2")
}

openApiGenerate {
	generatorName.set("kotlin-spring")
	inputSpec.set("$rootDir/src/main/resources/phone_booking.json")
	outputDir.set("$buildDir/generated")
	apiPackage.set("$group.phonebooking.api")
	modelPackage.set("$group.phonebooking.model")
	configOptions.set(mapOf(
		"dateLibrary" to "java8",
		"delegatePattern" to "true"
	))
}
sourceSets["main"].withConvention(conventionType = KotlinSourceSet::class) {
	kotlin.srcDir("$buildDir/generated/src/main/kotlin")
}
tasks.withType<KotlinCompile> {
	kotlinOptions {
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
tasks {
	val openApiGenerate by getting
	val compileKotlin by getting {
		dependsOn(openApiGenerate)
	}
}

springBoot {
	mainClass.set("com.tech.test.PhoneBookingApplicationKt")
}