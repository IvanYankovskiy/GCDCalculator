# GCDCalculator
Calculator app for GCD service

Приложение вычисляет НОД двух чисел. Получение заданий от REST API и отправка результатов обратно выполняются асинхронно через RabbitMQ.
Версия RabbitMQ 3.7.6 
Версия Erlang 20.2

<H1>1. Начало работы</H1>

Скачайте исходные файлы, откройте pom.xml проекта в IDE, выполните сборку проекта.
Перейдите в папку проекта <i>GcdCalculator/target</i>, где будут расположены следующие архивы:


- calculator-distribution.tar - если вы желаете запустить приложение в Linux-системе; 
- calculator-distribution.zip - если вы желаете запустить приложение в Windows-системе;

Далее, переместите архив в удобное для вас расположение и распакуйте его.
- В linux-системе tar –xvf calculator-distribution.tar

В распакованной папке перейдите в
- <i>calculator-distribution/config</i>

и откройте файл настроек application.properties.

<H1>2. Настройки RabbitMQ</H1>
Если вы планируете использовать RabbitMQ, установленный на локальной машине, то проверьте настройки доступа с имененем пользователя и паролем. В приложении
используется пользователь по умолчанию:
  
```Java
messaging.rabbit.host.user = guest
messaging.rabbit.host.password = guest
```
Если у вас создан другой пользователь, то замените свойства выше на свои значения.
Расположение сервера с RabbitMQ так же используется по умолчанию:
  
```Java
messaging.rabbit.host.url = localhost
messaging.rabbit.host.port = 5672
```
Теперь перейдём к запуску приложения.

<H1>3. Запуск приложения</H1>

Для запуска приложения перейдите в распакованную на шаге <b>1</b> папку с приложением и перейдите в <i>calculator-distribution/bin</i>.

- Для запуска приложения в ОС Windows запустите <i>start.bat</i>.
- Для запуска приложения приложения в ОС Linux запустите <i>start.sh</i>.

Теперь сконфигурируйте REST API и запустите его. REST API: <link>https://github.com/IvanYankovskiy/GCDApi.git</link>.

Логи приложения вычислителя расположены в <i>calculator-distribution/bin/logs</i>
