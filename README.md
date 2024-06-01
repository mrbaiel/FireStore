# FireStore
Its shoping platform for students for my univetsity.

Для запуска перейдите в директорию Apk/apk-debug.apk дальше установите доверившийся приложению и подождите пока пройдет проверку вашей системы

APK -> apk-debug.apk -> ... -> Download

После установки введите номер телефона и пароль, советую ставить легкие и запоминяющиеся пароли.
После регистрации введите данные в поля входа.

Для разработки было использовано язык Java и библиотеки для разработки мобильного приложения.

Firestore App

Firestore App — это Android-приложение, которое использует Firebase для аутентификации и базы данных. Это приложение включает функции авторизации пользователей, хранения данных в реальном времени, загрузки изображений и многое другое.
Требования

Перед началом работы убедитесь, что у вас установлены следующие инструменты:

    Android Studio Arctic Fox или новее
    JDK 8 или новее
    Интернет-соединение

Установка

    Клонируйте репозиторий на локальный компьютер:

    bash

git clone https://github.com/yourusername/FirestoreApp.git

Откройте проект в Android Studio:

bash

File -> Open -> выберите папку проекта

Синхронизируйте проект с Gradle:

bash

Tools -> Android -> Sync Project with Gradle Files

Настройте Firebase для вашего проекта:

    Перейдите на Firebase Console.
    Создайте новый проект или выберите существующий.
    Добавьте ваше Android-приложение в проект Firebase, следуя инструкциям.
    Скачайте файл google-services.json и добавьте его в папку app/ вашего проекта.
    Включите необходимые сервисы Firebase (Authentication, Realtime Database, Firestore, Storage).

Запустите проект на устройстве или эмуляторе:

bash

    Run -> Run 'app'

Зависимости

Проект использует следующие основные зависимости:

gradle

dependencies {
    implementation("com.android.tools.build:gradle:7.0.1")

    implementation(libs.appcompat)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.github.rey5137:material:1.3.1")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.0.0")
    implementation("com.google.firebase:firebase-database:20.0.5")
    implementation("com.google.firebase:firebase-auth:21.0.3")
    implementation("com.google.firebase:firebase-firestore:24.0.2")
    implementation("com.firebaseui:firebase-ui-database:8.0.2")
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    implementation("com.google.firebase:firebase-storage:21.0.0")
    implementation("io.github.pilgr:paperdb:2.7.2")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.squareup.picasso:picasso:2.8")
    androidTestImplementation("org.testng:testng:6.9.6")
    implementation("com.google.android.material:material:1.6.0")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("com.theartofdev.edmodo:android-image-cropper:2.8.+")
}

Функциональность

    Авторизация пользователей: Регистрация и вход через Firebase Authentication.
    База данных в реальном времени: Использование Firebase Realtime Database для хранения и получения данных.
    Загрузка и отображение изображений: Использование Firebase Storage для загрузки изображений и Picasso для их отображения.
    Поддержка offline режима: Работа с базой данных даже при отсутствии интернета.

Структура проекта

    UI: Все активити и фрагменты приложения.
    Model: Модели данных, используемые в приложении.
    Prevalent: Класс для хранения глобальных переменных и констант.
    Ресурсы: XML файлы макетов, стили, строки и другие ресурсы.

Вклад

Если вы хотите внести вклад в проект, пожалуйста, создайте форк репозитория и отправьте запрос на слияние (pull request). Мы будем рады вашим предложениям и улучшениям.
Лицензия

Этот проект лицензируется в соответствии с условиями лицензии MIT. Подробности можно найти в файле LICENSE.

Настройте этот шаблон в соответствии с вашими конкретными требованиями и особенностями проекта.
