cmake_minimum_required(VERSION 3.22.1)

add_library( # Имя библиотеки
        native-lib

        # Тип библиотеки
        SHARED

        # Источник библиотеки
        native-lib.c )

find_library( # Платформенная библиотека log
        log-lib
        log )

target_link_libraries( # Подключаем лог
        native-lib
        ${log-lib} )