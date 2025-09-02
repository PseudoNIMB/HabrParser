# Compose Multiplatform Application

### Desktop

### Простейший парсер RSS ленты с хабра, имеющий локальную модель в виде:
### - Название статьи
### - Ссылка на статью
### - Дата публикации
### - Краткое содержание статьи

### Пункты, указанные выше, отключаемы. Присутствует сортировка по дате, релевантности и рейтингу.
### Возможность сохранения вывода программы в текстовый_документ.txt с диалоговым окном как в винде.

Run the desktop application (just run): `./gradlew :composeApp:run`

Compile desktopApp to Directory with .exe file to launch on any PC (that's the way): `./gradlew :composeApp:createDistributable`