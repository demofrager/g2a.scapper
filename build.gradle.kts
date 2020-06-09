plugins {
    id ("org.jetbrains.kotlin.jvm") version "1.3.72"
    application
}

repositories {
    jcenter()
}

dependencies {
    implementation("it.skrape:skrapeit-core:1.0.0-alpha6")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.apache.commons:commons-email:1.5")
    implementation("com.natpryce:konfig:1.6.10.0")
    testImplementation ("junit:junit:4.12")
}


application{
    mainClassName = "g2a.G2aKt"
}



