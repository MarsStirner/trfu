# ТРФУ - трансфузиология

## Зависимости

Требуемое ПО

 * Для развертывания проекта
  * Maven 3.0.3+
  * Glassfish v3.1+
  * jdk 1.6.0+

## Настройки приложения TRFU

 Примеры настроек хранятся в папке проекта trfu-settings-example.
 Необходимо настроить соединение к базе данных MySQL, файл static/repository.properties

    jdbc.url=jdbc:mysql://{адрес базы данных}:{порт}/{имя базы данных}?useUnicode=true&autoReconnect=true&characterEncoding=UTF-8
    jdbc.username={логин пользователя}
    jdbc.password={пароль пользователя}

## Установка ПО

    * Установить jdk, прописать системную переменную JAVA_HOME.
    * Установить maven, прописать системную переменную M2_HOME.
    * Установить glassfish, создать домен с нужным именем.
    * Установить mysql.
    * Прописать в системную переменную PATH, подпапки bin из всех выше перечисленных программ.
    * Настроить glassfish:
        ** Если веб интерфейс администратора не пускает (Enable Secure Admin to access the DAS),
        выполнить команду ./asadmin enable-secure-admin
        ** Для текущей конфигурации в раздел Common Tasks → Configurations → server-config → JVM Settings → JVM Options
        добавить параметр -Defive.trfu.home=/opt/trfu/trfu-settings-example/, указывающий на абсолютный путь к папке с
        настроенной конфигурацией.
    * Настроить mysql:
        ** Выполнить скрипт trfu/sql/create-database-trfu.sql для создания базы данных
        ** Если необходим тестовый пользователь, выполнить скрипт trfu/sql/create-person-trfu.sql, будет создан
          тестовый администратор с аккаунтом Admin/Admin

## Сборка приложения

    * Из директории trfu выполнить команду mvn clean install
    * В директории trfu/ear/target/ появится EJB приложение trfu-ear-0.0.1-SNAPSHOT.ear, готовое для установки на сервер приложений glassfish.


## Установка приложения

    * В веб консоли администратора glassfish по адресу http://127.0.0.1:4848 в пунке Application, выбрать Deploy, указать нужный ear файл приложения,
    выбрать инстанс server (по умолчанию), куда будет установлено приложение. После нажатия ОК, приложение будет установлено.


## Лог-файл приложения

    * На текущий момент логи приложения пишутся в серверные логи glassfish, по пути ${com.sun.aas.instanceRoot}/logs
    P.S. ${com.sun.aas.instanceRoot} - это папка установленого домена в glassfish, например, /usr/local/glassfish3/glassfish/domains/fccho-moscow/